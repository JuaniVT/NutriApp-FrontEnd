import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './visualComponents/header/header';
import { Footer } from './visualComponents/footer/footer';
import { NotificacionLogroVentana } from "./visualComponents/notificacion-logro-ventana/notificacion-logro-ventana";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer, NotificacionLogroVentana],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('NutriApp_Frontend');
}
