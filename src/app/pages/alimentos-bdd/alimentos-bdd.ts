import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { SolicitudService } from '../../service/solicitud';
import { AlimentosbddService } from '../../service/alimentosbdd';

@Component({
  selector: 'alimentos-bdd',
  imports: [],
  templateUrl: './alimentos-bdd.html',
  styleUrl: './alimentos-bdd.css',
})
export class AlimentosBDD {
  private readonly alimentosBddService = inject(AlimentosbddService);
  protected readonly alimentosFiltrados = toSignal(this.alimentosBddService.getAll());


}
