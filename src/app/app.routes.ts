import { Routes } from '@angular/router';
import { InicioSesionComponent } from './inicio-sesion-component/inicio-sesion-component';

export const routes: Routes = [{path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: InicioSesionComponent}
];
