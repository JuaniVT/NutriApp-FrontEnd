import { Component, inject } from '@angular/core';
import { FechaLocalService } from '../../service/FechaLocalService';
import { ActivatedRoute, Router } from '@angular/router';
import { ManejadorMes } from './ManejadorMes';
import { DiaService } from '../../service/dia-service';

@Component({
  selector: 'app-calendario',
  imports: [],
  templateUrl: './calendario.html',
  styleUrl: './calendario.css',
})
export class Calendario {

  private router = inject(Router);
  private route = inject(ActivatedRoute);
  protected manejadorFechas = inject(FechaLocalService);
  protected manejadorMeses = inject(ManejadorMes);
  protected diaService = inject(DiaService);
  protected fechaHoy = this.manejadorFechas.toLocalDate(this.route.snapshot.paramMap.get('fecha')!);


  protected diasMes: any[] = [];

  nombreMes = '';


  ngOnInit() {

    this.cargarMes(this.fechaHoy);

  }



  cargarMes(fecha: Date) {

    const local = this.manejadorFechas.toLocal(fecha);

    this.nombreMes = fecha.toLocaleDateString('es-ES', {
      month: 'long',
      year: 'numeric'
    });

    this.nombreMes = this.nombreMes.charAt(0).toUpperCase() + this.nombreMes.slice(1); // primera letra del mes en mayusuculas

    // generamos las tarjetas
    this.diasMes = this.manejadorMeses.generarDiasDelMes(local);

    // agarramos el año y el mes de la zona horaria del usuario
    const año = local.getFullYear();
    const mes = local.getMonth() + 1;


    this.diaService.obtenerEstadosDelMes(año, mes)
        .subscribe(estados => {                         // estados es la lista de los dias que retorna el 
                                                        // backend con su fecha y su estado

            for (const dia of this.diasMes) {

                if (dia.vacio) {                      //Si este casillero es vacío, salteá esta iteración y pasa al siguiente"
                    continue;
                }

                const estado = estados.find(      //Para cada elemento e, fijate si su fecha es igual a la                        
                    e => e.fecha === dia.fecha    // fecha del día que estoy recorriendo, si son iguales find() devuelve un EstadoDiaDTO y se guarda en Estado
                );

                if (estado) {
                    dia.estadoDia = estado.estadoDia;       // si se encontro un estado para este dia, se le asigna el estado que coincide con la fecha de el dia que nos retorna la lista del back
                }
            }

        });

  }

  cambiarMes(numeroMes: number) {


    const contenedor = document.querySelector('.dias');

    if (contenedor instanceof HTMLElement) {
      contenedor.classList.remove('animando');
      void contenedor.offsetWidth;
      contenedor.classList.add('animando');
    }



    const nuevoMes = this.manejadorFechas.toLocal(new Date(this.fechaHoy.getFullYear(), this.fechaHoy.getMonth() + numeroMes, 1));


    // bloquear meses futuros
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    if (nuevoMes > new Date(hoy.getFullYear(), hoy.getMonth(), 1)) {
      return;
    }

    this.fechaHoy = nuevoMes; //actualizamos la fecha actual 
    this.cargarMes(nuevoMes);

  }


  irAlDia(diaIso: string) {

    const diaLocal = this.manejadorFechas.toLocalDate(diaIso);

    const hoyReal = this.manejadorFechas.toLocalDate(
      this.manejadorFechas.toIsoDate(new Date())
    );

    // Bloquear días futuros
    if (diaLocal > hoyReal) {
      return;
    }

    this.router.navigate(['/dia', diaIso]);

  }

}


