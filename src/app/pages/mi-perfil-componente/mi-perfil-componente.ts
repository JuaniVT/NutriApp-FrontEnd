import { Component, effect, inject } from '@angular/core';
import { ProfileService } from '../../service/profile';
import { toSignal } from '@angular/core/rxjs-interop';
import { EmailValidator, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { from } from 'rxjs';
import { Genero } from '../../models/person';
import { NivelActividadFisica, ObjetivoCaloricoTipo } from '../../models/nutritional-profile';
import { UserService } from '../../service/user';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth';

@Component({
  selector: 'app-mi-perfil',
  imports: [ReactiveFormsModule],
  templateUrl: './mi-perfil-componente.html',
  styleUrl: './mi-perfil-componente.css',
})
export class MiPerfilComponente {

  private readonly profileService = inject(ProfileService);
  private readonly userService = inject(UserService);
  private readonly authService = inject(AuthService)
  private readonly router = inject(Router);
  protected readonly personProfile = toSignal(this.profileService.getPersonProfile());
  protected readonly nutritionalProfilePerson = toSignal(this.profileService.getNutritionalProfile())
  


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
      
      this.nutritionalProfileForm.patchValue(this.nutritionalProfilePerson()!)
    })
  }





  handleActualizar(){
    if (confirm("Desea actualizar los datos? ")){
      if(this.personForm.valid && this.nutritionalProfileForm.valid){
        
        //actualizamos la persona
        this.profileService.updatePersonProfile(this.personForm.getRawValue()).subscribe({
          next: (p) => alert(p),
          error: (e) => alert(e)
        })

        //actualizamos el perfil nutricional
        this.profileService.updateNutritionalProfile(this.nutritionalProfileForm.getRawValue()!).subscribe({
          next: (n) => alert(n),
          error: (e) => alert(e)
        })
      }
    }
  }

  handleBorrarCuenta(){
    if(confirm("Seguro que desea eliminar su cuenta?")){
      this.userService.deleteAccount().subscribe({
        next: (r) => {alert(r), this.authService.clearToken(), this.router.navigateByUrl("/home")},
        error: (e) => alert("Hubo un error")
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
