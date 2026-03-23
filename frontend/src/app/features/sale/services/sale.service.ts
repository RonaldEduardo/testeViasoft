import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { SaleCreateDTO } from '../dto/sale-create.dto';
import { SaleCreateResponseDTO } from '../dto/sale-create-response.dto';
import { SaleResponseDTO } from '../dto/sale-response.dto';

@Injectable({
  providedIn: 'root',
})
export class SaleService {
  private readonly API = `${environment.apiUrl}/sales`;

  constructor(private http: HttpClient) {}

  createSale(payload: SaleCreateDTO): Observable<SaleCreateResponseDTO | void> {
    return this.http.post<SaleCreateResponseDTO | void>(
      `${this.API}/create`,
      payload,
    );
  }

  getAllSales(): Observable<SaleResponseDTO[]> {
    return this.http.get<SaleResponseDTO[]>(`${this.API}/all`);
  }

  getSaleById(id: number): Observable<SaleResponseDTO> {
    return this.http.get<SaleResponseDTO>(`${this.API}/${id}`);
  }
}
