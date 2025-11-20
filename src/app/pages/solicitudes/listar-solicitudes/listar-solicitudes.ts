  import { Component, computed, effect, inject, input, linkedSignal, signal } from '@angular/core';
  import { AuthService } from '../../../service/auth';
  import { SolicitudService } from '../../../service/solicitud';
  import { Solicitud } from '../../../models/solicitud';
  import { toSignal } from '@angular/core/rxjs-interop';
  import { Authority } from '../../../models/authority';

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
    readonly systemSolicitudes = input<boolean>(true);  //input para saber si se listan todas las solicitudes del sistema
    readonly mineSolicitudes = input<boolean>();  //input para saber si se listan las solicitudes que ingreso el usuario


    //SIGNAL "MODE" (definimos esta signal para que sea en el effect que se lanzen las peticiones en el 
    //backend y no se lanzen cada vez que se cambia el estado de algunas de estas señales, que era lo
    //que pasaba antes al tener todas las comprobaciones y peticiones en el effect)
    private readonly mode = computed(() => {

      //si se quiere listar todas las solicitudes del sistema y es admin
      if(this.systemSolicitudes() && this.role() == "ROLE_ADMIN"){
        return "ALL"

        //sino, si se quiere listar las solicitudes del usuario logeado  
      }else if(this.mineSolicitudes()){
        return "MINE"

      }
      
      return "NONE";
    })


    constructor(){
      

      effect(() => {
        
        //si el modo es de listar todas las solicitudes
        if(this.mode() == "ALL"){

          this.solicitudService.getAll().subscribe({
            next: (s) => {this.solicitudesList.set(s), console.log(s)},
            error: (e) => alert(e) 
          });

        //sino, si el modo es para listar solo las mias 
        }else if(this.mode() == "MINE"){

          this.solicitudService.getMine().subscribe({
            next: (s) => this.solicitudesList.set(s),
            error: (e) => alert(e)
          })

        }

      })
    }
  }
