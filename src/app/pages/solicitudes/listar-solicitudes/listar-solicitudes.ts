import { Component, computed, effect, inject, input, linkedSignal, signal } from '@angular/core';
import { AuthService } from '../../../service/auth';
import { SolicitudService } from '../../../service/solicitud';
import { Solicitud } from '../../../models/solicitud';
import { toSignal } from '@angular/core/rxjs-interop';
import { Authority } from '../../../models/authority';
import { identifierName } from '@angular/compiler';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { signalUpdateFn } from '@angular/core/primitives/signals';
import { map } from 'rxjs';
import { AgregarSolicitud } from '../agregar-solicitud/agregar-solicitud';
import { Loading } from '../../../visualComponents/loading/loading';

@Component({
  selector: 'listar-solicitudes',
  imports: [ReactiveFormsModule, AgregarSolicitud, Loading],
  templateUrl: './listar-solicitudes.html',
  styleUrl: './listar-solicitudes.css',
})
export class ListarSolicitudes {

  /*
 * Componente: ListarSolicitudes
 * ------------------------------------------------------------
 * Maneja la visualización, expansión, edición, aceptación,
 * rechazo y eliminación de solicitudes de alimentos.
 *
 *
 * ============================
 * 1. RECEPCIÓN DEL "MODE"
 * ============================
 * El componente recibe un parámetro de ruta "mode":
 *   - "system" → lista todas las solicitudes.
 *   - "mine" o null → lista solo las del usuario logueado.
 *
 * Se transforma en una signal con toSignal(), permitiendo que la UI
 * reaccione automáticamente si la ruta cambia.
 *
 *
 * ============================
 * 2. CARGA DE SOLICITUDES
 * ============================
 * Un effect() escucha cambios en mode().
 *   - "system": solicitudService.getAll()
 *   - "mine"/null: solicitudService.getMine()
 *
 * Cuando llegan los datos:
 *   • Se setea solicitudesList
 *   • Se generan los formularios con buildFormsFromSolicitudes()
 *
 * Si ocurre un error se limpia la lista para evitar inconsistencias.
 *
 *
 * ============================
 * 3. GENERACIÓN DE FORMULARIOS
 * ============================
 * Cada solicitud tiene un FormGroup asociado.  
 * buildFormsFromSolicitudes():
 *   - Mapea Solicitud[] → FormGroup[]
 *   - Setea formList
 *
 * Esto permite editar cada tarjeta sin mezclar estados.
 *
 *
 * ============================
 * 4. EXPANSIÓN DE TARJETAS
 * ============================
 * expandedIndex controla qué tarjeta está abierta.
 *
 * toggleExpand(index):
 *   - Abre si ninguna está abierta
 *   - Cierra la anterior y abre otra si se cambia
 *   - Cierra si se hace click en la misma
 *
 *
 * ============================
 * 5. LOADING GLOBAL
 * ============================
 * isLoading bloquea la UI durante operaciones como aceptar, editar
 * o eliminar. Al terminar cualquier petición se vuelve a false.
 *
 *
 * ============================
 * 6. BOTONES Y ACCIONES
 * ============================
 * - handleAccept(index)
 *      Acepta la solicitud; si el formulario fue modificado,
 *      primero se edita y luego se acepta.
 *
 * - handleDeny(index)
 *      Rechaza y elimina una solicitud del sistema.
 *
 * - handleEdit(index)
 *      Edita una solicitud propia y actualiza el formulario
 *      con patchValue().
 *
 * - handleDelete(index)
 *      Elimina una solicitud del usuario.
 *
 *
 * ============================
 * 7. POP-UP "AGREGAR SOLICITUD"
 * ============================
 * isAdding controla si el modal está visible.
 *
 * addSolicitud():
 *    • Inserta la nueva solicitud
 *    • Regenera todos los formularios
 *
 * Garantiza sincronización total entre UI y datos.
  */


  //SERVICES
  private readonly authService = inject(AuthService);
  private readonly solicitudService = inject(SolicitudService);

  //FORM BUILDER
  private readonly fb = inject(FormBuilder);

  //SOLICITUDES LIST y FORM LIST (las dos listas estan vinculadas asi cada solicitud tiene su formulario)
  protected readonly solicitudesList = signal<Solicitud[]>([]);
  protected readonly formList = signal<FormGroup[]>([]);

  //SEÑAL PARA COMPROBAR SI UNA SOLICITUD ESTA CARGANDO Y BLOQUERA EL UI
  protected readonly isLoading = signal<boolean>(false);

  //ROLE (signal que depende de la signal que se setea cuando el usuario se logea o se deslogea)
  protected readonly role = computed(() => this.authService.roleSignal());

  //ACTIVATED ROUTE
  private readonly route = inject(ActivatedRoute);

  //SIGNAL "MODE" (señal para obener el modo en el que se quiere listar las solcitudes. Se pasa un string
  //en la ruta que puede ser "system", "mine" o null si no pasa nada. Este modo se pasa dependiendo de
  //que boton toque el usuario)
  protected readonly mode = toSignal(
  this.route.paramMap.pipe(
    map(params => params.get("mode"))
  ),
  { initialValue: this.route.snapshot.paramMap.get("mode") }
);


  constructor() {
    effect(() => {

      //si el modo es de listar todas las solicitudes del sistema
      if (this.mode() == "system") {

        this.solicitudService.getAll().subscribe({
          //cuando llegue la lista se la seteamos a la que maneja el html y contruimos un lista de
          //formularios con el mismo indice que la lista de solicitudes y con la info de cada solicitud patcheada
          next: (s) => {this.solicitudesList.set(s), this.buildFormsFromSolicitudes(s)},
          
          //si llegua un error significa que no hay solicitudes cargadas, entonces se vuelve a setear
          //la lista vacia porque puede lleguar a ver fugas de memoria en la lista ya que el compoenente
          //no se resetea y por lo tanto la lista tampoco, entonces puede lleguar a quedar cargada igual aunque no lo este
          error: (e) => {alert(e.message), this.solicitudesList.set([])}  
        });

        

        //sino, si el modo es para listar solo las mias o no se pasa un modo en la ruta listamos solo las del usuario logeado
      } else if (this.mode() == "mine" || this.mode() == null) {
          //cuando llegue la lista se la seteamos a la que maneja el html y contruimos un lista de
          //formularios con el mismo indice que la lista de solicitudes y con la info de cada solicitud patcheada
        this.solicitudService.getMine().subscribe({
          
          next: (s) => {this.solicitudesList.set(s), this.buildFormsFromSolicitudes(s)},
          
          //si llegua un error significa que no hay solicitudes cargadas, entonces se vuelve a setear
          //la lista vacia porque puede lleguar a ver fugas de memoria en la lista ya que el compoenente
          //no se resetea y por lo tanto la lista tampoco, entonces puede lleguar a quedar cargada igual aunque no lo este
          error: (e) => {alert(e.message), this.solicitudesList.set([])}
        })

      }
    })
  }
  

  //metodo para setear nuestra lista de formularios que maneja el html, mapeando una lista de solicitudes
  //asi convertimos esa lista en una lista de formularios con los indices de las dos listas enlazados
  //y con toda la info de cada solicitud patcheada o mapeada
  private buildFormsFromSolicitudes (solicitudes: Solicitud[]){
    
    //mapeamos la lista de solicitudes a una de formularios con los campos ya cargagados de los 
    //atributos de cada solicitud para obtener su info (es como el patchValue pero asi lo hacemos mas directo)
    const mapedForms = solicitudes.map((solicitud) => 
      this.fb.nonNullable.group({
        nombreComida: [solicitud.nombreComida, [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$')]], 
        porcion: [solicitud.porcion, [Validators.required, Validators.min(0)]], 
        calorias: [solicitud.calorias, [Validators.required, Validators.min(0)]], 
        proteinas: [solicitud.proteinas, [Validators.required, Validators.min(0)]], 
        carbohidratos: [solicitud.carbohidratos, [Validators.required, Validators.min(0)]], 
        grasas: [solicitud.grasas, [Validators.required, Validators.min(0)]]
      })
    ) 

    //le seteamos la lista de formularios con los campos de las solicitudes a nuestro signal
    //que tiene la lista de solicitudes que va a manejar el html
    this.formList.set(mapedForms);
    
  }
  
  //getters para las comprobaciones y mensajes de error en el fomulario
  get nombreComida (){
    return (index: number) => (this.formList()[index].controls["nombreComida"]);
  };

  get porcion (){
    return (index: number) => (this.formList()[index].controls["porcion"]);
  };

  get calorias (){
    return (index: number) => (this.formList()[index].controls["calorias"]);
  };

  get proteinas (){
    return (index: number) => (this.formList()[index].controls["proteinas"]);
  };

  get carbohidratos (){
    return (index: number) => (this.formList()[index].controls["carbohidratos"]);
  };

  get grasas (){
    return (index: number) => (this.formList()[index].controls["grasas"]);
  };



  //HANDLERS de los botones
  handleAccept(index: number){
    
    if(confirm("Seguro que desea aceptar la solicitud? ")){

      //seteamos isLoading para que se bloquee la UI y que espere a que se complete la peticion o retorne un error
      this.isLoading.set(true);

      //si el formulario no fue modificado
      if(this.formList()[index].pristine){
  
        //le pasamos el id de la solcitud que tiene el indice que se nos paso
        this.solicitudService.accept(this.solicitudesList()[index].id!).subscribe({
  
          next: (s) => {
            console.log(s); 
            //cerramos el expandible porque sino queda abierta la tarjeta que tiene el indice de la anterior
            this.toggleExpand(index),
            //elminamos uno elemento desde la posicion del index en las dos listas
            this.solicitudesList().splice(index, 1), this.formList().splice(index, 1);
            //debloquemas la UI cuando el back retorne algo
            this.isLoading.set(false);
          },
          error: (e) => {alert("Hubo un error: " + e.message), this.isLoading.set(false)}
        })
      }else{

        //si el formulario fue modificado modificamos y aceptamos
        this.solicitudService.modifyAndAccept(this.solicitudesList()[index].nombreComida, this.formList()[index].getRawValue()).subscribe({
          
          next: (s) => {
            console.log(s); 
            //cerramos el expandible porque sino queda abierta la tarjeta que tiene el indice de la anterior
            this.toggleExpand(index),
            //elminamos uno elemento desde la posicion del index en las dos listas
            this.solicitudesList().splice(index, 1), this.formList().splice(index, 1); 
            //debloquemas la UI cuando el back retorne algo
            this.isLoading.set(false);
          },
          error: (e) => {alert("Hubo un error" + e.message), this.isLoading.set(false)}
        })
      }
      
    }

  }

  handleDeny(index: number){
    if(confirm("Seguro que desea rechazar la solicitud: " + this.solicitudesList()[index].nombreComida+ "?")){

      //si confirma bloqueamos la UI
      this.isLoading.set(true);

      this.solicitudService.decline(this.solicitudesList()[index].id!).subscribe({
        next: (s) => {
          //cerramos el expandible porque sino queda abierta la tarjeta que tiene el indice de la anterior
          this.toggleExpand(index),
          //elminamos uno elemento desde la posicion del index en las dos listas
          this.solicitudesList().splice(index, 1), this.formList().splice(index, 1); 
          //debloquemas la UI cuando el back retorne algo
          this.isLoading.set(false);
        },
        error: (e) => {alert("Hubo un error: " + e.message), this.isLoading.set(false)}
      })
    }
  }

  handleEdit(index: number){
    if(confirm("Seguro que desea editar su solicitud: " + this.solicitudesList()[index].nombreComida)){

      //si confirma bloqueamos la UI
      this.isLoading.set(true);

      this.solicitudService.modifydMine(this.solicitudesList()[index].nombreComida, this.formList()[index].getRawValue()).subscribe({
        next: (s) => {
          //patcheamos los campos con el objeto editado que devolvio el back
          this.formList()[index].patchValue(s);
          //actualizamos los campos de la solicitud en la lista asi se modifica el nombre en tiempo real
          this.solicitudesList()[index] = s;
          //desbloqueamos la UI
          this.isLoading.set(false);
        },
        error: (e) => {alert(e.message), this.isLoading.set(false)}
      })
    }
  }

  handleDelete(index: number){
    if(confirm("Seguro que desea eliminar su solicitud: " + this.solicitudesList()[index].nombreComida)){

      //si confirma bloqueamos la UI
      this.isLoading.set(true);

      this.solicitudService.deleteMine(this.solicitudesList()[index].nombreComida).subscribe({
        next: (s) => {
          //cerramos el expandible porque sino queda abierta la tarjeta que tiene el indice de la anterior
          this.toggleExpand(index),
          //elminamos uno elemento desde la posicion del index en las dos listas
          this.solicitudesList().splice(index, 1), this.formList().splice(index, 1); 
          //debloquemas la UI cuando el back retorne algo
          this.isLoading.set(false);
        },
        error: (e) => {alert(e.message), this.isLoading.set(false)}
      })
    }
  }




  //logica para popear el componente de agregar una solicitud
  protected readonly isAdding = signal<boolean>(false);

  openAdding(){
    this.isAdding.set(true)
  }

  closeAdding(b: boolean){
    this.isAdding.set(b)
  }

  addSolicitud(solicitud: Solicitud){
    //agregamos la nueva solicitud
    this.solicitudesList().push(solicitud);
    //volvemos a consruir los formularios a partir de la lista de solicitudes
    this.buildFormsFromSolicitudes(this.solicitudesList());
  }



  //logica para hacer las solicitudes expandibles
  protected expandedIndex: number | null = null;

  toggleExpand(index: number) {

    //variable para guardar en memoria cual esta abierto
    let expandedIndexMemori: number | null = null;

    //si en el incideDeExpansion no hay nada (significa que esta cerrada la tarjeta)
    if (this.expandedIndex == null) {
      //le seteamos el inidice de expansion para que expanda la tarjeta con ese indice
      this.expandedIndex = index;

      //si inidice que se pasa por parametro es distinto al indice de la tarjeta que esta abierta
      //significa que se abrio otra tarjeta
    } else if (index != this.expandedIndex) {

      //abrimos la otra tarjeta
      this.expandedIndex = index;

    } else {
      //sino signica que queremos cerrar la tarjeta
      this.expandedIndex = null;
    }
  }

  
  
  
  //LOGICA PARA DEJAR ABIRIR TODAS LAS TARJETAS QUE QUIERA SIN IR CERRANDO LA QUE ESTABA ABIERTA
  //(HACE FALTA CAMBIAR LA LOGICA DEL HTML PARA QUE SE MUESTRE EL DESPLEGABLE:
  //<form class="contSolicitudForm" [class.contSolicitudFormTransition]="expandedIndexes.has(indice)">)

  // expandedIndexes: Set<number> = new Set();

  //     toggleExpand(index: number) {
  // if (this.expandedIndexes.has(index)) {
  //   // Si ya está abierta → cerrarla
  //   this.expandedIndexes.delete(index);
  // } else {
  //   // Si está cerrada → abrirla
  //   this.expandedIndexes.add(index);
  // }
}

