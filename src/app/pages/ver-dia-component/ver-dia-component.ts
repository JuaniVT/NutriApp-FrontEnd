import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { DiaDTO, DiaService } from '../../service/dia-service';
import { ComidaIngeridaSalidaDTO } from '../../models/comida-ingerida-salida.dto';
import { FormControl, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';
import { modificarComidaIngeridaDTO } from '../../models/modificar-comida-ingerida-dto';
import { AgregarComidaComponent } from '../agregar-comida/agregar-comida';
import { ManejadorSemana } from './manejadorSemana';

@Component({ selector: 'app-ver-dia-component', standalone: true, imports: [CommonModule, FormsModule, ReactiveFormsModule, AgregarComidaComponent], templateUrl: './ver-dia-component.html', styleUrl: './ver-dia-component.css', })
export class VerDiaComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private diaService = inject(DiaService);
  protected manejadorSemana = inject(ManejadorSemana);
  protected semana: any[] = [];

  protected dia?: DiaDTO;
  protected fecha = this.route.snapshot.paramMap.get('fecha')!;
  protected diaCargadoInicial = this.route.snapshot.paramMap.get('fecha')!; // solo referencia
  protected diaActualSeleccionado = this.route.snapshot.paramMap.get('fecha')!; // el que se pinta


  cargando = true;
  error?: string;
  mostrarAgregar = false;
  modalKey = 0;

  tipoSeleccionado: 'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK' = 'DESAYUNO';

  protected types = ['DESAYUNO', 'ALMUERZO', 'CENA', 'SNACK'];



  ngOnInit(): void {
    if (this.fecha) {

      this.cargarDia(this.fecha);
      this.cargarSemana();

    }

  }



  cargarDia(fecha: string) {
    this.diaService.verDiaCompleto(fecha).subscribe({
      next: (data) => {
        // 🔹 Inicializar el campo 'mostrar' en cada comida
        data.comidasIngeridas.forEach(c => c.mostrar = false);

        this.dia = data;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se encontró el día seleccionado.';
        this.cargando = false;
      }
    });
  }

  cargarSemana() {

    if (!this.fecha) return;

    const fechaDate = new Date(this.fecha);
    this.semana = this.manejadorSemana.generarSemana(fechaDate);

  }

  irAlDia(fechaIso: string) {
    this.fecha = fechaIso;
    this.diaActualSeleccionado = fechaIso;
    this.cargarDia(fechaIso);
    this.cargarSemana();
  }


  cambiarSemana(dias: number) {
    if (!this.fecha) return;

    const nueva = new Date(this.fecha);
    nueva.setDate(nueva.getDate() + dias);

    // Bloquear semana futura
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    const inicioNuevaSemana = this.manejadorSemana.generarSemana(nueva)[0].fecha;
    if (inicioNuevaSemana > hoy) return; // No avanzar al futuro

    this.fecha = nueva.toISOString().split("T")[0];
    this.cargarSemana();
  }


  formatearFecha(fecha: Date): string {
  const opciones: Intl.DateTimeFormatOptions = {
    day: 'numeric',
    month: 'long'
  };
  return fecha.toLocaleDateString('es-ES', opciones);
}



  protected formModificarCantidad = new FormGroup(
    {
      cantidad: new FormControl<number | null>(null, [Validators.min(1), Validators.required])
    })


  get cantidad() {
    return this.formModificarCantidad.controls['cantidad'];
  }


  // funciones relacionadas a la funcion de agregado
  abrirAgregar(tipo: 'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK') {
    this.tipoSeleccionado = tipo;
    this.mostrarAgregar = false;
    setTimeout(() => {
      this.modalKey++;        // fuerza a Angular a recrear el componente hijo
      this.mostrarAgregar = true;
    });
  }

  cerrarAgregar() {
    this.mostrarAgregar = false;
  }

  getComidasPorTipo(tipo: string) {
    return this.dia?.comidasIngeridas.filter(c => c.tipoComida === tipo) || [];
  }

  tieneComidas(tipo: string): boolean {
    return this.getComidasPorTipo(tipo).length > 0;
  }

  calcularTotal(macro: 'proteinas' | 'carbohidratos' | 'grasas'): number {
    if (!this.dia?.comidasIngeridas?.length) return 0;
    return this.dia.comidasIngeridas.reduce((total, comida) => total + (comida[macro] ?? 0), 0);
  }

  calcularTotalCalorias(): number {
    if (!this.dia?.comidasIngeridas?.length) return 0;
    return this.dia.comidasIngeridas.reduce((total, comida) => total + (comida.calorias ?? 0), 0);
  }

  calcularMacroPorcentaje(macro: 'proteinas' | 'carbohidratos' | 'grasas'): number {
    const total = this.calcularTotal(macro);

    const limites: Record<'proteinas' | 'carbohidratos' | 'grasas', number> = {
      proteinas: 150,
      carbohidratos: 300,
      grasas: 100
    };

    const porcentaje = (total / limites[macro]) * 100;
    return Math.min(Math.round(porcentaje), 100);
  }

  toggleDetalle(comida: any) {
    comida.mostrar = !comida.mostrar;
  }

  actualizarComida(comida: ComidaIngeridaSalidaDTO) {
    const dto: modificarComidaIngeridaDTO = {
      id: comida.id,
      nombre: comida.nombreComida,
      gramos: this.formModificarCantidad.value.cantidad!,
      tipoComida: comida.tipoComida,
      tipoComidaNuevo: comida.tipoComida, // o uno distinto si el usuario lo cambia
      fecha: this.fecha!, // la fecha del día que estás viendo
    };


    this.diaService.modificarComida(dto).subscribe({
      next: (resp) => {
        console.log('Comida modificada correctamente:', resp.mensaje);

        this.cargarDia(this.fecha!);
      },
      error: (err) => {
        console.error('Error al modificar la comida:', err);
      }
    });
  }

  onEliminarClick(event: Event, comida: any) {
    this.eliminarComida(comida);
  }

  eliminarComida(comida: ComidaIngeridaSalidaDTO) {
    this.diaService.eliminarComida(comida.id, this.fecha!, comida.tipoComida.toUpperCase()).subscribe({
      next: (res) => {
        alert(res.mensaje);
        this.cargarDia(this.fecha!); // actualiza la lista solo cuando la eliminación termine
      },
      error: (err) => console.error(err)
    });
  }

}
