import { AfterViewInit, ElementRef, inject, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { DiaDTO, DiaService } from '../../service/dia-service';
import { ComidaIngeridaSalidaDTO } from '../../models/comida-ingerida-salida.dto';
import { modificarComidaIngeridaDTO } from '../../models/modificar-comida-ingerida-dto';
import { AgregarComidaComponent } from '../agregar-comida/agregar-comida';
import { ManejadorSemana } from './manejadorSemana';

import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ComidasFavoritasComponent } from '../comidas-favoritas/comidas-favoritas';
import { PaqueteService } from '../../service/paquete-service';
import { catchError, of } from 'rxjs';
import { Paquete } from '../../models/paquete';
import { AlimentoInPaquete } from '../../models/alimentoInPaquete';
import { FechaLocalService } from './FechaLocalService';
import { NgxGaugeModule } from 'ngx-gauge';

@Component({ selector: 'app-ver-dia-component', standalone: true, imports: [NgxGaugeModule ,CommonModule, FormsModule, ReactiveFormsModule, AgregarComidaComponent], templateUrl: './ver-dia-component.html', styleUrl: './ver-dia-component.css', })
export class VerDiaComponent implements OnInit, AfterViewInit {

   @ViewChild('barraCurva', { static: false }) barraCurva!: ElementRef<SVGPathElement>;
  private route = inject(ActivatedRoute);
  private diaService = inject(DiaService);
  protected manejadorSemana = inject(ManejadorSemana);
  protected manejadorFechas = inject(FechaLocalService);

  protected semana: any[] = [];
  protected dia?: DiaDTO;

  protected fecha = this.route.snapshot.paramMap.get('fecha')!;
  protected diaCargadoInicial = this.route.snapshot.paramMap.get('fecha')!; // solo referencia
  protected diaActualSeleccionado = this.route.snapshot.paramMap.get('fecha')!; // el dia que se pinta azul cuando esta seleccionado
  protected fechaReferenciaSemana = this.fecha; // fecha independiente de el dia actual para no cambiar el titulo de la fecha cuando se navega por las semanas

  longitudPath: number = 0;

 @ViewChild('arcFill', { static: false }) arcFill!: ElementRef<SVGPathElement>;
arcLength = 0;

ngAfterViewInit() {
  setTimeout(() => {
    if (!this.arcFill) return;

    this.arcLength = this.arcFill.nativeElement.getTotalLength();
  });
}

getArcDash(): string {
  const totalCalorias = this.calcularTotalCalorias() + this.dia!.caloriasRestantes;
  const caloriasConsumidas = this.calcularTotalCalorias();
  const porcentaje = caloriasConsumidas / totalCalorias;
  return `${this.arcLength * porcentaje} ${this.arcLength}`;
}

  get consumidas() {
  return this.calcularTotalCalorias();
}

get total() {
  return this.calcularTotalCalorias() + this.dia!.caloriasRestantes;
}

get porcentaje() {
  return (this.consumidas / this.total) * 100;
}

// Convierte el progreso en un stroke visible
getStrokeDash(): string {
  const total = this.calcularTotalCalorias() + this.dia!.caloriasRestantes;
  if (total === 0) return "0 300";

  const progreso = this.calcularTotalCalorias() / total;
  const largoTotal = 300; // largo del path aproximado

  return `${largoTotal * progreso} ${largoTotal}`;
}

  cargando = true;
  error?: string;
  modalTab: 'buscar' | 'favoritos' = 'buscar';
  mostrarAgregar = false;
  modalKey = 0;
  protected paquetes: Paquete[] = []
  tipoSeleccionado: 'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK' = 'DESAYUNO';

  protected readonly types: Array<'DESAYUNO' | 'ALMUERZO' | 'CENA' | 'SNACK'> =
    ['DESAYUNO', 'ALMUERZO', 'CENA', 'SNACK'];


  @Output() cerrado = new EventEmitter<void>();
  @Output() agregado = new EventEmitter<void>();

  protected formModificarCantidad = new FormGroup(
    {
      cantidad: new FormControl<number | null>(null, [Validators.min(1), Validators.required])
    })


  constructor(
    private comidaFavoritaService: PaqueteService
  ) { }

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



    const fechaDate = this.manejadorFechas.toLocalDate(this.fechaReferenciaSemana); // crea fecha en TZ local a medianoche local
    this.semana = this.manejadorSemana.generarSemana(fechaDate);

  }

  irAlDia(fechaIso: string) {
    this.fecha = fechaIso;
    this.diaActualSeleccionado = fechaIso;  // cambiamos la fecha del dia seleccionado para que en el css se muestre azul
    this.cargarDia(fechaIso);

    // actualizás la referencia de semana para que
    // se centre alrededor del día que tocaste
    this.fechaReferenciaSemana = fechaIso;
    
    this.cargarSemana();
  }


  cambiarSemana(dias: number) {
    if (!this.fecha) return;

    const nueva = this.manejadorFechas.toLocalDate(this.fechaReferenciaSemana);
    nueva.setDate(nueva.getDate() + dias);


    const hoy = new Date(); // fecha para bloquear los dias futuros
    hoy.setHours(0, 0, 0, 0);

    const inicioNuevaSemana = this.manejadorSemana.generarSemana(nueva)[0].fecha;
    if (inicioNuevaSemana > hoy) return; // No avanzar al futuro

    this.fechaReferenciaSemana = nueva.toISOString().split("T")[0];
    this.cargarSemana();
  }


  formatearFechaCorta(fechaISO: string): string {

    const fecha = this.manejadorFechas.toLocalDate(fechaISO);

    const opciones: Intl.DateTimeFormatOptions = {
      weekday: "long",
      day: "numeric",
      month: "short",
      year: "numeric"
    };

    
    let formateada = fecha.toLocaleDateString("es-ES", opciones); // pasa la fecha a una cadena formateada de acuerdo a la region y el idioma que se pase junto a las opciones


    formateada = formateada.charAt(0).toUpperCase() + formateada.slice(1); // pone la primera letra en mayusucula y lo que resta de la cadena lo deja como esta

    // Quitar la coma
    formateada = formateada.replace(",", "");

    formateada = formateada.replace(/,/g, "");   // quita comas
    formateada = formateada.replace(/\bde\b/g, ""); // quita "de"

    const partes = formateada.split(" "); //separa la cadena en un array usando el espacio como separador ["Martes", "14", "nov", "2025"]
    partes[2] = partes[2].charAt(0).toUpperCase() + partes[2].slice(1); //hace que la primer letra del mes este en mayusucula y lo que queda de la cadena este como estaba
    formateada = partes.join(" "); //vuelve a unir el array en una sola cadena separando por espacios "Martes 14 Nov 2025"




    return formateada;
  }





  get cantidad() {
    return this.formModificarCantidad.controls['cantidad'];
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

  getTotalesPorTipo(tipo: string) {
  const comidas = this.getComidasPorTipo(tipo);

  let totalCalorias = 0;
  let totalProteinas = 0;
  let totalCarbohidratos = 0;
  let totalGrasas = 0;

  comidas.forEach(c => {
    totalCalorias += c.calorias;
    totalProteinas += c.proteinas;
    totalCarbohidratos += c.carbohidratos;
    totalGrasas += c.grasas;
  });

  return {
    calorias: totalCalorias,
    proteinas: totalProteinas,
    carbohidratos: totalCarbohidratos,
    grasas: totalGrasas
  };
}

getTotalesDelDia() {
  
  let totales = {
    calorias: 0,
    proteinas: 0,
    carbohidratos: 0,
    grasas: 0
  };

  this.types.forEach(tipo => {
    const t = this.getTotalesPorTipo(tipo);
    totales.calorias += t.calorias;
    totales.proteinas += t.proteinas;
    totales.carbohidratos += t.carbohidratos;
    totales.grasas += t.grasas;
  });

  return totales;
}


  cerrarModal() {
    this.cerrado.emit();
  }

  // === PIE CHART PARA CADA ALIMENTO === //

getTotalMacros(comida: any) {
  return comida.proteinas + comida.carbohidratos + comida.grasas;
}

getPorcentajeCarbos(comida: any) {
  const total = this.getTotalMacros(comida);
  if (total === 0) return 0;
  return (comida.carbohidratos / total) * 100;
}

getPorcentajeProte(comida: any) {
  const total = this.getTotalMacros(comida);
  if (total === 0) return 0;
  return (comida.proteinas / total) * 100;
}

getPorcentajeGrasas(comida: any) {
  const total = this.getTotalMacros(comida);
  if (total === 0) return 0;
  return (comida.grasas / total) * 100;
}

}


