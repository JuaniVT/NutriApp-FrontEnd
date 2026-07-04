import { Component, EventEmitter, inject, Input, Output, signal } from '@angular/core';
import { HidratacionEntradaDTO } from '../../models/hidratacion';
import { HidratacionService } from '../../service/hidratacion-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-hidratacion-component',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './hidratacion-component.html',
  styleUrl: './hidratacion-component.css',
})
export class HidratacionComponent {
  @Output() registroExitoso = new EventEmitter<void>();
  private hidratacionService = inject(HidratacionService);
  @Input() fecha?: string;
   @Input() totalMl?: number;
 // 1. Definir el emisor de eventos
  @Output() totalCargado = new EventEmitter<number>();

  // Variables simples para que ngModel funcione perfecto
  cantidad: number | null = null;
  loading: boolean = false;
  mensaje: string | null = null;
  isError: boolean = false;

  // Nuevas propiedades para la UI de hidratación
  metaDiaria: number = 2000; // 2000ml como meta por defecto
  totalVasos: number = 8;    // 8 vasos por defecto

  // Arreglo para iterar en el template para los vasos
  vasosArray: number[] = Array.from({ length: this.totalVasos }, (_, i) => i + 1);

  obtenerTotal(fecha: string) {
  // 1. Log inicial
  console.log('Hijo: Petición iniciada para:', fecha);
  
  this.hidratacionService.obtenerTotalPorFecha(fecha).subscribe({
    next: (total) => {
      // 2. Log de éxito
      console.log('Hijo: Valor recibido de API:', total);
      this.totalMl = total;
      this.vasosArray = Array.from({ length: this.totalVasos }, (_, i) => i + 1);
      this.totalCargado.emit(total);
    },
    error: (err) => console.error('Hijo: Error en API', err)
  });
}


  registrar() {
    if (!this.cantidad || this.cantidad <= 0) {
      this.mostrarMensaje('Ingresa una cantidad válida.', true);
      return;
    }

    this.loading = true;
    this.mensaje = null;

    this.hidratacionService.registrar({ cantidadMl: this.cantidad }, this.fecha).subscribe({
      next: (res) => {
        this.mostrarMensaje(`¡Guardado!`, false);
        this.cantidad = null;
        this.loading = false;
        this.obtenerTotal(this.fecha!); // Recargar el total
        this.registroExitoso.emit();
      },
      error: () => {
        this.mostrarMensaje('Error al conectar.', true);
        this.loading = false;
      }
    });
  }

  registrarRapido(cantidad: number) {
  this.cantidad = cantidad;
  this.registrar(); // Reutilizas tu lógica existente
}

  private mostrarMensaje(texto: string, error: boolean) {
    this.mensaje = texto;
    this.isError = error;
    setTimeout(() => this.mensaje = null, 3000);
  }

  // --- Getters para la lógica de la UI (equivalentes a los 'computed' de Vue) ---

  get mlPorVaso(): number {
    return this.metaDiaria / this.totalVasos;
  }

  get porcentaje(): number {
    if (this.metaDiaria === 0) return 0;
    return Math.round((this.totalMl! / this.metaDiaria) * 100);
  }

  get porcentajeVisual(): number {
    return Math.min(this.porcentaje, 100);
  }

  get metaCumplida(): boolean {
    return this.totalMl! >= this.metaDiaria;
  }

  get mlFaltantes(): number {
    return Math.max(this.metaDiaria - this.totalMl!, 0);
  }

  get colorProgreso(): string {
    if (this.porcentaje >= 100) return '#16a34a'; // verde, meta cumplida
    if (this.porcentaje >= 60) return '#3b82f6';  // azul, buen avance
    return '#f59e0b'; // ámbar, recién arrancando
  }

  // --- Métodos para el template ---
  fillRatio(i: number): number {
    const glassStart = (i - 1) * this.mlPorVaso;
    const ratio = (this.totalMl! - glassStart) / this.mlPorVaso;
    return Math.max(0, Math.min(1, ratio));
  }
}
