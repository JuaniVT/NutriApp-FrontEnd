import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../service/auth';
import { EmailVerificationService } from '../../service/email-verification-service';
import { CustomAsyncValidators } from '../../models/CustomAsyncValidators';
import { DialogService } from '../../service/dialog';

@Component({
  selector: 'app-registro-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './registro-component.html',
  styleUrl: './registro-component.css',
})
export class RegistroComponent implements OnInit, OnDestroy {

 protected readonly client = inject(AuthService);
  private router = inject(Router);
  private dialog = inject(DialogService);
 fechaHoy = new Date().toLocaleDateString('en-CA');

  fechaMinima2017(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;

  const fechaIngresada = new Date(control.value);
  const fechaMinima = new Date('2017-01-01');
  const hoy = new Date();
  

  // Normalizar fechas para evitar errores por horas
  hoy.setHours(0,0,0,0);
  fechaIngresada.setHours(0,0,0,0);

  // ❌ No permitir fecha futura
  if (fechaIngresada > hoy) {
    return { fechaFutura: true };
  }

  // ❌ No permitir fecha igual o posterior a 2017
  if (fechaIngresada >= fechaMinima) {
    return { fechaMuyReciente: true };
  }

  return null;
}




  protected registroForm = new FormGroup({
  usuario: new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
      Validators.maxLength(20), // coincide con el mensaje del HTML
      Validators.pattern('^[a-zA-Z0-9._-]+$') // permite guion medio también
    ],   [CustomAsyncValidators.usernameExists(this.client)]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};:\\"\\\\|,.<>/?]).+$')
    ])
  }),

  persona: new FormGroup({
    nombre: new FormControl('', [Validators.required, Validators.minLength(3)]),
    apellido: new FormControl('', [Validators.required, Validators.minLength(3)]),
    dni: new FormControl('', [Validators.required, Validators.pattern('^\\d{8}$')],  [CustomAsyncValidators.dniExists(this.client)]),
    fechaNacimiento: new FormControl('', [Validators.required, this.fechaMinima2017]),
    telefono: new FormControl('', [Validators.required, Validators.pattern('^\\d{7,15}$')], [CustomAsyncValidators.telefonoExists(this.client)]),
    direccion: new FormControl('', [Validators.required]),
    genero: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email], [CustomAsyncValidators.emailExists(this.client)])
  }),

  perfilNutricional: new FormGroup({
    peso: new FormControl<number | null>(null, [Validators.required, Validators.min(30), Validators.max(300)]),
    altura: new FormControl<number | null>(null, [Validators.required, Validators.min(100), Validators.max(250)]),
    edad: new FormControl<number | null>(null, [Validators.required, Validators.min(10), Validators.max(120)]),
    nivelActividadFisica: new FormControl('', [Validators.required]),
    objetivoCaloricoTipo: new FormControl('', [Validators.required])
  })
});

  
 onSubmit() {
  const datos = this.registroForm.value;

  console.log(datos);
  this.client.register(datos).subscribe({
    next: (resp) => {
      this.dialog.success('Cuenta creada correctamente');
      this.router.navigate(['/dia', this.fechaHoy]);
    },
    error: (err) => {
      // Revisamos si el backend envió un 'mensaje'
      if (err.error && err.error.mensaje) {
        this.dialog.error(err.error.mensaje); // ahora sí mostrará "El nombre de usuario ya está en uso", etc.
      } else {
        this.dialog.error('Ocurrió un error al registrarse.');
      }
    }
  });
}



 pasoPersona: number = 0;

// Solo los pasos 4 (persona completa) y 6 (usuario) validan el grupo entero
// acá; los pasos 1-3 ya controlan sus campos puntuales con [disabled] en el HTML.
private grupoDelPaso(paso: number): FormGroup | null {
  if (paso === 4) return this.registroForm.controls.persona;
  if (paso === 6) return this.registroForm.controls.usuario;
  return null;
}

siguientePasoPersona() {
  const grupo = this.grupoDelPaso(this.pasoPersona);

  // .valid es true SOLO si el status es 'VALID' (un grupo en PENDING no pasa).
  if (grupo && !grupo.valid) {
    grupo.markAllAsTouched();
    return;
  }

  this.pasoPersona++;
}

pasoAnteriorPersona() {
  this.pasoPersona--;
}

//RELACIONADO A LA SESSION DEL OBJETIVO CALORICO
protected objetivosInfo = [
  {
    key: "MANTENIMIENTO",
    titulo: "Mantenimiento",
    descripcion: "Mantener tu peso actual con un equilibrio ideal."
  },
  {
    key: "DEFICIT_LIGERO",
    titulo: "Déficit Ligero",
    descripcion: "Bajar de peso de forma suave y constante."
  },
  {
    key: "DEFICIT_MODERADO",
    titulo: "Déficit Moderado",
    descripcion: "Pérdida de peso más rápida pero controlada."
  },
  {
    key: "SUPERAVIT_LIGERO",
    titulo: "Súperavit Ligero",
    descripcion: "Subir de peso o masa muscular de forma gradual."
  },
  {
    key: "SUPERAVIT_MODERADO",
    titulo: "Súperavit Moderado",
    descripcion: "Aumentar masa muscular más agresivamente."
  }
];

seleccionarObjetivo(valor: string) {
  this.registroForm.controls.perfilNutricional.controls.objetivoCaloricoTipo.setValue(valor);
}

//RELACIONADO A LA SESSION DEL NIVEL DE ACTIVIDAD

seleccionarNivelActividad(valor: string) {
  this.registroForm
    .get("perfilNutricional.nivelActividadFisica")
    ?.setValue(valor);
}

protected nivelActividadInfo = [
  {
    key: "SEDENTARIO",
    titulo: "Sedentario",
    descripcion: "Poco o ningún ejercicio diario."
  },
  {
    key: "LIGERA",
    titulo: "Ligera",
    descripcion: "Actividad física ligera 1-3 veces por semana."
  },
  {
    key: "MODERADA",
    titulo: "Moderada",
    descripcion: "Ejercicio moderado 3-5 veces por semana."
  },
  {
    key: "INTENSA",
    titulo: "Intensa",
    descripcion: "Entrenamientos intensos 6-7 veces por semana."
  },
  {
    key: "MUY_INTENSA",
    titulo: "Muy Intensa",
    descripcion: "Actividad física extrema o 2 entrenamientos al día."
  }
];

// Persona
get nombre() { return this.registroForm.get('persona.nombre'); }
get apellido() { return this.registroForm.get('persona.apellido'); }
get genero() { return this.registroForm.get('persona.genero'); }
get email() { return this.registroForm.get('persona.email'); }

// Perfil nutricional
get peso() { return this.registroForm.get('perfilNutricional.peso'); }
get altura() { return this.registroForm.get('perfilNutricional.altura'); }
get edad() { return this.registroForm.get('perfilNutricional.edad'); }

emailVerificado: boolean = false;
codigoEnviado: boolean = false;

// true cuando el email vino autocompletado desde Google: el campo queda solo lectura
emailBloqueadoPorGoogle: boolean = false;
mensaje: string = "";
codigo: string = "";
cargando: boolean = false;

// Reenvío de código con contador
readonly segundosReenvio = 30;
contadorReenvio: number = 0;
reenviando: boolean = false;
private intervaloReenvio: any = null;

constructor(private emailService: EmailVerificationService) {}

ngOnDestroy(): void {
  this.detenerContadorReenvio();
}

private iniciarContadorReenvio() {
  this.detenerContadorReenvio();
  this.contadorReenvio = this.segundosReenvio;
  this.intervaloReenvio = setInterval(() => {
    this.contadorReenvio--;
    if (this.contadorReenvio <= 0) {
      this.detenerContadorReenvio();
    }
  }, 1000);
}

private detenerContadorReenvio() {
  if (this.intervaloReenvio) {
    clearInterval(this.intervaloReenvio);
    this.intervaloReenvio = null;
  }
}

enviarCodigoEmail() {
  const email = this.registroForm.get('persona.email')?.value;

  if (!email) {
    this.mensaje = "Ingresa un email válido.";
    return;
  }

  this.cargando = true;
  this.mensaje = "";

  this.emailService.enviarCodigo(email).subscribe({
    next: () => {
      this.codigoEnviado = true;
      this.cargando = false;
      this.mensaje = "Código enviado a tu correo 📩";
      this.iniciarContadorReenvio();
    },
    error: () => {
      this.cargando = false;
      this.mensaje = "Error al enviar el código. Intenta más tarde.";
    }
  });
}

reenviarCodigo() {
  if (this.contadorReenvio > 0 || this.reenviando) {
    return;
  }

  const email = this.registroForm.get('persona.email')?.value;

  if (!email) {
    this.mensaje = "Ingresa un email válido.";
    return;
  }

  this.reenviando = true;
  this.mensaje = "";

  this.emailService.enviarCodigo(email).subscribe({
    next: () => {
      this.reenviando = false;
      this.codigo = "";
      this.mensaje = "Te reenviamos un nuevo código a tu correo 📩";
      this.iniciarContadorReenvio();
    },
    error: () => {
      this.reenviando = false;
      this.mensaje = "Error al reenviar el código. Intenta más tarde.";
    }
  });
}

verificarCodigo() {
  const email = this.registroForm.get('persona.email')?.value;

  if (!this.codigo) {
    this.mensaje = "Ingresa el código recibido.";
    return;
  }
  console.log(email, this.codigo)
  this.cargando = true;
  this.mensaje = "";

  this.emailService.verificarCodigo(email!, this.codigo).subscribe({
next: (res: any) => {
this.cargando = false;
if (res.exito) {
this.emailVerificado = true;
this.mensaje = "Correo verificado correctamente ✔️";
} else {
this.mensaje = "Código incorrecto o expirado ❌";
}
},
error: () => {
// Esto ahora solo se dispara si hay un fallo real de conexión
this.cargando = false;
this.mensaje = "Error al verificar el correo ❌";
}
});
}

ngOnInit(): void {
  const userData = history.state['googleUser'];

  if (userData) {
    // Autocompleta nombre y email
    this.registroForm.patchValue({
      persona: {
        nombre: userData.name || '',
        email: userData.email || ''
      }
    });

    // El email lo trae Google: no tiene sentido que el usuario lo edite.
    // El nombre sí queda editable, se mantiene igual que antes.
    if (userData.email) {
      this.emailBloqueadoPorGoogle = true;
    }

    // (Opcional) generar username automático si no existe
    if (userData.email && !this.registroForm.get('usuario.username')?.value) {
      const sugerido = userData.email.split('@')[0]
        .replace(/[^a-zA-Z0-9._]/g, '')
        .substring(0, 20);

      this.registroForm.get('usuario.username')?.setValue(sugerido);
    }
  }
}



}
