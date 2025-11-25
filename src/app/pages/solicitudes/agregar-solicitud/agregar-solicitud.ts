import { Component, inject, output } from '@angular/core';
import { SolicitudService } from '../../../service/solicitud';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { config } from 'rxjs';
import { Solicitud } from '../../../models/solicitud';

@Component({
  selector: 'app-agregar-solicitud',
  imports: [ReactiveFormsModule],
  templateUrl: './agregar-solicitud.html',
  styleUrl: './agregar-solicitud.css',
})
export class AgregarSolicitud {

  private readonly solicitudService = inject(SolicitudService);

  //output para cerra la ventana modal
  readonly isAdding = output<boolean>();

  //output para agregar la solcitud en la lista de solicitudes
  readonly solicitudAgregada = output<Solicitud>(); 

  protected readonly form = new FormBuilder().nonNullable.group({
    nombreComida: ["", [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$')]], 
    porcion: [0, [Validators.required, Validators.min(1)]], 
    calorias: [0, [Validators.required, Validators.min(0)]], 
    proteinas: [0, [Validators.required, Validators.min(0)]], 
    carbohidratos: [0, [Validators.required, Validators.min(0)]], 
    grasas: [0, [Validators.required, Validators.min(0)]],
    fecha: [new Date().toISOString().slice(0, 19)]
  })

  get nombreComida (){
    return this.form.controls.nombreComida
  }

  get porcion (){
    return this.form.controls.porcion
  }

  get calorias (){
    return this.form.controls.calorias
  }

  get proteinas (){
    return this.form.controls.proteinas
  }

  get carbohidratos (){
    return this.form.controls.carbohidratos
  }

  get grasas (){
    return this.form.controls.grasas
  }


  handleAdd(){
    if(confirm("Seguro que desea agregar la solicitud: ")){
      this.solicitudService.insert(this.form.getRawValue()).subscribe({
        //emitimos el output con la solicitud asi se agrega a la lista dinamicamente y cerramos la ventana modal
        next: (s) => {this.solicitudAgregada.emit(this.form.getRawValue()),   this.handleCancel()},
        error: (e) => {alert(e.message)}
      })    
    }
  }

  handleCancel(){
    this.isAdding.emit(false);
  }
}
