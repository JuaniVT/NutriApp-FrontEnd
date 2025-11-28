import { inject, Injectable } from '@angular/core';
import { FechaLocalService } from '../../service/FechaLocalService';

@Injectable({ providedIn: 'root' })

export class ManejadorMes {


    private manejadorFechas = inject(FechaLocalService);

    generarDiasDelMes(fecha: Date) {

        const dias: any[] = [];

        const año = fecha.getFullYear();
        const mes = fecha.getMonth();

        const hoy = this.manejadorFechas.toLocal(new Date());

        // obtenemos el inicio del mes mandando 1 que seria el primer dia del mes
        const inicioMes = this.manejadorFechas.toLocal(new Date(año, mes, 1));


        // obtenemos el final del mes accediendo al siguiente mes de la fecha y al
        //  obtener el dia 0 estas en el dia anterior al primero, osea en ultimo dia del mes anterior
        const finMes = this.manejadorFechas.toLocal(new Date(año, mes + 1, 0));


        const nombresDias = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];


        const indicePrimerDia = (inicioMes.getDay() + 6) % 7; // lunes=0 ... domingo=6

        // ⇢ Agregar celdas vacías antes del 1°
        for (let i = 0; i < indicePrimerDia; i++) {
            dias.push({ vacio: true });
        }


        for (let inicio = 1; inicio <= finMes.getDate(); inicio++) {

            const actual = this.manejadorFechas.toLocal(new Date(año, mes, inicio));
            const iso = this.manejadorFechas.toIsoDate(actual);

            dias.push({
                vacio: false,
                fecha: iso,
                numero: inicio,
                nombre: nombresDias[actual.getDay()],
                esHoy:actual.toDateString() === hoy.toDateString(), // booleano que nos permite pintar el dia si es que el mismo es el actual
                esFuturo: actual > hoy
            });


        }


        // ⇢ Completar final con vacíos para cerrar la última semana
            while (dias.length % 7 !== 0) {
                dias.push({ vacio: true });
            }

        return dias;

    }




}
