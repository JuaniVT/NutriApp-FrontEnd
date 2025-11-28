import { inject, Injectable } from '@angular/core';
import { FechaLocalService } from '../../service/FechaLocalService';

@Injectable({ providedIn: 'root' })

export class ManejadorMes {


    private manejadorFechas = inject(FechaLocalService);

    generarDiasDelMes(fecha: Date) {

        const dias: any[] = [];

        const año = fecha.getFullYear();
        const mes = fecha.getMonth();

        // obtenemos el inicio del mes mandando 1 que seria el primer dia del mes
        const inicioMes = this.manejadorFechas.toLocal(new Date(año, mes, 1));


        // obtenemos el final del mes accediendo al siguiente mes de la fecha y al
        //  obtener el dia 0 estas en el dia anterior al primero, osea en ultimo dia del mes anterior
        const finMes = this.manejadorFechas.toLocal(new Date(año, mes + 1, 0));


        for (let inicio = 1; inicio <= finMes.getDate(); inicio++) {

            const actual = this.manejadorFechas.toLocal(new Date(año, mes, inicio));
            const iso = this.manejadorFechas.toIsoDate(actual);

            dias.push({

                fecha: iso,
                numero: inicio,
                esHoy: actual.getTime() ===
                        this.manejadorFechas.toLocal(fecha).getTime() // booleano que nos permite pintar el dia si es que el mismo es el actual

            });

        }


        return dias;

    }




}
