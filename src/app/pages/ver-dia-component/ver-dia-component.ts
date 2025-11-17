import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { DiaDTO, DiaService} from '../../service/dia-service';
import { ComidaIngeridaSalidaDTO } from '../../models/comida-ingerida-salida.dto';
import { modificarComidaIngeridaDTO } from '../../models/modificar-comida-ingerida-dto';
import { AgregarComidaComponent } from '../agregar-comida/agregar-comida';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ComidasFavoritasComponent } from '../comidas-favoritas/comidas-favoritas';
import { PaqueteService } from '../../service/paquete-service';
import { catchError, of } from 'rxjs';
import { Paquete } from '../../models/paquete';
import { AlimentoInPaquete } from '../../models/alimentoInPaquete';
@Component({ selector: 'app-ver-dia-component', standalone: true, imports: [CommonModule, FormsModule, ReactiveFormsModule, AgregarComidaComponent], templateUrl: './ver-dia-component.html', styleUrl: './ver-dia-component.css', })
export class VerDiaComponent implements OnInit {

  protected  dia?: DiaDTO;
  protected fecha?: string;
  cargando = true;
  error?: string;
  modalTab: 'buscar' | 'favoritos' = 'buscar';
  mostrarAgregar = false;
  modalKey = 0;
 protected paquetes : Paquete [] = []
tipoSeleccionado: 'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK' = 'DESAYUNO';

protected readonly types: Array<'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK'> = 
  ['DESAYUNO', 'ALMUERZO', 'CENA', 'SNACK'];
  

  @Output() cerrado = new EventEmitter<void>();
    @Output() agregado = new EventEmitter<void>();

  protected formModificarCantidad = new FormGroup(
    {
      cantidad : new FormControl<number | null>(null, [Validators.min(1), Validators.required])
    })

  get cantidad() 
  {
    return this.formModificarCantidad.controls['cantidad'];
  }

  constructor(
    private route: ActivatedRoute,
    private diaService: DiaService,
    private comidaFavoritaService : PaqueteService
  ) {}

  ngOnInit(): void {

    this.fecha = this.route.snapshot.paramMap.get('fecha') || undefined;
    if (this.fecha) {
      this.cargarDia(this.fecha);
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
// funciones relacionadas a la funcion de agregado
abrirAgregar(tipo: 'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK') {
   this.tipoSeleccionado = tipo;
  this.mostrarAgregar = false;

  this.cargarComidasFavoritas();

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
  const dto : modificarComidaIngeridaDTO = {
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

cargarComidasFavoritas(): void {
    this.comidaFavoritaService.getComidasFavoritasPorUsuario()
      .pipe(
        catchError(err => {
          console.error(err);
          alert('Error al cargar comidas favoritas');
          return of([]);
        })
      )
      .subscribe(respuesta => {
        this.paquetes = [];
        respuesta.forEach(fav => {
          let elemento = this.paquetes.find(p => p.nombrePaquete === fav.nombrePaquete);
          if (!elemento) {
            elemento = { nombrePaquete: fav.nombrePaquete, alimentos: [] };
            this.paquetes.push(elemento);
          }
          let alimento: AlimentoInPaquete = { id: fav.comidaId, nombre: fav.nombreComida, gramos: fav.cantidad };
          elemento.alimentos.push(alimento);
        });
      });
  }

  // Agregar un favorito al día
agregarFavorito(nombrePaquete: string) {
  this.comidaFavoritaService.agregarPaqueteADia(nombrePaquete, this.tipoSeleccionado, this.fecha!)
    .subscribe({
      next: (res) => {
        // Muestra un alert con el mensaje del backend
        alert(res.mensaje || 'Paquete agregado correctamente.');
        
        this.agregado.emit();   // notifica al padre para recargar el día
        this.cargarDia(this.fecha!);       
        this.cerrarAgregar();
      },
      error: (err) => {
        const mensajeError = err.error?.error || 'No se pudo agregar el paquete.';
        alert(mensajeError);  // muestra un alert con el error
        this.error = mensajeError;
        console.error(err);
      }
    });
}


  cerrarModal() {
    this.cerrado.emit();
  }


}
