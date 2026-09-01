import { AfterViewInit, ElementRef, inject, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { HidratacionComponent } from '../hidratacion-component/hidratacion-component';
import { DiaDTO, DiaService } from '../../service/dia-service';
import { ComidaIngeridaSalidaDTO } from '../../models/comida-ingerida-salida.dto';
import { modificarComidaIngeridaDTO } from '../../models/modificar-comida-ingerida-dto';
import { AgregarComidaComponent } from '../agregar-comida/agregar-comida';
import { ManejadorSemana } from './manejadorSemana';

import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ComidasFavoritasComponent } from '../comidas-favoritas/comidas-favoritas';
import { PaqueteService } from '../../service/paquete-service';
import { catchError, forkJoin, of } from 'rxjs';
import { Paquete } from '../../models/paquete';
import { AlimentoInPaquete } from '../../models/alimentoInPaquete';
import { FechaLocalService } from '../../service/FechaLocalService';
import { DialogService } from '../../service/dialog';
import { ProfileService } from '../../service/profile';
import { NgxGaugeModule } from 'ngx-gauge';
import { ArcElement, Chart, ChartDataset, registerables, Tooltip } from 'chart.js';
import { NotificacionLogro } from '../../service/notificacion-logro';
import { LogroService } from '../../service/logro';
import { EstadoDiaDTO } from '../../models/estado-dia.dto';
Chart.register(...registerables);

Chart.register(ArcElement, Tooltip);

@Component({
  selector: 'app-ver-dia-component', standalone: true, imports: [NgxGaugeModule, CommonModule, FormsModule, ReactiveFormsModule, AgregarComidaComponent, HidratacionComponent],
  templateUrl: './ver-dia-component.html', styleUrl: './ver-dia-component.css',
})
export class VerDiaComponent implements OnInit, AfterViewInit {

  @ViewChild('arcFill', { static: false }) arcFill!: ElementRef<SVGPathElement>;
  arcLength = 0;
  @ViewChild('miChart', { static: false }) miChart!: ElementRef<HTMLCanvasElement>;
  // En tu componente padre
  @ViewChild(HidratacionComponent, { static: false }) hidratacionHijo!: HidratacionComponent;
  progressChart?: Chart;
  private route = inject(ActivatedRoute);
  private diaService = inject(DiaService);
  private profileService = inject(ProfileService);
  private readonly notificacionLogroService = inject(NotificacionLogro);
  protected manejadorSemana = inject(ManejadorSemana);
  protected manejadorFechas = inject(FechaLocalService);
  private dialog = inject(DialogService);

  protected semana: any[] = [];
  protected dia?: DiaDTO;
  protected estadosDias: EstadoDiaDTO[] = [];
  private semanaRequestId = 0; // identificador de request para cada carga de semana

  protected fecha = this.route.snapshot.paramMap.get('fecha')!;
  protected diaCargadoInicial = this.route.snapshot.paramMap.get('fecha')!; // solo referencia
  protected diaActualSeleccionado = this.route.snapshot.paramMap.get('fecha')!; // el dia que se pinta azul cuando esta seleccionado
  protected fechaReferenciaSemana = this.fecha; // fecha independiente de el dia actual para no cambiar el titulo de la fecha cuando se navega por las semanas

  longitudPath: number = 0;
  totalEnPadre: number = 0;

  /** Objetivo calórico diario del perfil nutricional del usuario. Se usa como
      meta cuando el día todavía no tiene ingestas: el back sólo completa
      `caloriasRestantes` tras la primera comida, así que sin esto se vería
      "0 / 0" al abrir un día vacío. */
  protected objetivoDiario = 0;

  handleTotalRecibido(total: number) {
    this.totalEnPadre = total;
  }

  ngAfterViewInit() {
    setTimeout(() => {
      // 1. Verificación de seguridad del elemento DOM
      if (!this.arcFill) {
        console.warn('arcFill no está disponible aún');
        return;
      }

      // 2. Ejecución de lógica propia
      this.arcLength = this.arcFill.nativeElement.getTotalLength();
      this.actualizarProgreso();

      // 3. Ejecución segura del componente hijo
      if (this.hidratacionHijo) {
        console.log('Llamando a obtenerTotal con fecha:', this.fecha);
        this.hidratacionHijo.obtenerTotal(this.fecha);
      } else {
        console.error('El componente hidratacionHijo no ha sido encontrado en el ViewChild');
      }
    });
  }

  renderProgressChart() {
    if (!this.miChart || !this.dia) return;

    const total = this.calcularTotalCalorias() + this.dia.caloriasRestantes;
    const consumidas = this.calcularTotalCalorias();

    // Si ya existe, destruimos el chart anterior
    if (this.progressChart) {
      this.progressChart.destroy();
    }

    this.progressChart = new Chart(this.miChart.nativeElement, {
      type: 'doughnut',
      data: {
        labels: ['Consumidas', 'Restantes'],
        datasets: [{
          data: [consumidas, this.dia.caloriasRestantes],
          backgroundColor: ['#ff6f61', '#ffe066'], // colores cálidos
          borderWidth: 0
        }]
      },
      options: {
        cutout: '75%',
        responsive: true,
        animation: {
          animateRotate: true,
          duration: 1000
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: function (context) {
                return `${context.label}: ${context.raw} kcal`;
              }
            }
          },
          legend: { display: false }
        }
      }
    });
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

  /** Meta de calorías del día para la barra de progreso.
      Con ingestas cargadas: consumidas + caloriasRestantes (incluye ajuste por
      actividad). Día vacío (todavía "0 / 0" desde el back): objetivo del perfil. */
  get metaCalorias(): number {
    const totalDelDia = this.calcularTotalCalorias() + (this.dia?.caloriasRestantes ?? 0);
    return totalDelDia > 0 ? totalDelDia : this.objetivoDiario;
  }

  /** Progreso de calorías consumidas sobre la meta, recortado a 0–100. */
  get porcentajeCalorias(): number {
    const meta = this.metaCalorias;
    if (meta <= 0) return 0;
    return Math.min(Math.max((this.calcularTotalCalorias() / meta) * 100, 0), 100);
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

    // Objetivo diario para la barra de progreso cuando el día está vacío.
    // Si falla queda en 0 y la barra usa el total que devuelva el back.
    this.profileService.getNutritionalProfile().subscribe({
      next: (perfil) => { this.objetivoDiario = perfil?.objetivoDiario ?? 0; },
      error: (err) => console.error('No se pudo obtener el perfil nutricional:', err)
    });
  }


  cargarDia(fecha: string) {
    this.diaService.verDiaCompleto(fecha).subscribe({
      next: (data) => {
        // Inicializamos el campo 'mostrar' en cada comida
        data.comidasIngeridas.forEach(c => c.mostrar = false);

        // Guardamos el día cargado
        this.dia = data;
        this.totalEnPadre = this.dia.hidratacion.cantidadMl;
        this.renderProgressChart();

        // La semana debe volver a leer los estados reales del backend para que el
        // día actualmente seleccionado no quede con estado en blanco aunque el día
        // ya tenga información del back.
        this.fecha = fecha;
        this.fechaReferenciaSemana = fecha;
        this.cargarSemana();

        this.cargando = false;

        // Actualizamos la barra de progreso SVG
        this.actualizarProgreso();
      },
      error: () => {
        this.error = 'No se encontró el día seleccionado.';
        this.cargando = false;
      }
    });
  }

  actualizarProgreso() {
    if (!this.arcFill || !this.dia) return;

    const total = this.calcularTotalCalorias() + this.dia.caloriasRestantes;
    if (total === 0) return;

    const consumidas = this.calcularTotalCalorias();
    const porcentaje = consumidas / total;

    const dash = this.arcLength * porcentaje;
    this.arcFill.nativeElement.setAttribute('stroke-dasharray', `${dash} ${this.arcLength}`);
  }



  cargarSemana() {
    const requestId = ++this.semanaRequestId;   // se incrementa la variable para que la request sea diferente y nueva con respecto a la anterior

    const fechaDate = this.manejadorFechas.toLocalDate(this.fechaReferenciaSemana); // crea fecha en TZ local a medianoche local
    this.semana = this.manejadorSemana.generarSemana(fechaDate);

    const primerDia = this.semana[0].fecha;  //obtenemos la fecha del primer dia de la semana 
    const ultimoDia = this.semana[6].fecha;  //obtenemos la fecha del ultimo dia de la semana 

    const añoPrimerDia = primerDia.getFullYear();       // obtenemos la fecha del año y el mes del primer dia 
    const mesPrimerDia = primerDia.getMonth() + 1;      

    const añoUltimoDia = ultimoDia.getFullYear();       // obtenemos la fecha del año y el mes del ultimo dia
    const mesUltimoDia = ultimoDia.getMonth() + 1;      // hacemos esto porque una semana puede estar entre un mes y el otro, lo mismo con el año, 
                                                        // hay que contemplar esto, porque el endpoint que devuelve los estados los devuelve del mes y año 
                                                        // que se pasen por parametro 


    const aplicarSiSigueVigente = (estados: EstadoDiaDTO[]) => {
      if (requestId !== this.semanaRequestId) return;
      this.aplicarEstadosSemana(estados);
    };

    const limpiarSiSigueVigente = () => {
      if (requestId !== this.semanaRequestId) return;       // estas funciones son para que solamente se actualicen 
       this.limpiarEstadosSemana();                         // los estados si la request que viene del back es nueva y no vieja
    };                                                      // De esa forma, la semana actual no queda pisada por una respuesta anterior.    


    //preguntamos si el primer y el último día de la semana pertenecen al mismo mes y al mismo año
    if (añoPrimerDia === añoUltimoDia && mesPrimerDia === mesUltimoDia) {

      this.diaService.obtenerEstadosDelMes(
        añoPrimerDia,                             // como la semana no cruza de mes pedimos los estados y los aplicamos 
        mesPrimerDia
      ).subscribe({
        next: (estados) => {
          aplicarSiSigueVigente(estados);     // se aplican los estados a los dias correspondientes
        },
        error: (err) => {
          console.error('Error al obtener estados de los días:', err);
          limpiarSiSigueVigente();          // si hay algun error no voy a mantener estados que podrían ser viejos o incorrectos
        }
      });

      return;
    }

    //si La semana cruza de mes:
    // obtenemos los estados de ambos meses
    this.diaService.obtenerEstadosDelMes(
      añoPrimerDia,
      mesPrimerDia
    ).subscribe({
      next: (estadosPrimerMes) => {

        this.diaService.obtenerEstadosDelMes(
          añoUltimoDia,
          mesUltimoDia
        ).subscribe({
         next: (estadosSegundoMes) => {



    const todosLosEstados = [
        ...estadosPrimerMes,             // combinamos los dos arrays de estados en uno solo llamado todosLosEstados gracias al spread operator . . . 
        ...estadosSegundoMes
    ];

            aplicarSiSigueVigente(todosLosEstados);
          },
          error: (err) => {
            console.error('Error al obtener estados del segundo mes:', err);
            limpiarSiSigueVigente();
          }
        });

      },
      error: (err) => {
        console.error('Error al obtener estados del primer mes:', err);
        limpiarSiSigueVigente();
      }
    });

  }

  

  private aplicarEstadosSemana(estados: EstadoDiaDTO[]) {
   
    this.estadosDias = estados;

    this.semana = this.semana.map(dia => {          // recorremos la semana para transformar cada elemento (dia) para que pueda tener su estado nutricional
     
      const estadoDia = estados.find(estado => estado.fecha === dia.iso);       // buscamos dentro del array el primer elemento que cumpla la condicion se busca que coincidan las fechas del dto del estado del dia y el objeto dia

      return {
        ...dia,
        estadoDia: estadoDia?.estadoDia?.trim() ?? null           
      };
    });

    // combinamos las propiedades del objeto dia y el estado, 
    // para meterle el estado nutricional del dia al dia correspondiente     
    // aca se pregunta si hay un estado usalo, si no hay se usa null y luego se devuelve el dia

  }

  

  //Si no pudimos obtener información confiable, evitamos conservar o mostrar 
  // información que podría ser vieja o incorrecta
  private limpiarEstadosSemana() {  

    this.estadosDias = [];

    this.semana = this.semana.map(dia => ({
      ...dia,
      estadoDia: null
    }));
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

    const limiteMaximoDiasAtras = 60; // 2 meses aprox

    const nueva = this.manejadorFechas.toLocalDate(this.fechaReferenciaSemana);
    nueva.setDate(nueva.getDate() + dias);


    const hoy = new Date(); // fecha para bloquear los dias futuros
    hoy.setHours(0, 0, 0, 0);

    const inicioNuevaSemana = this.manejadorSemana.generarSemana(nueva)[0].fecha;
    if (inicioNuevaSemana > hoy) return; // No avanzar al futuro

    // bloquear dos meses para atras
    const limiteAtras = this.manejadorFechas.toLocal(new Date(hoy));
    limiteAtras.setDate(hoy.getDate() - limiteMaximoDiasAtras);

    if (nueva < limiteAtras) {
      this.dialog.info("Para ver días más antiguos usá el calendario 🙂");
      return;
    }

    this.fechaReferenciaSemana = this.manejadorFechas.toIsoDate(nueva);
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

    if (comida.mostrar) {
      // creamos el gráfico cuando se abre el detalle
      setTimeout(() => this.renderChart(comida), 50);
    }
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
        this.cargarSemana();

        //se llama a comprobar si gano algun logro dentro de este metodo asyncrono, porque si lo ponemos afuera, se podria llegar
        //a ejecutar antes de que se registre la comida, y asi, no comprobar el logro correctamente
        this.notificacionLogroService.obtenerUltimoLogroYmostrarNotificacion();
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
        this.dialog.success(res.mensaje);
        this.cargarDia(this.fecha!);        // actualiza la lista solo cuando la eliminación termine
        this.cargarSemana();
      },
      error: (err) => console.error(err)
    });
  }

  cargarComidasFavoritas(): void {
    this.comidaFavoritaService.getComidasFavoritasPorUsuario()
      .pipe(
        catchError(err => {
          console.error(err);
          this.dialog.error('Error al cargar comidas favoritas');
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
          // Muestra la modal con el mensaje del backend
          this.dialog.success(res.mensaje || 'Paquete agregado correctamente.');

          this.agregado.emit();   // notifica al padre para recargar el día
          this.cargarDia(this.fecha!);
          this.cargarSemana();
          this.cerrarAgregar();

          //llamamos a comprobar si se gano algun logro y lo mostramos
          this.notificacionLogroService.obtenerUltimoLogroYmostrarNotificacion();
        },
        error: (err) => {
          const mensajeError = err.error?.error || 'No se pudo agregar el paquete.';
          this.dialog.error(mensajeError);  // muestra la modal con el error
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

  chartsMap = new Map<number, Chart>(); // para evitar gráficos duplicados


  renderChart(comida: any) {
    const canvasId = `chart-${comida.id}`;
    const canvas = document.getElementById(canvasId) as HTMLCanvasElement;

    if (!canvas) return;

    // si ya existe un gráfico para ese alimento → destruirlo
    if (this.chartsMap.has(comida.id)) {
      this.chartsMap.get(comida.id)?.destroy();
    }

    const chart = new Chart(canvas, {
      type: 'pie',
      data: {
        labels: ['Proteínas', 'Carbohidratos', 'Grasas'],
        datasets: [{
          data: [
            comida.proteinas,
            comida.carbohidratos,
            comida.grasas
          ],
          backgroundColor: [
            '#FFB74D',  // naranja premium
            '#81D4FA',  // celeste moderno
            '#FF8A80'   // coral suave
          ],
          borderWidth: 1
        }]
      },
      options: {
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              font: { size: 12 }
            }
          }
        }
      }
    });

    this.chartsMap.set(comida.id, chart);
  }

}