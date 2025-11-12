import { Routes } from '@angular/router';
import { InicioSesionComponent } from './inicio-sesion-component/inicio-sesion-component';
import { MiPerfilComponente } from './mi-perfil-componente/mi-perfil-componente';
import { NotFound } from './not-found/not-found';
import { Home } from './home/home';

export const routes: Routes = [
    {path: '', redirectTo: 'app-home', pathMatch: 'full'},     //ruta por defecto
    {path: "home", title: "Home", component: Home},
    {path: 'login', component: InicioSesionComponent},
    {path: "perfil", title: "Perfil" , component: MiPerfilComponente},


    // !!IMPORTANTE¡¡ -> esta ruta tiene que estar al final de todas ya que sino, desde este componente las que estan abajo no te redireccionan
    {path: "**", title: "Not Found 404", component: NotFound}      //ruta de 404 (pagina no encontrada)
];
