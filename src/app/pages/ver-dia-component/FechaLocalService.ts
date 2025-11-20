import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })

export class FechaLocalService {


    // Transforma una date a una date Local
    toLocalDate(iso: string): Date {
        const [y, m, d] = iso.split('-').map(Number);
        const fecha = new Date(y, m - 1, d);
        fecha.setHours(0, 0, 0, 0);
        return fecha;
    }


    // Trasnforma una date a un string iso 
    toIsoDate(date: Date): string {
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, '0');
        const d = String(date.getDate()).padStart(2, '0');
        return `${y}-${m}-${d}`;
    }


    // Añade dias teniendo en cuenta su zona horaria y parseo
    addDays(date: Date, days: number): Date {
        const nueva = new Date(date);
        nueva.setDate(nueva.getDate() + days);
        nueva.setHours(0, 0, 0, 0);
        return nueva;
    }



}