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

  getProducers(): Observable<Producer[]> {
    return this.http.get<Producer[]>(`${this.API}/all`);
  }
}
