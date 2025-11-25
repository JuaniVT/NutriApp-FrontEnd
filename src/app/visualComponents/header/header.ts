import { InvokeFunctionExpr } from '@angular/compiler';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth';
import { toSignal } from '@angular/core/rxjs-interop';

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
    this.router.navigateByUrl("/home");
  }

  logOut (){
    this.auth.clearToken();
  }

  toggleMenu() {
    const menu = document.getElementById("menuMobile") as any;
    menu.togglePopover();
  }

   


}
