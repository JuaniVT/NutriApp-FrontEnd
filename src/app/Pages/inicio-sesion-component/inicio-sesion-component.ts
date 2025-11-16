import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth';

@Component({
  selector: 'app-inicio-sesion-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './inicio-sesion-component.html',
  styleUrl: './inicio-sesion-component.css',
})
export class InicioSesionComponent implements OnInit{
  loginForm! : FormGroup;
  errorMessage : string = '';
  submitted = false;
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);

   ngOnInit(): void {
    // Inicializamos el formulario con validaciones
    this.loginForm = new FormGroup({
      username: new FormControl('',[ Validators.required, Validators.minLength(4)]),
      password: new FormControl('',[ Validators.required, Validators.minLength(0)])
    });
  }


  get username()
{
  return this.loginForm.controls['username'];
}

get password()
{
  return this.loginForm.controls['password'];
}

 onSubmit(): void {

  this.submitted = true;
    if (this.loginForm.invalid) return;

    const { username, password } = this.loginForm.value;

    this.authService.login(username, password).subscribe({
  next: (res) => {
    this.authService.saveToken(res.token);
    alert("Login exitoso");
    this.router.navigate(['/dia', '2025-11-11'])
    this.errorMessage = '';
  },
  error: (err) => {
    if (err.status === 401) {
      this.errorMessage = 'Usuario o contraseña incorrectos.';
    } else {
      this.errorMessage = 'Ocurrió un error en el servidor.';
    }
  }
});
}

irRegistro()
{
  this.router.navigate(['/registro']);
}
}


