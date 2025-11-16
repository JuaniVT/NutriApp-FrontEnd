import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './visualComponents/header/header';
import { Footer } from './visualComponents/footer/footer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('NutriApp_Frontend');
}
