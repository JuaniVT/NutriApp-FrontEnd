
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { AlimentoBusquedaDTO } from '../../models/alimentoBusquedadto';
import { DiaService } from '../../service/dia-service';
import { AlimentoInPaquete } from '../../models/alimentoInPaquete';
import { Paquete } from '../../models/paquete';
import { FormsModule } from '@angular/forms';
import { PaqueteService } from '../../service/paquete-service';
import { ComidaFavoritaDTO } from '../../models/comidaFavoritaDTO';
import { ComidaIngeridaSalidaDTO } from '../../models/comida-ingerida-salida.dto';
import { catchError, debounceTime, distinctUntilChanged, of, Subject, switchMap } from 'rxjs';

@Component({
  selector: 'app-comidas-favoritas',
  imports: [CommonModule, FormsModule],
  templateUrl: './comidas-favoritas.html',
  styleUrl: './comidas-favoritas.css',
})
export class ComidasFavoritasComponent {
   paquetes: Paquete[] = [];
  paqueteSeleccionado?: Paquete;
  protected readonly client = inject(PaqueteService);
  // Para agregar alimento
  filtro: string = '';
  resultados: AlimentoBusquedaDTO[] = [];
  alimentoSeleccionado?: AlimentoBusquedaDTO;
  protected gramos = 100;
  protected readonly search = inject(DiaService);
  mensajeError: string = '';


  private filtroSubject: Subject<string> = new Subject<string>();

  
  constructor(private diaService: DiaService) {
    this.filtroSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(filtro => {
        if (!filtro.trim()) {
          this.resultados = [];
          this.mensajeError = '';
          return of([]); // no hace petición si el filtro está vacío
        }
        return this.diaService.buscarAlimentos(filtro).pipe(
          catchError(err => {
            console.error('Error en la búsqueda:', err);
            this.mensajeError = 'Ocurrió un error al buscar alimentos.';
            return of([]); // devuelve array vacío si falla la petición
          })
        );
      })
    ).subscribe(res => {
      this.resultados = res;
      if (res.length === 0 && this.filtro.trim()) {
        this.mensajeError = 'No se encontraron alimentos.';
      } else {
        this.mensajeError = '';
      }
    });
  }

  buscarAlimentos() {
    this.filtroSubject.next(this.filtro);
  }

   ngOnInit(): void {
    this.cargarComidasFavoritas();
  }

  cargarComidasFavoritas(): void {
    this.client.getComidasFavoritasPorUsuario()
      .pipe(
        catchError(err => {
          console.error(err);
          alert('Error al cargar comidas favoritas');
          return of([]);
        })
      )
      .subscribe(respuesta => {
        this.paquetes = [];
        respuesta.forEach(fav => {
          let elemento = this.paquetes.find(p => p.nombrePaquete === fav.nombrePaquete);
          if (!elemento) {
            elemento = { nombrePaquete: fav.nombrePaquete, alimentos: [] };
            this.paquetes.push(elemento);
          }
          let alimento: AlimentoInPaquete = { id: fav.comidaId, nombre: fav.nombreComida, gramos: fav.cantidad };
          elemento.alimentos.push(alimento);
        });
      });
  }

  crearPaquete(): void {
    const nombre = prompt('Ingrese el nombre del nuevo paquete');
    if (!nombre) return;
    this.paquetes.push({ nombrePaquete: nombre, alimentos: [] });
    alert(`Paquete "${nombre}" creado con éxito`);
  }

  abrirAgregarAlimento(paquete: Paquete) {
    this.paqueteSeleccionado = paquete;
    this.filtro = '';
    this.resultados = [];
    this.alimentoSeleccionado = undefined;
  }

  cerrarModal() {
    this.paqueteSeleccionado = undefined;
    this.filtro = '';
    this.resultados = [];
    this.alimentoSeleccionado = undefined;
  }

  seleccionarAlimento(alimento: AlimentoBusquedaDTO) {
    this.alimentoSeleccionado = alimento;
  }

  agregarAlPaquete(): void {
    if (!this.paqueteSeleccionado || !this.alimentoSeleccionado) return;

    const nuevaComida: ComidaFavoritaDTO = {
      nombrePaquete: this.paqueteSeleccionado.nombrePaquete,
      nombreComida: this.alimentoSeleccionado.descripcion,
      comidaId: this.alimentoSeleccionado.fdcId,
      cantidad: this.gramos
    };

    const elemento: AlimentoInPaquete = {
      id: this.alimentoSeleccionado.fdcId,
      nombre: this.alimentoSeleccionado.descripcion,
      gramos: this.gramos
    };

    this.client.addComidaFavorita(nuevaComida).subscribe({
  next: mensaje => {
    alert(mensaje); // "Comida agregada correctamente."
    this.paqueteSeleccionado?.alimentos.push(elemento);
    this.cerrarModal();
    this.cargarComidasFavoritas();
  },
  error: err => {
    console.error(err);
    alert('Error al agregar comida al paquete');
  }
});}

  eliminarAlimento(paquete: Paquete, alimento: AlimentoInPaquete): void {
    this.client.eliminarAlimento(paquete.nombrePaquete, alimento.id)
      .subscribe({
        next: () => {
          paquete.alimentos = paquete.alimentos.filter(a => a.id !== alimento.id);
          this.cargarComidasFavoritas();
          alert(`Alimento "${alimento.nombre}" eliminado del paquete "${paquete.nombrePaquete}"`);
        },
        error: err => {
          console.error(err);
          alert('Error al eliminar alimento del paquete');
        }
      });
  }


}
