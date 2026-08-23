import { Component, effect, inject, input, output, signal } from '@angular/core';
import { SolicitudService } from '../../../service/solicitud';
import { AlimentosbddService } from '../../../service/alimentosbdd';
import { AlimentosBDD } from '../alimentos-bdd/alimentos-bdd';
import { ComidaBDD } from '../../../models/comida-bdd';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Loading } from '../../../visualComponents/loading/loading';
import { DialogService } from '../../../service/dialog';

@Component({
  selector: 'app-agregar-alimentos-bdd',
  imports: [ReactiveFormsModule, Loading],
  templateUrl: './agregar-alimentos-bdd.html',
  styleUrl: './agregar-alimentos-bdd.css',
})
export class AgregarAlimentosBDD {

  private readonly alimentosBddService= inject(AlimentosbddService);
  private readonly dialog = inject(DialogService);

  //output para cerra la ventana modal
  readonly isOpen = output<boolean>();

  //input para saber si se esta editando
  readonly isEditing = input<boolean>();

  //input para patchear los campos con la comida a editar
  readonly comidaInput = input<ComidaBDD>();

  //input para saber si se esta agregando
  readonly isAdding = input<boolean>();

  //output para agregar la solcitud en la lista de solicitudes
  readonly comidaOutput = output<ComidaBDD>(); 

  //signal para bloquer la UI cuando carga una peticion
  protected readonly isLoading = signal<boolean>(false);


  //cuando me llegue la comida con los campos se ejecuta el effect y se patchean los campos de form
  constructor(){
    effect(() => {
      this.form.patchValue(this.comidaInput()!);
    })
  }

  protected readonly form = new FormBuilder().nonNullable.group({
    nombreComida: ["", [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$')]], 
    gramosPorPorcion: [0, [Validators.required, Validators.min(1), Validators.max(99999)]], 
    calorias: [0, [Validators.required, Validators.min(0), Validators.max(99999)]], 
    proteinas: [0, [Validators.required, Validators.min(0), Validators.max(99999)]], 
    carbohidratos: [0, [Validators.required, Validators.min(0), Validators.max(99999)]], 
    grasas: [0, [Validators.required, Validators.min(0), Validators.max(99999)]]
  })

  get nombreComida (){
    return this.form.controls.nombreComida
  }

  get gramosPorPorcion (){
    return this.form.controls.gramosPorPorcion
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


  async handleAdd(){
    const confirmado = await this.dialog.confirm("Seguro que desea agregar la comida?");
    if(confirmado){

      //bloqueamo la UI
      this.isLoading.set(true);

      this.alimentosBddService.insert(this.form.getRawValue()).subscribe({
        next: (a) => {
          //desbloqueamos UI
          this.isLoading.set(false);
          //emitimos la comida insertada del back para que se muestre con el id y todo en el componente padre
          this.comidaOutput.emit(a);
          //cerramos el form
          this.isOpen.emit(false);
        },
        error: (e) => {this.dialog.error(e.message), this.isLoading.set(false)}
      })
    }
  }

  async handleEdit(){
    const confirmado = await this.dialog.confirm("Seguro que desea editar la comida?");
    if(confirmado){

      //bloqueamo la UI
      this.isLoading.set(true);

      this.alimentosBddService.modify(this.comidaInput()?.id!, this.form.getRawValue()).subscribe({
        next: (a) => {
          //desbloqueamos UI
          this.isLoading.set(false);
          //emitimos la comida insertada del back para que se muestre con el id y todo en el componente padre
          this.comidaOutput.emit(a);
          //cerramos el form
          this.isOpen.emit(false);
        },
        error: (e) => {this.dialog.error(e.message), this.isLoading.set(false)}
      })

    }
  }

  handleCancel(){
    this.isOpen.emit(false);
  }
}
