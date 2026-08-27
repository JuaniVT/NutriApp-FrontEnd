import { InvokeFunctionExpr, TaggedTemplateLiteral } from '@angular/compiler';
import { Component, computed, HostListener, inject, OnInit, signal} from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth';
import { toSignal } from '@angular/core/rxjs-interop';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit{
  
  private readonly router = inject(Router)
  protected readonly auth = inject(AuthService);
  fechaHoy = new Date().toLocaleDateString('en-CA');
  
  
  //signal que depende de la signal del rol que se setea cuando el usuario se logea o se deslogea
  protected readonly role = computed(() => this.auth.roleSignal());
  
  
  irASeccion(id: string) {
    const element = document.getElementById(id);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  ngOnInit(): void {
    
  }
  
  redirectHome (){
    this.router.navigateByUrl('/dia/' + this.fechaHoy);
  }
  
  logOut (){
    this.auth.clearToken();
  }
  
  //metodo para abrir o cerrar el menu del perfil
  togglePerfil(){
    const menuPerfil = document.getElementById("menuPerfil") as HTMLElement;
    const menuMobile = document.getElementById("menuMobile") as HTMLElement;
    
    //escondemos el otro menu
    menuMobile.hidePopover();
    
    //toggleamos el que si queremos
    menuPerfil.togglePopover();
  }

  //metodo para abrir o cerrar el menu del desplegable en mobile
  toggleMobile(){
    const menuPerfil = document.getElementById("menuPerfil") as HTMLElement;
    const menuMobile = document.getElementById("menuMobile") as HTMLElement;
  
    //escondemos el otro menu
    menuPerfil.hidePopover();
    
    //toggleamos el que si queremos
    menuMobile.togglePopover();
  }
  
  //decorador para el metodo que se ejecuta cuando en el documento ocurre determinado evento (en este caso es el 'click')
  //y tambien cuando se ejecuta el metodo se pasa el elvento por parametro con el '$event' y se recibe en los parametros del metodo
  @HostListener ('document:click', ['$event'])
  cerrarMenus(event: MouseEvent){

    //guardamos donde se hizo el click
    const target = event.target as HTMLElement;

    const menuPerfil = document.getElementById("menuPerfil") as HTMLElement;
    const menuMobile = document.getElementById("menuMobile") as HTMLElement; 

    const botonPerfil = document.getElementById("botonPerfil") as HTMLElement;
    const botonMobile = document.getElementById("hamburgerMenu") as HTMLElement;

    //si clickeo en el menu del perfil o el del mobile, no hacemos nada
    if(menuPerfil?.contains(target) || menuMobile.contains(target)){
      return;
    }

    //si clickeo en el boton del perfil o el del mobile, no hacemos nada
    if(botonPerfil?.contains(target) || botonMobile.contains(target)){
      return;
    }

    
    //si clickeo afuera de cualquier menu, cerramos los dos
    menuPerfil.hidePopover();
    menuMobile.hidePopover();
  }
}
