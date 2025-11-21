import { Component, computed, effect, inject, input, linkedSignal, signal } from '@angular/core';
import { AuthService } from '../../../service/auth';
import { SolicitudService } from '../../../service/solicitud';
import { Solicitud } from '../../../models/solicitud';
import { toSignal } from '@angular/core/rxjs-interop';
import { Authority } from '../../../models/authority';
import { identifierName } from '@angular/compiler';

@Component({
  selector: 'listar-solicitudes',
  imports: [],
  templateUrl: './listar-solicitudes.html',
  styleUrl: './listar-solicitudes.css',
})
export class ListarSolicitudes {

  //SERVICES
  private readonly authService = inject(AuthService);
  private readonly solicitudService = inject(SolicitudService);

  //SOLICITUDES LIST
  protected readonly solicitudesList = signal<Solicitud[]>([]);

  //ROLE
  private readonly authority = toSignal(this.authService.getRole());
  protected readonly role = computed(() => this.authority()?.authority);

  //INPUTS (para saber ver si se carga la lista de solicitudes de una forma u otra)
  readonly systemSolicitudes = input<boolean>();  //input para saber si se listan todas las solicitudes del sistema
  readonly mineSolicitudes = input<boolean>(true);  //input para saber si se listan las solicitudes que ingreso el usuario


  //SIGNAL "MODE" (definimos esta signal para que sea en el effect que se lanzen las peticiones en el 
  //backend y no se lanzen cada vez que se cambia el estado de algunas de estas señales, que era lo
  //que pasaba antes al tener todas las comprobaciones y peticiones en el effect)
  private readonly mode = computed(() => {

    //si se quiere listar todas las solicitudes del sistema y es admin
    if (this.systemSolicitudes() && this.role() == "ROLE_ADMIN") {
      return "ALL"

      //sino, si se quiere listar las solicitudes del usuario logeado  
    } else if (this.mineSolicitudes()) {
      return "MINE"

    }

    return "NONE";
  })


  constructor() {


    effect(() => {

      //si el modo es de listar todas las solicitudes
      if (this.mode() == "ALL") {

        this.solicitudService.getAll().subscribe({
          next: (s) => { this.solicitudesList.set(s), console.log(s) },
          error: (e) => alert(e)
        });

        //sino, si el modo es para listar solo las mias 
      } else if (this.mode() == "MINE") {

        this.solicitudService.getMine().subscribe({
          next: (s) => this.solicitudesList.set(s),
          error: (e) => alert(e)
        })

      }

    })
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

