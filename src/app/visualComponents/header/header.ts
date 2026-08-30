import { Component, computed, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

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

  redirectHome (){
    this.router.navigateByUrl('/dia/' + this.fechaHoy);
  }

  logOut (){
    this.auth.clearToken();
  }

  toggleMenu() {
    const menu = document.getElementById("menuMobile") as any;
    menu.togglePopover();
  }

   


}
