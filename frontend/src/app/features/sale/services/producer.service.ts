import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { Producer } from '../models/producer';

@Injectable({
  providedIn: 'root',
})
export class ProducerService {
  private readonly API = `${environment.apiUrl}/producers`;

  constructor(private http: HttpClient) {}

  create(producer: Producer): Observable<Producer> {
    return this.http.post<Producer>(`${this.API}/create`, producer);
  }

  getProducers(): Observable<Producer[]> {
    return this.http.get<Producer[]>(`${this.API}/all`);
  }

  findById(id: number): Observable<Producer> {
    return this.http.get<Producer>(`${this.API}/${id}`);
  }

  update(id: number, producer: Producer): Observable<Producer> {
    return this.http.put<Producer>(`${this.API}/${id}`, producer);
  }
}
