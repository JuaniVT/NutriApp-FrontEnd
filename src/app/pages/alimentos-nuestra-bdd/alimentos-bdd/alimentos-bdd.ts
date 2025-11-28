import { Component, inject, linkedSignal, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { SolicitudService } from '../../../service/solicitud';
import { AlimentosbddService } from '../../../service/alimentosbdd';
import { Loading } from '../../../visualComponents/loading/loading';
import { not } from 'rxjs/internal/util/not';
import { Conditional } from '@angular/compiler';
import { AgregarAlimentosBDD } from '../agregar-alimentos-bdd/agregar-alimentos-bdd';
import { ComidaBDD } from '../../../models/comida-bdd';

@Component({
  selector: 'alimentos-bdd',
  imports: [Loading, AgregarAlimentosBDD],
  templateUrl: './alimentos-bdd.html',
  styleUrl: './alimentos-bdd.css',
})
export class AlimentosBDD {
  private readonly alimentosBddService = inject(AlimentosbddService);

  //signal para inciarlizar la pagina con los ultimos 10 alimentos
  private readonly ultimos10Alimnetos = toSignal(this.alimentosBddService.getLast10());
  //signal para obtener un señal editable a partir de la anterior asi cambia cuando se busca un alimento
  protected readonly alimentosFiltrados = linkedSignal(() => this.ultimos10Alimnetos());

  //signal para bloquear la UI
  protected readonly isLoading = signal<boolean>(false);

  //signal para popear formulario de edicion
  protected readonly isEditing = signal<boolean>(false);

  //signal para mandarle la comida que se quiere editar al formulario
  protected readonly comidaEditando = signal<ComidaBDD | undefined>(undefined);

  //signal para popear formulario de adcion
  protected readonly isAdding = signal<boolean>(false);

  //signal para mensaje de no encontrado
  protected readonly notFound = signal<boolean>(false);

  

  //busca alimentos en base a un filtro y actualiza la signal
  buscar(filtro: string){

    //si se aprita el boton pero no se escribio nada
    if(filtro.length == 0 || filtro == null){
      //cortamos la ejecucion
      return;
    }
    
    //bloqueamos la UI
    this.isLoading.set(true);

    this.alimentosBddService.filterByFoodName(filtro).subscribe({
      next: (a) => {

        //si no encontro nada
        if(a.length == 0 || a == null){
          //cortamos la ejecucion, debloqueamos la UI, y mostramos mensaje de error
          this.isLoading.set(false);
          this.notFound.set(true);
          return;
        
        }else{
          //reseteamos el mensaje de error
          this.notFound.set(false);
          //actualizamos la lista
          this.alimentosFiltrados.set(a);
          //desbloqueamos la UI
          this.isLoading.set(false);
        }

      },
      error: (e) => {alert(e.message), this.isLoading.set(false)}
    })
  }

  handleDelete(index: number){
    if(confirm("Seguro que desea eliminar el alimento?")){

      //bloqueamos UI 
      this.isLoading.set(true);

      this.alimentosBddService.delete(this.alimentosFiltrados()![index].id!).subscribe({
        next: () => {
          //desbloqueamos UI
          this.isLoading.set(false);
          //eliminamos de la vista
          this.alimentosFiltrados()?.splice(index, 1);
        },
        error: (e) => {alert(e.message), this.isLoading.set(false)}
      })
    }
  }

  addFood(food: ComidaBDD){
    this.alimentosFiltrados()?.unshift(food);
  }

  editFood(food: ComidaBDD){

    //buscamos el indice en la lista en base a los id
    const index = this.alimentosFiltrados()?.findIndex(a => a.id == food.id);

    //actualizamos los datos de la comida en la lista
    this.alimentosFiltrados()![index!] = food;
  }

  openEdit(index: number){
    this.isEditing.set(true);

    this.comidaEditando.set(this.alimentosFiltrados()![index])
  }

  openAdding(){
    this.isAdding.set(true);
  }
}
