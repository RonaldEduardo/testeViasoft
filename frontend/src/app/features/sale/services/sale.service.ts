import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { SaleCreateDTO } from '../dto/sale-create.dto';
import { SaleResponseDTO } from '../dto/sale-response.dto';
import { SaleItemDTO } from '../dto/sale-item.dto';
import { SaleItemResponseDTO } from '../dto/sale-item-response.dto';

@Injectable({
  providedIn: 'root',
})
export class SaleService {
  private readonly API = `${environment.apiUrl}/sales`;

  constructor(private http: HttpClient) { }

  createSale(payload: SaleCreateDTO): Observable<SaleResponseDTO> {
    return this.http.post<SaleResponseDTO>(`${this.API}/create`, payload);
  }


  getAllSales(): Observable<SaleResponseDTO[]> {
    return this.http.get<SaleResponseDTO[]>(`${this.API}/all`);
  }

  getSaleById(id: number): Observable<SaleResponseDTO> {
    return this.http.get<SaleResponseDTO>(`${this.API}/${id}`);
  }

  calculateItem(payload: { productId: number, quantity: number }): Observable<SaleItemResponseDTO> {
    return this.http.post<SaleItemResponseDTO>(`${this.API}/calculate-item`, payload);
  }

}
