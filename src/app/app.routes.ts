    import { Routes } from '@angular/router';
    import { Home } from './pages/home/home';
    import { InicioSesionComponent } from './pages/inicio-sesion-component/inicio-sesion-component';
    import { MiPerfilComponente } from './pages/mi-perfil-componente/mi-perfil-componente';
    import { RegistroComponent } from './pages/registro-component/registro-component';
    import { VerDiaComponent } from './pages/ver-dia-component/ver-dia-component';
    import { NotFound } from './pages/not-found/not-found';
    import { ComidasFavoritasComponent } from './pages/comidas-favoritas/comidas-favoritas';
    import { ListarSolicitudes } from './pages/solicitudes/listar-solicitudes/listar-solicitudes';
import { AlimentosBDD } from './pages/alimentos-nuestra-bdd/alimentos-bdd/alimentos-bdd';

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},     //ruta por defecto
    {path: "home", title: "Home", component: Home},
    {path: 'login', title: "Login", component: InicioSesionComponent},
    {path: "perfil", title: "Perfil" , component: MiPerfilComponente},
    {path: 'registro', title: "Registro", component: RegistroComponent},
    {path: 'dia/:fecha', title: "Dia", component: VerDiaComponent},
    {path: 'favoritas', title:"Comidas Favoritas", component: ComidasFavoritasComponent},
    {path: "listar-solicitudes/:mode", title: "Solicitudes", component: ListarSolicitudes},
    {path: "alimentos-bdd", title: "Alimentos BDD", component: AlimentosBDD},

        // !!IMPORTANTE¡¡ -> esta ruta tiene que estar al final de todas ya que sino, desde este componente las que estan abajo no te redireccionan
        {path: "**", title: "Not Found 404", component: NotFound}      //ruta de 404 (pagina no encontrada)
    ];
