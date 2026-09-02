import { AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import { Observable, of, timer } from 'rxjs';
import { first, map, switchMap } from 'rxjs';
import { AuthService } from '../service/auth';

export class CustomAsyncValidators {

  /**
   * Valida el valor ACTUAL del control: emite una vez y completa.
   *
   * Antes se suscribía a `control.valueChanges`, por lo que el validador
   * solo resolvía cuando el valor volvía a cambiar. Si el campo se
   * autocompletaba (ej: email desde Google) y quedaba readonly, nunca
   * volvía a emitir y el control quedaba en estado PENDING para siempre,
   * lo que hacía que `FormGroup.invalid` fuese `false` y se pudiera
   * avanzar de paso sin completar el resto del formulario.
   */
  private static check(
    control: AbstractControl,
    fn: (value: string) => Observable<boolean>,
    errorKey: string
  ): Observable<ValidationErrors | null> {
    const value = control.value;

    // El vacío lo maneja Validators.required, no el async.
    if (!value) {
      return of(null);
    }

    // timer(300) reemplaza al debounceTime: Angular cancela la ejecución
    // anterior del validador en cada cambio, así que no se acumulan llamadas.
    return timer(300).pipe(
      switchMap(() => fn(value)),
      map(disponible => (disponible ? null : { [errorKey]: true })),
      first()
    );
  }

  static usernameExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) =>
      CustomAsyncValidators.check(control, v => validationService.checkUsername(v), 'usernameTaken');
  }

  static emailExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) =>
      CustomAsyncValidators.check(control, v => validationService.checkEmail(v), 'emailTaken');
  }

  static dniExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) =>
      CustomAsyncValidators.check(control, v => validationService.checkDni(v), 'dniTaken');
  }

  static telefonoExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) =>
      CustomAsyncValidators.check(control, v => validationService.checkTelefono(v), 'telefonoTaken');
  }
}
