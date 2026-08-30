import { Component, computed, effect, inject, signal } from '@angular/core';
import { ProfileService } from '../../service/profile';
import { toSignal } from '@angular/core/rxjs-interop';
import { EmailValidator, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { from } from 'rxjs';
import { Genero, Person } from '../../models/person';
import { NivelActividadFisica, NutritionalProfile, ObjetivoCaloricoTipo } from '../../models/nutritional-profile';
import { UserService } from '../../service/user';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth';
import { DialogService } from '../../service/dialog';
import { LogroService } from '../../service/logro';
import { LogroHistorial } from '../../models/logro-historial';

@Component({
  selector: 'app-mi-perfil',
  imports: [ReactiveFormsModule],
  templateUrl: './mi-perfil-componente.html',
  styleUrl: './mi-perfil-componente.css',
})
export class MiPerfilComponente {

  //perfil
  private readonly profileService = inject(ProfileService);
  private readonly userService = inject(UserService);
  private readonly authService = inject(AuthService)
  private readonly router = inject(Router);
  private readonly dialog = inject(DialogService);
  private readonly personProfile = toSignal(this.profileService.getPersonProfile());
  private readonly nutritionalProfilePerson = toSignal(this.profileService.getNutritionalProfile())

  //signals para mostrar datos en tiempo real al ser actualizados
  protected readonly personProfileSignal = signal<Person | undefined>(undefined);
  protected readonly nutritionalProfileSignal = signal<NutritionalProfile | undefined>(undefined);
  
  //logros
  private readonly logroService = inject(LogroService);
  protected readonly historialLogros = toSignal(this.logroService.getAll());

  private readonly tiposLogro = [
    "META_CALORICA_DIARIA", "LOGIN", "HIDRATACION_DIARIA"
  ]

  //resumen que devuelve una lista con el nivel de cada logro y las veces que se gano,
  //necesita computed() ya que se usa historialLogros() que es una signal
  protected readonly logrosResumen = computed(() => {
    
    //si no hay nigun logro retornamo una lista vacia
    if(!this.historialLogros()){
      return [];
    }

    //lista para despues retornarla
    const logrosList: Array<{
      logro_id: string,
      cantGanado: number,
      nivel: string
    }> = [];

    //recorremos los tipos de logro{
    this.tiposLogro.forEach(tipo => {
      //variable contadora de veces ganado el tipo de logro
      let vecesGanado = 0;

      //por cada tipo de logro recorremos el historial y nos fijamos si coicide el tipo
      this.historialLogros()?.forEach(logro => {
        if(tipo == logro.logro_id){
          vecesGanado++;
        }
      })
      
      //solo agregamos los logros que el usuario obtuvo al menos una vez.
      if(vecesGanado > 0){
        //le insertamos un objeto a la lista para retornas despues llamadno al metodo obtenerNivelLogro
        logrosList.push({
          logro_id: tipo,
          cantGanado: vecesGanado,
          nivel: this.obtenerNivelLogro(vecesGanado)
        })
      }
    })

    //retornamos la lista
    return logrosList;
  });

  private obtenerNivelLogro(vecesGanado: number){
    if (vecesGanado >= 30) {
      return 'level 3';
    }

    if (vecesGanado >= 7) {
      return 'level 2';
    }

    return 'level 1';
  }



  protected readonly personForm = new FormBuilder().nonNullable.group({
    nombre: ["", [Validators.required]],
    apellido: ["", [Validators.required]],
    dni: ["", [Validators.required, Validators.minLength(8), Validators.maxLength(8)]],
    fechaNacimiento: ["", [Validators.required]],
    telefono: ["", [Validators.required, Validators.minLength(10)]],
    direccion: ["", [Validators.required]],
    genero: ["MASCULINO" as Genero, [Validators.required]],
    email: ["", [Validators.required, Validators.email]],
  })


  protected readonly nutritionalProfileForm = new FormBuilder().nonNullable.group({
    peso: [0, [Validators.required, Validators.min(1)]],
    altura: [0, [Validators.required, Validators.min(1)]],
    nivelActividadFisica: ["SEDENTARIO" as NivelActividadFisica, [Validators.required]],
    objetivoCaloricoTipo: ["MANTENIMIENTO" as ObjetivoCaloricoTipo, [Validators.required]],
    edad: [0, [Validators.required, Validators.min(1)]],
    geb: [0, [Validators.required]],
    objetivoDiario: [0, [Validators.required]],
  })


  protected readonly tiposNivelActividadFisica = ["SEDENTARIO" , "LIGERA" , "MODERADA" , "INTENSA" , "MUY_INTENSA"];
  protected readonly tiposObjetivoCaloricoTipo = ["MANTENIMIENTO" , "DEFICIT_LIGERO" , "DEFICIT_MODERADO" , "SUPERAVIT_LIGERO" , "SUPERAVIT_MODERADO"];


  constructor(){
    effect(() => {
      //incializamos el formulario con los datos que nos llega
      this.personForm.patchValue(this.personProfile()!)
      
      //incializamos el formulario con los datos que nos llega
      this.nutritionalProfileForm.patchValue(this.nutritionalProfilePerson()!)

      //seteamos los valores a las signals que son para mostrar dinamicamente
      this.personProfileSignal.set(this.personProfile())
      this.nutritionalProfileSignal.set(this.nutritionalProfilePerson())
    })
  }



  async handleActualizar(){
    const confirmado = await this.dialog.confirm("Desea actualizar los datos? ");
    if (confirmado){
      if(this.personForm.valid && this.nutritionalProfileForm.valid){

        //actualizamos la persona
        this.profileService.updatePersonProfile(this.personForm.getRawValue()).subscribe({
          next: (p) => {
            //patcheamos el formulario con lo que nos respondio el back
            this.personForm.patchValue(p)
            
            //actualizamos la signal que es para mostrar los datos dinamicamente
            this.personProfileSignal.set(p); 

            //actualizamos el perfil nutricional adentro del suscribe para que se ejecute esta peticion una vez que el back
            //responde para no generar una inconsistencia, que es lo que pasaba antes
            this.profileService.updateNutritionalProfile(this.nutritionalProfileForm.getRawValue()!).subscribe({
              next: (n) => {
                //patcheamos el formulario con lo que nos respondio el back
                this.nutritionalProfileForm.patchValue(n)

                //actualizamos la signal que es para mostrar los datos dinamicamente
                this.nutritionalProfileSignal.set(n);
              },
              error: (e) => alert(e)
            })
          },
          error: (e) => alert(e)
        })
      }
    }
  }

  async handleBorrarCuenta(){
    const confirmado = await this.dialog.confirm("Seguro que desea eliminar su cuenta?", { danger: true });
    if(confirmado){
      this.userService.deleteAccount().subscribe({
        next: (r) => {this.dialog.success(r), this.authService.clearToken(), this.router.navigateByUrl("/home")},
        error: (e) => this.dialog.error("Hubo un error")
      })
    }
  }


  get nombre (){
    return this.personForm.controls.nombre;
  }

  get apellido (){
    return this.personForm.controls.apellido;
  }

  get direccion (){
    return this.personForm.controls.direccion;
  }

  get dni (){
    return this.personForm.controls.dni;
  }

  get email (){
    return this.personForm.controls.email;
  }

  get fechaNacimiento (){
    return this.personForm.controls.fechaNacimiento;
  }

  get genero (){
    return this.personForm.controls.genero;
  }

  get telefono (){
    return this.personForm.controls.telefono;
  }


  get altura (){
    return this.nutritionalProfileForm.controls.altura;
  }

  get edad (){
    return this.nutritionalProfileForm.controls.edad;
  }

  get geb (){
    return this.nutritionalProfileForm.controls.geb;
  }

  get nivelActividadFisica (){
    return this.nutritionalProfileForm.controls.nivelActividadFisica;
  }

  get objetivoCaloricoTipo (){
    return this.nutritionalProfileForm.controls.objetivoCaloricoTipo;
  }

  get objetivoDiario (){
    return this.nutritionalProfileForm.controls.objetivoDiario;
  }

  get peso (){
    return this.nutritionalProfileForm.controls.peso;
  }

}
