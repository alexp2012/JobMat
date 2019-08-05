import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOpening } from 'app/shared/model/opening.model';

type EntityResponseType = HttpResponse<IOpening>;
type EntityArrayResponseType = HttpResponse<IOpening[]>;

@Injectable({ providedIn: 'root' })
export class OpeningService {
  public resourceUrl = SERVER_API_URL + 'api/openings';

  constructor(protected http: HttpClient) {}

  create(opening: IOpening): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opening);
    return this.http
      .post<IOpening>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(opening: IOpening): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opening);
    return this.http
      .put<IOpening>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOpening>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOpening[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(opening: IOpening): IOpening {
    const copy: IOpening = Object.assign({}, opening, {
      date: opening.date != null && opening.date.isValid() ? opening.date.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((opening: IOpening) => {
        opening.date = opening.date != null ? moment(opening.date) : null;
      });
    }
    return res;
  }
}
