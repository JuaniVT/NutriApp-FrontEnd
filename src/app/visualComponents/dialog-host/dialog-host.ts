import { Component, ElementRef, ViewChild, effect, signal } from '@angular/core';
import { DialogService } from '../../service/dialog';

@Component({
  selector: 'app-dialog-host',
  imports: [],
  templateUrl: './dialog-host.html',
  styleUrl: './dialog-host.css',
})
export class DialogHost {
  @ViewChild('promptInput') promptInputRef?: ElementRef<HTMLInputElement>;

  promptValue = signal('');

  constructor(readonly dialog: DialogService) {
    //cada vez que se abre una modal de tipo prompt reseteamos el valor del input al default
    effect(() => {
      const req = this.dialog.request();
      if (req?.kind === 'prompt') {
        this.promptValue.set(req.defaultValue ?? '');
      }
    });
  }

  onBackdropClick() {
    this.cancel();
  }

  onEscape() {
    this.cancel();
  }

  accept() {
    const req = this.dialog.request();
    if (!req) return;

    if (req.kind === 'prompt') {
      this.dialog.resolve(this.promptValue().trim() ? this.promptValue() : null);
    } else if (req.kind === 'confirm') {
      this.dialog.resolve(true);
    } else {
      this.dialog.resolve(undefined);
    }
  }

  cancel() {
    const req = this.dialog.request();
    if (!req) return;

    //info/success/error solo tienen boton de aceptar, cancelar equivale a cerrarlas igual
    if (req.kind === 'prompt') {
      this.dialog.resolve(null);
    } else {
      this.dialog.resolve(false);
    }
  }

  onPromptInput(value: string) {
    this.promptValue.set(value);
  }
}
