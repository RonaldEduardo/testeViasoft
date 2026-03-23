import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';

import { SaleItemResponseDTO } from '../../dto/sale-item-response.dto';
import { SaleResponseDTO } from '../../dto/sale-response.dto';
import { Producer } from '../../models/producer';
import { ProducerService } from '../../services/producer.service';
import { SaleService } from '../../services/sale.service';

interface SaleViewModel {
  saleId: number | null;
  producerId: number;
  producerName: string;
  totalValue: number;
  itemCount: number;
  periodDate: Date | null;
  rawItems: SaleItemResponseDTO[];
}

@Component({
  selector: 'app-sales-report',
  templateUrl: './sales-report.component.html',
})
export class SalesReportComponent implements OnInit, OnDestroy {
  sales: SaleViewModel[] = [];
  producers: Producer[] = [];
  selectedProducerId = '';
  periodStart = '';
  periodEnd = '';
  isLoading = false;
  errorMessage = '';

  private readonly destroy$ = new Subject<void>();
  private readonly expandedSales = new Set<string>();

  constructor(
    private saleService: SaleService,
    private producerService: ProducerService,
  ) {}

  ngOnInit(): void {
    this.loadProducers();
    this.loadSales();
  }

  get filteredSales(): SaleViewModel[] {
    return this.sales.filter((sale) => {
      const matchesProducer =
        !this.selectedProducerId ||
        sale.producerId === Number(this.selectedProducerId);

      const matchesPeriod = this.matchesPeriodFilter(sale.periodDate);

      return matchesProducer && matchesPeriod;
    });
  }

  get totalFilteredValue(): number {
    return this.filteredSales.reduce((acc, sale) => acc + sale.totalValue, 0);
  }

  clearFilters(): void {
    this.selectedProducerId = '';
    this.periodStart = '';
    this.periodEnd = '';
  }

  toggleItems(sale: SaleViewModel, index: number): void {
    const key = this.saleKey(sale, index);

    if (this.expandedSales.has(key)) {
      this.expandedSales.delete(key);
      return;
    }

    this.expandedSales.add(key);
  }

  isExpanded(sale: SaleViewModel, index: number): boolean {
    return this.expandedSales.has(this.saleKey(sale, index));
  }

  getItemUnitPrice(item: SaleItemResponseDTO): number {
    if (item.priceAtTimeOfSale != null) {
      return item.priceAtTimeOfSale;
    }

    if (item.unitPrice != null) {
      return item.unitPrice;
    }

    if (item.quantity > 0) {
      return this.getItemTotal(item) / item.quantity;
    }

    return 0;
  }

  getItemTotal(item: SaleItemResponseDTO): number {
    if (item.total != null) {
      return item.total;
    }

    if (item.netTotal != null) {
      return item.netTotal;
    }

    if (item.grossTotal != null) {
      return item.grossTotal;
    }

    return this.getItemUnitPrice(item) * item.quantity;
  }

  formatSaleLabel(sale: SaleViewModel, index: number): string {
    if (sale.saleId != null) {
      return '#' + sale.saleId;
    }

    return '#' + (index + 1);
  }

  trackBySale(_index: number, sale: SaleViewModel): string {
    return this.saleKey(sale, _index);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadSales(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.saleService
      .getAllSales()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (sales) => {
          this.sales = sales.map((sale) => this.mapSale(sale));
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Nao foi possivel carregar as vendas.';
          this.isLoading = false;
        },
      });
  }

  private loadProducers(): void {
    this.producerService
      .getProducers()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (producers) => {
          this.producers = producers;
          this.sales = this.sales.map((sale) => this.refreshProducerName(sale));
        },
      });
  }

  private mapSale(sale: SaleResponseDTO): SaleViewModel {
    const resolvedItems = sale.items || [];
    const resolvedTotal = this.resolveTotal(sale, resolvedItems);
    const saleDate = this.resolveDate(sale);

    return {
      saleId: sale.id ?? sale.saleId ?? null,
      producerId: sale.producerId,
      producerName: this.resolveProducerName(
        sale.producerId,
        sale.producerName,
      ),
      totalValue: resolvedTotal,
      itemCount: resolvedItems.reduce((acc, item) => acc + item.quantity, 0),
      periodDate: saleDate,
      rawItems: resolvedItems,
    };
  }

  private resolveTotal(
    sale: SaleResponseDTO,
    items: SaleItemResponseDTO[],
  ): number {
    if (sale.totalValue != null) {
      return sale.totalValue;
    }

    if (sale.netTotal != null) {
      return sale.netTotal;
    }

    if (sale.grossTotal != null) {
      return sale.grossTotal;
    }

    return items.reduce((acc, item) => acc + this.getItemTotal(item), 0);
  }

  private resolveDate(sale: SaleResponseDTO): Date | null {
    const rawDate = sale.createdAt || sale.saleDate;

    if (!rawDate) {
      return null;
    }

    const parsed = new Date(rawDate);
    return Number.isNaN(parsed.getTime()) ? null : parsed;
  }

  private refreshProducerName(sale: SaleViewModel): SaleViewModel {
    return {
      ...sale,
      producerName: this.resolveProducerName(
        sale.producerId,
        sale.producerName,
      ),
    };
  }

  private resolveProducerName(producerId: number, fallback?: string): string {
    if (fallback) {
      return fallback;
    }

    const producer = this.producers.find((item) => item.id === producerId);
    return producer?.name || 'Produtor #' + producerId;
  }

  private matchesPeriodFilter(periodDate: Date | null): boolean {
    if (!this.periodStart && !this.periodEnd) {
      return true;
    }

    if (!periodDate) {
      return false;
    }

    if (this.periodStart) {
      const startDate = new Date(this.periodStart + 'T00:00:00');
      if (periodDate < startDate) {
        return false;
      }
    }

    if (this.periodEnd) {
      const endDate = new Date(this.periodEnd + 'T23:59:59.999');
      if (periodDate > endDate) {
        return false;
      }
    }

    return true;
  }

  private saleKey(sale: SaleViewModel, index: number): string {
    return sale.saleId != null ? 'sale-' + sale.saleId : 'idx-' + index;
  }
}
