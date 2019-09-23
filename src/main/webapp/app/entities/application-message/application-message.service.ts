import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IApplicationMessage } from 'app/shared/model/application-message.model';

type EntityResponseType = HttpResponse<IApplicationMessage>;
type EntityArrayResponseType = HttpResponse<IApplicationMessage[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationMessageService {
  public resourceUrl = SERVER_API_URL + 'api/application-messages';

  constructor(protected http: HttpClient) {}

  create(applicationMessage: IApplicationMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicationMessage);
    return this.http
      .post<IApplicationMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(applicationMessage: IApplicationMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicationMessage);
    return this.http
      .put<IApplicationMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApplicationMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApplicationMessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(applicationMessage: IApplicationMessage): IApplicationMessage {
    const copy: IApplicationMessage = Object.assign({}, applicationMessage, {
      date: applicationMessage.date != null && applicationMessage.date.isValid() ? applicationMessage.date.toJSON() : null
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
      res.body.forEach((applicationMessage: IApplicationMessage) => {
        applicationMessage.date = applicationMessage.date != null ? moment(applicationMessage.date) : null;
      });
    }
    return res;
  }
}
