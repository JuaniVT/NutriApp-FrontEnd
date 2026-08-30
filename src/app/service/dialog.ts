import { Injectable, signal } from '@angular/core';

export type DialogKind = 'info' | 'success' | 'error' | 'confirm' | 'prompt';

export interface DialogRequest {
  kind: DialogKind;
  message: string;
  title?: string;
  confirmText?: string;
  cancelText?: string;
  danger?: boolean;
  defaultValue?: string;
}

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  private resolver: ((value: any) => void) | null = null;

  readonly request = signal<DialogRequest | null>(null);

  info(message: string, title?: string): Promise<void> {
    return this.open({ kind: 'info', message, title });
  }

  success(message: string, title?: string): Promise<void> {
    return this.open({ kind: 'success', message, title });
  }

  error(message: string, title?: string): Promise<void> {
    return this.open({ kind: 'error', message, title });
  }

  confirm(message: string, opts?: Partial<DialogRequest>): Promise<boolean> {
    return this.open({ kind: 'confirm', message, ...opts });
  }

  prompt(message: string, defaultValue = ''): Promise<string | null> {
    return this.open({ kind: 'prompt', message, defaultValue });
  }

  //llamado por el DialogHost cuando el usuario responde (aceptar, cancelar, cerrar con Escape o click afuera)
  resolve(value: any) {
    const resolver = this.resolver;
    this.resolver = null;
    this.request.set(null);
    resolver?.(value);
  }

  private open(req: DialogRequest): Promise<any> {
    return new Promise((resolve) => {
      //si ya habia una modal abierta sin resolver la cerramos como cancelada antes de abrir la nueva
      this.resolver?.(req.kind === 'prompt' ? null : false);
      this.resolver = resolve;
      this.request.set(req);
    });
  }
}
