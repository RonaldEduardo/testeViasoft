import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProducerService } from '../../services/producer.service';
import { Producer } from '../../models/producer';
import { cnpjDigitsLengthValidator } from '../../../../shared/validators/cnpj.validator';
import { onlyDigits } from '../../../../shared/utils/cnpj';

@Component({
  selector: 'app-producer-form',
  templateUrl: './producer-form.component.html',
})
export class ProducerFormComponent implements OnInit {
  producerForm!: FormGroup;
  producerId: number | null = null;
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private producerService: ProducerService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadProducerIfEditMode();
  }

  private buildForm(): void {
    this.producerForm = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100),
        ],
      ],
      creditLimit: [null, [Validators.required, Validators.min(0.01)]],
      cnpj: ['', [Validators.required, cnpjDigitsLengthValidator(3, 14)]],
    });
  }

  private loadProducerIfEditMode(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) return;

    const id = Number(idParam);
    if (Number.isNaN(id)) return;

    this.producerId = id;
    this.isEditMode = true;

    this.mapToForm(id);
  }

  private mapToForm(id: number): void {
    this.producerService.findById(id).subscribe((producer: Producer) => {
      this.producerForm.patchValue({
        name: producer.name,
        creditLimit: producer.creditLimit,
        cnpj: producer.cnpj,
      });
    });
  }

  onSubmit(): void {
    if (this.producerForm.invalid) {
      this.producerForm.markAllAsTouched();
      return;
    }

    const formValue = this.producerForm.getRawValue();
    const payload: Producer = {
      ...formValue,
      cnpj: onlyDigits(formValue.cnpj),
    };

    if (this.isEditMode && this.producerId !== null) {
      this.producerService.update(this.producerId, payload).subscribe(() => {
        this.router.navigate(['/checkout']);
      });
      return;
    }

    this.producerService.create(payload).subscribe(() => {
      this.router.navigate(['/checkout']);
    });
  }
}
