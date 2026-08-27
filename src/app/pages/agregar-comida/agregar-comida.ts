import { Component, Input, Output, EventEmitter, inject, signal } from '@angular/core';

import { DiaService } from '../../service/dia-service';
import { AlimentoBusquedaDTO } from '../../models/alimentoBusquedadto';
import { ComidaIngeridaDTO } from '../../models/comidaingeridadto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LogroService } from '../../service/logro';
import { LogroHistorial } from '../../models/logro-historial';
import { errorContext } from 'rxjs/internal/util/errorContext';
import { NotificacionLogro } from '../../service/notificacion-logro';

@Component({
  selector: 'app-agregar-comida',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './agregar-comida.html',
  styleUrl: './agregar-comida.css',
})
export class AgregarComidaComponent {
  //service para las notificaciones de los logros
  private readonly notificacionLogroService = inject(NotificacionLogro);

  // agregar-comida.component.ts
  mostrarModal: boolean = false;   // 👈 NECESARIO
 @Input() tipoComida!: 'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK';
  @Input() fecha?: string;
  @Output() comidaAgregada = new EventEmitter<void>();
  @Output() cerrar = new EventEmitter<void>();

  filtro: string = '';
  alimentos: AlimentoBusquedaDTO[] = [];
  alimentoSeleccionado?: AlimentoBusquedaDTO;
  gramos: number = 100;

  constructor(private diaService: DiaService) {}

  buscarAlimentos() {
    if (!this.filtro) return;

    this.diaService.buscarAlimentos(this.filtro).subscribe({
      next: res => this.alimentos = res,
      error: err => console.error(err)
    });
  }

  seleccionarAlimento(alimento: AlimentoBusquedaDTO) {
    this.alimentoSeleccionado = alimento;
  }

  agregar() {
    if (!this.alimentoSeleccionado) return;

    const comida: ComidaIngeridaDTO = {
      id: this.alimentoSeleccionado.fdcId,
      nombreComida: this.alimentoSeleccionado.descripcion,
      gramos: this.gramos,
      tipoComida: this.tipoComida,
      fecha: this.fecha!
    };

    this.diaService.agregarComidaIngerida(comida).subscribe({
      next: res => {
        alert(res.mensaje);
        this.alimentoSeleccionado = undefined;
        this.filtro = '';
        this.alimentos = [];
        this.comidaAgregada.emit(); // notifica al padre

        //se llama a comprobar si gano algun logro dentro de este metodo asyncrono, porque si lo ponemos afuera, se podria llegar
        //a ejecutar antes de que se registre la comida, y asi, no comprobar el logro correctamente
        this.notificacionLogroService.obtenerUltimoLogroYmostrarNotificacion();
      },
      error: err => console.error(err)
    });
  }

  cancelar() {
    this.alimentoSeleccionado = undefined;
    this.filtro = '';
    this.alimentos = [];
  }

  abrirModal() {
  this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.alimentoSeleccionado = undefined;
    this.filtro = "";
    this.alimentos = [];
  }
}
