import { Component, inject } from '@angular/core';
import { ProfileService } from '../../service/profile';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-mi-perfil',
  imports: [],
  templateUrl: './mi-perfil-componente.html',
  styleUrl: './mi-perfil-componente.css',
})
export class MiPerfilComponente {

  private readonly http = inject(ProfileService);
  protected readonly personProfile = toSignal(this.http.getPersonProfile());

  

}
