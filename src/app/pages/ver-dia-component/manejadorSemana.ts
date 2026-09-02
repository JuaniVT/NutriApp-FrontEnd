
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})

export class ManejadorSemana {

    generarSemana(fechaReferencia: Date) {
        const semana: any[] = [];       //semana con 7 objetos que van a ser dias
        const inicio = new Date(fechaReferencia);
        inicio.setHours(0, 0, 0, 0);  //normaliza a medianoche local


        const diaSemana = (inicio.getDay() + 6) % 7;
        inicio.setDate(inicio.getDate() - diaSemana);      // Ajustar para que la semana empiece en lunes

        const hoy = new Date();
        hoy.setHours(0, 0, 0, 0);           //se usa para que solo se compare la fecha y no el horario

        const nombres = [
            "Lunes", "Martes", "Miércoles", "Jueves",
            "Viernes", "Sábado", "Domingo"
        ];

        for (let i = 0; i < 7; i++) {
            const fecha = new Date(inicio);
            fecha.setDate(inicio.getDate() + i);
            fecha.setHours(0, 0, 0, 0);

            const año = fecha.getFullYear();
            const mes = String(fecha.getMonth() + 1).padStart(2, '0');
            const dia = String(fecha.getDate()).padStart(2, '0');

            semana.push({
                fecha,
                nombreDia: nombres[(fecha.getDay() + 6) % 7],
                esFuturo: fecha > hoy,
                iso: `${año}-${mes}-${dia}`,
                estadoDia: null,
                caloriasConsumidas: null,
                objetivoCalorico: null
            });
        }

        return semana;
    }
}
