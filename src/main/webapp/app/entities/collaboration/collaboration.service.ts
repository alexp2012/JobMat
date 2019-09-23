import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICollaboration } from 'app/shared/model/collaboration.model';

type EntityResponseType = HttpResponse<ICollaboration>;
type EntityArrayResponseType = HttpResponse<ICollaboration[]>;

@Injectable({ providedIn: 'root' })
export class CollaborationService {
  public resourceUrl = SERVER_API_URL + 'api/collaborations';

  constructor(protected http: HttpClient) {}

  create(collaboration: ICollaboration): Observable<EntityResponseType> {
    return this.http.post<ICollaboration>(this.resourceUrl, collaboration, { observe: 'response' });
  }

  update(collaboration: ICollaboration): Observable<EntityResponseType> {
    return this.http.put<ICollaboration>(this.resourceUrl, collaboration, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICollaboration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICollaboration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
