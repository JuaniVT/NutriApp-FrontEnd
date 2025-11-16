import { Routes } from '@angular/router';
import { InicioSesionComponent } from './inicio-sesion-component/inicio-sesion-component';
import { MiPerfilComponente } from './mi-perfil-componente/mi-perfil-componente';
import { NotFound } from './not-found/not-found';
import { Home } from './home/home';
import { RegistroComponent } from './registro-component/registro-component';
import { VerDiaComponent } from './componentes/ver-dia-component/ver-dia-component';

export const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},     //ruta por defecto
    {path: "home", title: "Home", component: Home},
    {path: 'login', component: InicioSesionComponent},
    {path: "perfil", title: "Perfil" , component: MiPerfilComponente},
    {path: 'registro', component: RegistroComponent},
    {path: 'dia/:fecha', component: VerDiaComponent},


    // !!IMPORTANTE¡¡ -> esta ruta tiene que estar al final de todas ya que sino, desde este componente las que estan abajo no te redireccionan
    {path: "**", title: "Not Found 404", component: NotFound}      //ruta de 404 (pagina no encontrada)
];
