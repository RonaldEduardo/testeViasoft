import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { SaleCreateDTO } from '../dto/sale-create.dto';

@Injectable({
  providedIn: 'root',
})
export class SaleService {
  private readonly API = `${environment.apiUrl}/sales`;

  constructor(private http: HttpClient) {}

  createSale(payload: SaleCreateDTO): Observable<void> {
    return this.http.post<void>(this.API, payload);
  }
}
