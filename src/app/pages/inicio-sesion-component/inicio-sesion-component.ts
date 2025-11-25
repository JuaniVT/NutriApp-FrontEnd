import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth';
import { SocialAuthService, GoogleLoginProvider, SocialUser } from '@abacritt/angularx-social-login';
declare var google : any;

@Component({
  selector: 'app-inicio-sesion-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './inicio-sesion-component.html',
  styleUrl: './inicio-sesion-component.css',
})
export class InicioSesionComponent implements OnInit{
  protected loginForm = new FormGroup({
      username: new FormControl('',[ Validators.required, Validators.minLength(4)]),
      password: new FormControl('',[ Validators.required, Validators.minLength(0)])
    });;

  errorMessage : string = '';
  submitted = false;
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  fechaHoy = new Date().toLocaleDateString('en-CA');

   ngOnInit(): void {
    google.accounts.id.initialize(
      {
        client_id: '1014095084666-jr120vlq4cad4uregm9qpkclkes2je8r.apps.googleusercontent.com',
        callback: (resp: any) => { this.handleCredentialResponse(resp)
        }
      })
    google.accounts.id.renderButton(document.getElementById("google-btn"), {
      theme: 'filled_blue',
      size: 'large',
      shape: 'rectangle',
      width: 350
    })
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

    this.authService.login(username!, password!).subscribe({
  next: (res) => {
    this.authService.saveToken(res.token);
    alert("Login exitoso");
    this.router.navigate(['/dia', this.fechaHoy])
    this.errorMessage = '';
  },
  error: (err) => {
    if (err.status === 400) {
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

handleCredentialResponse(response: any) {
  const idToken = response.credential;

  this.authService.googleLogin(idToken).subscribe({
    next: (res) => {

      if (!res.registered) {
        // Usuario NO existe → redirigir al registro con datos precargados
        this.router.navigate(['/registro'], {
  state: {
    googleUser: {
      email: res.email,
      name: res.name
    }
  }
});
        return;
      }

      // Usuario EXISTE → guardar token y entrar a la app
      this.authService.saveToken(res.token); // asegurate que saveToken guarde en localStorage

      alert("Login con Google exitoso");
      this.router.navigate(['/dia', this.fechaHoy]);
    },
    error: (err) => {
      console.error("Error login Google:", err);
      this.errorMessage = 'Error en el login con Google.';
    }
  });
}



}


