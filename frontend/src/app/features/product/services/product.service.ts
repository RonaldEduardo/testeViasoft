import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly API = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}
  create(product: Product): Observable<HttpResponse<Product>> {
    return this.http.post<Product>(`${this.API}/create`, product, {
      observe: 'response',
    });
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API}`);
  }

  findById(id: number): Observable<Product> {
    const url = `${this.API}/${id}`;
    return this.http.get<Product>(url);
  }

  update(id: number, product: Product): Observable<Product> {
    const url = `${this.API}/${id}`;
    return this.http.put<Product>(url, product);
  }

  delete(id: number): Observable<void> {
    const url = `${this.API}/${id}`;
    return this.http.delete<void>(url);
  }
}
