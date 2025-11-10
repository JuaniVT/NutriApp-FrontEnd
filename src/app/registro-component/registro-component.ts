import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../service/auth';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registro-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './registro-component.html',
  styleUrl: './registro-component.css',
})
export class RegistroComponent {

  protected registroForm = new FormGroup({
  usuario: new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
      Validators.maxLength(20), // coincide con el mensaje del HTML
      Validators.pattern('^[a-zA-Z0-9._-]+$') // permite guion medio también
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};:\\"\\\\|,.<>/?]).+$')
    ])
  }),

  persona: new FormGroup({
    nombre: new FormControl('', [Validators.required, Validators.minLength(3)]),
    apellido: new FormControl('', [Validators.required, Validators.minLength(3)]),
    dni: new FormControl('', [Validators.required, Validators.pattern('^\\d{8}$')]),
    fechaNacimiento: new FormControl('', [Validators.required]),
    telefono: new FormControl('', [Validators.required, Validators.pattern('^\\d{7,15}$')]),
    direccion: new FormControl('', [Validators.required]),
    genero: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email])
  }),

  perfilNutricional: new FormGroup({
    peso: new FormControl<number | null>(null, [Validators.required, Validators.min(30), Validators.max(300)]),
    altura: new FormControl<number | null>(null, [Validators.required, Validators.min(100), Validators.max(250)]),
    edad: new FormControl<number | null>(null, [Validators.required, Validators.min(10), Validators.max(120)]),
    nivelActividadFisica: new FormControl('', [Validators.required]),
    objetivoCaloricoTipo: new FormControl('', [Validators.required])
  })
});

  protected readonly client = inject(AuthService);
  private router = inject(Router);

  onSubmit() {
  const datos = this.registroForm.value;

  this.client.register(datos).subscribe({
    next: (resp) => {
      alert('Cuenta creada correctamente');
    },
    error: (err) => {
      if (err.error && err.error.error) {
        alert(err.error.error); // muestra "El nombre de usuario ya está en uso", etc.
      } else {
        alert('Ocurrió un error al registrarse.');
      }
    }
  });
}

 // 🔹 GETTERS para acceder más fácil desde el HTML
  get usuario() {
    return this.registroForm.get('usuario') as FormGroup;
  }

  get persona() {
    return this.registroForm.get('persona') as FormGroup;
  }

  get perfilNutricional() {
    return this.registroForm.get('perfilNutricional') as FormGroup;
  }

}
