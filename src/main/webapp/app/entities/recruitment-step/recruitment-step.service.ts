import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRecruitmentStep } from 'app/shared/model/recruitment-step.model';

type EntityResponseType = HttpResponse<IRecruitmentStep>;
type EntityArrayResponseType = HttpResponse<IRecruitmentStep[]>;

@Injectable({ providedIn: 'root' })
export class RecruitmentStepService {
  public resourceUrl = SERVER_API_URL + 'api/recruitment-steps';

  constructor(protected http: HttpClient) {}

  create(recruitmentStep: IRecruitmentStep): Observable<EntityResponseType> {
    return this.http.post<IRecruitmentStep>(this.resourceUrl, recruitmentStep, { observe: 'response' });
  }

  update(recruitmentStep: IRecruitmentStep): Observable<EntityResponseType> {
    return this.http.put<IRecruitmentStep>(this.resourceUrl, recruitmentStep, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecruitmentStep>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecruitmentStep[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
