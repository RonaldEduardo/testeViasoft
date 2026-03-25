import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { onlyDigits } from '../utils/cnpj';

export function cnpjDigitsLengthValidator(min = 3, max = 14): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const rawValue = String(control.value ?? '');
    const digits = onlyDigits(rawValue);

    if (!digits) {
      return null;
    }

    if (digits.length < min || digits.length > max) {
      return {
        cnpjDigitsLength: {
          min,
          max,
          actual: digits.length,
        },
      };
    }

    return null;
  };
}
