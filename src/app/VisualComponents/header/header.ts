import { InvokeFunctionExpr } from '@angular/compiler';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit{
  private readonly router = inject(Router)
  protected readonly auth = inject(AuthService);
  
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
