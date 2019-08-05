import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Candidate } from 'app/shared/model/candidate.model';
import { CandidateService } from './candidate.service';
import { CandidateComponent } from './candidate.component';
import { CandidateDetailComponent } from './candidate-detail.component';
import { CandidateUpdateComponent } from './candidate-update.component';
import { CandidateDeletePopupComponent } from './candidate-delete-dialog.component';
import { ICandidate } from 'app/shared/model/candidate.model';

@Injectable({ providedIn: 'root' })
export class CandidateResolve implements Resolve<ICandidate> {
  constructor(private service: CandidateService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICandidate> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Candidate>) => response.ok),
        map((candidate: HttpResponse<Candidate>) => candidate.body)
      );
    }
    return of(new Candidate());
  }
}

export const candidateRoute: Routes = [
  {
    path: '',
    component: CandidateComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jobMatApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CandidateDetailComponent,
    resolve: {
      candidate: CandidateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CandidateUpdateComponent,
    resolve: {
      candidate: CandidateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CandidateUpdateComponent,
    resolve: {
      candidate: CandidateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const candidatePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CandidateDeletePopupComponent,
    resolve: {
      candidate: CandidateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
