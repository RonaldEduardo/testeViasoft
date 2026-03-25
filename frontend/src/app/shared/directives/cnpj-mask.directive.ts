import { Directive, ElementRef, HostListener, Optional } from '@angular/core';
import { NgControl } from '@angular/forms';
import { formatCnpj } from '../utils/cnpj';

@Directive({
  selector: '[appCnpjMask]',
})
export class CnpjMaskDirective {
  constructor(
    private elementRef: ElementRef<HTMLInputElement>,
    @Optional() private ngControl?: NgControl,
  ) {}

  @HostListener('input', ['$event'])
  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const formatted = formatCnpj(input.value);

    if (formatted !== input.value) {
      input.value = formatted;
    }

    if (this.ngControl?.control) {
      this.ngControl.control.setValue(formatted, { emitEvent: false });
    }
  }

  @HostListener('blur')
  onBlur(): void {
    const input = this.elementRef.nativeElement;
    const formatted = formatCnpj(input.value);

    if (formatted !== input.value) {
      input.value = formatted;
    }

    if (this.ngControl?.control) {
      this.ngControl.control.setValue(formatted, { emitEvent: false });
    }
  }
}
