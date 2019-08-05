import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Application } from 'app/shared/model/application.model';
import { ApplicationService } from './application.service';
import { ApplicationComponent } from './application.component';
import { ApplicationDetailComponent } from './application-detail.component';
import { ApplicationUpdateComponent } from './application-update.component';
import { ApplicationDeletePopupComponent } from './application-delete-dialog.component';
import { IApplication } from 'app/shared/model/application.model';

@Injectable({ providedIn: 'root' })
export class ApplicationResolve implements Resolve<IApplication> {
  constructor(private service: ApplicationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IApplication> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Application>) => response.ok),
        map((application: HttpResponse<Application>) => application.body)
      );
    }
    return of(new Application());
  }
}

export const applicationRoute: Routes = [
  {
    path: '',
    component: ApplicationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jobMatApp.application.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ApplicationDetailComponent,
    resolve: {
      application: ApplicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.application.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ApplicationUpdateComponent,
    resolve: {
      application: ApplicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.application.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ApplicationUpdateComponent,
    resolve: {
      application: ApplicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.application.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const applicationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ApplicationDeletePopupComponent,
    resolve: {
      application: ApplicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.application.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
