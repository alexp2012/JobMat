import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RecruitmentStep } from 'app/shared/model/recruitment-step.model';
import { RecruitmentStepService } from './recruitment-step.service';
import { RecruitmentStepComponent } from './recruitment-step.component';
import { RecruitmentStepDetailComponent } from './recruitment-step-detail.component';
import { RecruitmentStepUpdateComponent } from './recruitment-step-update.component';
import { RecruitmentStepDeletePopupComponent } from './recruitment-step-delete-dialog.component';
import { IRecruitmentStep } from 'app/shared/model/recruitment-step.model';

@Injectable({ providedIn: 'root' })
export class RecruitmentStepResolve implements Resolve<IRecruitmentStep> {
  constructor(private service: RecruitmentStepService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRecruitmentStep> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<RecruitmentStep>) => response.ok),
        map((recruitmentStep: HttpResponse<RecruitmentStep>) => recruitmentStep.body)
      );
    }
    return of(new RecruitmentStep());
  }
}

export const recruitmentStepRoute: Routes = [
  {
    path: '',
    component: RecruitmentStepComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jobMatApp.recruitmentStep.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RecruitmentStepDetailComponent,
    resolve: {
      recruitmentStep: RecruitmentStepResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.recruitmentStep.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RecruitmentStepUpdateComponent,
    resolve: {
      recruitmentStep: RecruitmentStepResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.recruitmentStep.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RecruitmentStepUpdateComponent,
    resolve: {
      recruitmentStep: RecruitmentStepResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.recruitmentStep.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const recruitmentStepPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RecruitmentStepDeletePopupComponent,
    resolve: {
      recruitmentStep: RecruitmentStepResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.recruitmentStep.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
