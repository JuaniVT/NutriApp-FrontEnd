import { Routes } from '@angular/router';
import { InicioSesionComponent } from './inicio-sesion-component/inicio-sesion-component';
import { RegistroComponent } from './registro-component/registro-component';

export const routes: Routes = [{path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: InicioSesionComponent},
    {path: 'registro', component: RegistroComponent}
];
