import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth';
import { SocialAuthService, GoogleLoginProvider, SocialUser } from '@abacritt/angularx-social-login';
import { DialogService } from '../../service/dialog';
declare var google : any;

@Component({
  selector: 'app-inicio-sesion-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './inicio-sesion-component.html',
  styleUrl: './inicio-sesion-component.css',
})
export class InicioSesionComponent implements OnInit, AfterViewInit, OnDestroy {
  protected loginForm = new FormGroup({
      username: new FormControl('',[ Validators.required, Validators.minLength(4)]),
      password: new FormControl('',[ Validators.required, Validators.minLength(0)])
    });;

  errorMessage : string = '';
  submitted = false;
  loading = false;
  showPassword = false;
  shake = false;
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly dialog = inject(DialogService);
  private readonly host = inject<ElementRef<HTMLElement>>(ElementRef);
  fechaHoy = new Date().toLocaleDateString('en-CA');

   ngOnInit(): void {
    // Si el script de Google Sign-In no cargó (sin red, bloqueado por una
    // extensión, etc.) no se debe cortar la inicialización del componente:
    // el resto de la vista y sus animaciones tienen que seguir funcionando.
    try {
      google.accounts.id.initialize(
        {
          client_id: '1014095084666-jr120vlq4cad4uregm9qpkclkes2je8r.apps.googleusercontent.com',
          callback: (resp: any) => { this.handleCredentialResponse(resp)
          }
        })
      const googleBtnContainer = document.getElementById("google-btn");
      const anchoDisponible = googleBtnContainer?.offsetWidth || 350;
      google.accounts.id.renderButton(googleBtnContainer, {
        theme: 'outline',
        size: 'large',
        shape: 'pill',
        logo_alignment: 'left',
        width: Math.min(350, anchoDisponible)
      })
    } catch (e) {
      console.error('No se pudo inicializar Google Sign-In:', e);
    }
  }

  /** listener de scroll para el degradé del fondo; se guarda para removerlo al destruir. */
  private bgScrollHandler?: () => void;

  ngAfterViewInit(): void {
    this.initRevealOnScroll();
    this.initBackgroundScroll();
  }

  /* ===== Demo interactiva de "Probá cómo funciona" ===== */

  /** Meta de calorías elegida con el slider. */
  metaKcal = 2100;

  /** Reparto de macros por calorías (30% P / 40% C / 30% G) → gramos. */
  get gramosP(): number { return Math.round(this.metaKcal * 0.30 / 4); }
  get gramosC(): number { return Math.round(this.metaKcal * 0.40 / 4); }
  get gramosG(): number { return Math.round(this.metaKcal * 0.30 / 9); }

  private static readonly RING_R = { p: 52, c: 42, g: 32 };
  /** Escala visual del anillo: gramos que equivalen a una vuelta completa. */
  private static readonly RING_MAX_G = 300;

  get circP(): number { return 2 * Math.PI * InicioSesionComponent.RING_R.p; }
  get circC(): number { return 2 * Math.PI * InicioSesionComponent.RING_R.c; }
  get circG(): number { return 2 * Math.PI * InicioSesionComponent.RING_R.g; }

  private arcOffset(circ: number, gramos: number): number {
    const frac = Math.min(gramos / InicioSesionComponent.RING_MAX_G, 1);
    return circ * (1 - frac);
  }
  get dashP(): number { return this.arcOffset(this.circP, this.gramosP); }
  get dashC(): number { return this.arcOffset(this.circC, this.gramosC); }
  get dashG(): number { return this.arcOffset(this.circG, this.gramosG); }

  onMetaKcal(valor: string): void {
    this.metaKcal = Number(valor);
  }

  /** Hidratación de la demo. */
  readonly aguaMeta = 2000;
  aguaMl = 0;

  get aguaPct(): number {
    return Math.min(Math.round((this.aguaMl / this.aguaMeta) * 100), 100);
  }

  sumarAgua(ml: number): void {
    this.aguaMl = Math.min(this.aguaMl + ml, this.aguaMeta);
  }

  resetAgua(): void {
    this.aguaMl = 0;
  }

  ngOnDestroy(): void {
    if (this.bgScrollHandler) {
      window.removeEventListener('scroll', this.bgScrollHandler);
      window.removeEventListener('resize', this.bgScrollHandler);
    }
  }

  /** Actualiza --bg-progress (0→1) en el :host según cuánto se scrolleó hacia
      "Sobre Nosotros": el CSS interpola el color del fondo entre crema y durazno. */
  private initBackgroundScroll(): void {
    const host = this.host.nativeElement;
    const seccion = host.querySelector<HTMLElement>('.nosotros-section');
    if (!seccion) return;
    if (window.matchMedia?.('(prefers-reduced-motion: reduce)').matches) return;

    let pendiente = false;

    const actualizar = () => {
      pendiente = false;
      const topSeccion = seccion.getBoundingClientRect().top + window.scrollY;
      // progreso completo cuando la sección llega a ~mitad de pantalla
      const fin = Math.max(topSeccion - window.innerHeight * 0.55, 1);
      const p = Math.min(Math.max(window.scrollY / fin, 0), 1);
      host.style.setProperty('--bg-progress', p.toFixed(3));
    };

    const onScroll = () => {
      if (pendiente) return;
      pendiente = true;
      requestAnimationFrame(actualizar);
    };

    this.bgScrollHandler = onScroll;
    window.addEventListener('scroll', onScroll, { passive: true });
    window.addEventListener('resize', onScroll, { passive: true });
    actualizar();
  }

  /** Agrega .revealed a la sección "Sobre Nosotros" cuando entra en viewport,
      disparando la transición escalonada de entrada definida en el CSS. */
  private initRevealOnScroll(): void {
    const seccion = this.host.nativeElement.querySelector<HTMLElement>('.nosotros-section');
    if (!seccion) return;

    if (typeof IntersectionObserver === 'undefined') {
      seccion.classList.add('revealed');
      return;
    }

    const io = new IntersectionObserver(
      (entradas) => {
        for (const entrada of entradas) {
          if (!entrada.isIntersecting) continue;
          entrada.target.classList.add('revealed');
          io.unobserve(entrada.target);
        }
      },
      { threshold: 0.15 }
    );

    io.observe(seccion);
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
    if (this.loginForm.invalid) {
      this.triggerShake();
      return;
    }

    const { username, password } = this.loginForm.value;

    this.loading = true;
    this.authService.login(username!, password!).subscribe({
  next: (res) => {
    this.loading = false;
    this.authService.saveToken(res.token);
    this.dialog.success("Login exitoso");
    this.router.navigate(['/dia', this.fechaHoy])
    this.errorMessage = '';
  },
  error: (err) => {
    this.loading = false;
    if (err.status === 400) {
      this.errorMessage = 'Usuario o contraseña incorrectos.';
    } else {
      this.errorMessage = 'Ocurrió un error en el servidor.';
    }
    this.triggerShake();
  }
});
}

/** Reinicia y dispara la animación de "temblor" del formulario. */
private triggerShake(): void {
  this.shake = false;
  setTimeout(() => (this.shake = true));
  setTimeout(() => (this.shake = false), 500);
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

      this.dialog.success("Login con Google exitoso");
      this.router.navigate(['/dia', this.fechaHoy]);
    },
    error: (err) => {
      console.error("Error login Google:", err);
      this.errorMessage = 'Error en el login con Google.';
    }
  });
}



}


