import { Component, OnInit } from '@angular/core';
import { Producer } from '../../models/producer';
import { ProducerService } from '../../services/producer.service';

@Component({
  selector: 'app-producer-list',
  templateUrl: './producer-list.component.html',
})
export class ProducerListComponent implements OnInit {
  producers: Producer[] = [];
  filtroNome: string = '';
  filtroCnpj: string = '';

  constructor(private producerService: ProducerService) {}

  ngOnInit(): void {
    this.producerService.getProducers().subscribe({
      next: (data) => (this.producers = data),
      error: (err) => console.error('Erro de conexao ou CORS:', err),
    });
  }

  get produtoresFiltrados() {
    const filtroNome = this.filtroNome.trim().toLowerCase();
    const filtroCnpj = this.filtroCnpj.trim().toLowerCase();
    const filtroCnpjDigits = this.filtroCnpj.replace(/\D/g, '');

    return this.producers.filter((producer) => {
      const matchNome =
        !filtroNome || producer.name.toLowerCase().includes(filtroNome);

      const producerCnpj = (producer.cnpj || '').toString();
      const producerCnpjLower = producerCnpj.toLowerCase();
      const producerCnpjDigits = producerCnpj.replace(/\D/g, '');

      const matchCnpj =
        !filtroCnpj ||
        producerCnpjLower.includes(filtroCnpj) ||
        (!!filtroCnpjDigits && producerCnpjDigits.includes(filtroCnpjDigits));

      return matchNome && matchCnpj;
    });
  }

  limparFiltros() {
    this.filtroNome = '';
    this.filtroCnpj = '';
  }
}
