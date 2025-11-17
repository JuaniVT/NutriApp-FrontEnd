    import { Routes } from '@angular/router';
    import { Home } from './pages/home/home';
    import { InicioSesionComponent } from './pages/inicio-sesion-component/inicio-sesion-component';
    import { MiPerfilComponente } from './pages/mi-perfil-componente/mi-perfil-componente';
    import { RegistroComponent } from './pages/registro-component/registro-component';
    import { VerDiaComponent } from './pages/ver-dia-component/ver-dia-component';
    import { NotFound } from './pages/not-found/not-found';
    import { ComidasFavoritasComponent } from './pages/comidas-favoritas/comidas-favoritas';


export const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},     //ruta por defecto
    {path: "home", title: "Home", component: Home},
    {path: 'login', component: InicioSesionComponent},
    {path: "perfil", title: "Perfil" , component: MiPerfilComponente},
    {path: 'registro', component: RegistroComponent},
    {path: 'dia/:fecha', component: VerDiaComponent},
    {path: 'favoritas', component: ComidasFavoritasComponent},


        // !!IMPORTANTE¡¡ -> esta ruta tiene que estar al final de todas ya que sino, desde este componente las que estan abajo no te redireccionan
        {path: "**", title: "Not Found 404", component: NotFound}      //ruta de 404 (pagina no encontrada)
    ];
