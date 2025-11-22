import { AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { debounceTime, first, switchMap } from 'rxjs';
import { AuthService } from '../service/auth';

export class CustomAsyncValidators {

  static usernameExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) => {
      return control.valueChanges.pipe(
        debounceTime(300),
        switchMap(value => validationService.checkUsername(value)),
        switchMap(disponible => disponible ? [null] : [{ usernameTaken: true }]),
        first()
      );
    };
  }

  static emailExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) => {
      return control.valueChanges.pipe(
        debounceTime(300),
        switchMap(value => validationService.checkEmail(value)),
        switchMap(disponible => disponible ? [null] : [{ emailTaken: true }]),
        first()
      );
    };
  }

  static dniExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) => {
      return control.valueChanges.pipe(
        debounceTime(300),
        switchMap(value => validationService.checkDni(value)),
        switchMap(disponible => disponible ? [null] : [{ dniTaken: true }]),
        first()
      );
    };
  }

  static telefonoExists(validationService: AuthService): AsyncValidatorFn {
    return (control: AbstractControl) => {
      return control.valueChanges.pipe(
        debounceTime(300),
        switchMap(value => validationService.checkTelefono(value)),
        switchMap(disponible => disponible ? [null] : [{ telefonoTaken: true }]),
        first()
      );
    };
  }
}