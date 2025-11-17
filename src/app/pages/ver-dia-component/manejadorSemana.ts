
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})

export class ManejadorSemana {

    generarSemana(fechaReferencia: Date) {
        const semana: any[] = [];       //semana con 7 objetos que van a ser dias
        const inicio = new Date(fechaReferencia);


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
            fecha.setDate(inicio.getDate() + i);   // Ajusta fecha al día correcto sumando i días al inicio

            semana.push({
                fecha,
                nombreDia: nombres[(fecha.getDay() + 6) % 7], // nombre del día en escala lunes-domingo
                esFuturo: fecha > hoy,                       // true/false
                iso: fecha.toISOString().split('T')[0]       //manda la fehca un    string ISO YYYY-MM-DD
            });
        }

        return semana;
    }
}
