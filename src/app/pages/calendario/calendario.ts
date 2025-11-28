import { Component, inject } from '@angular/core';
import { FechaLocalService } from '../../service/FechaLocalService';
import { ActivatedRoute, Router } from '@angular/router';
import { ManejadorMes } from './ManejadorMes';

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
    }).toUpperCase();

    this.diasMes = this.manejadorMeses.generarDiasDelMes(local);

  }

  cambiarMes(numeroMes: number) {

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

     // Bloquear días futuros
    if (diaLocal > this.fechaHoy) {
      return;
    }

    this.router.navigate(['/dia', diaIso]);

  }

}


