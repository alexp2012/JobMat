import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBusinessInterest } from 'app/shared/model/business-interest.model';

type EntityResponseType = HttpResponse<IBusinessInterest>;
type EntityArrayResponseType = HttpResponse<IBusinessInterest[]>;

@Injectable({ providedIn: 'root' })
export class BusinessInterestService {
  public resourceUrl = SERVER_API_URL + 'api/business-interests';

  constructor(protected http: HttpClient) {}

  create(businessInterest: IBusinessInterest): Observable<EntityResponseType> {
    return this.http.post<IBusinessInterest>(this.resourceUrl, businessInterest, { observe: 'response' });
  }

  update(businessInterest: IBusinessInterest): Observable<EntityResponseType> {
    return this.http.put<IBusinessInterest>(this.resourceUrl, businessInterest, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusinessInterest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusinessInterest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
