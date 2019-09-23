import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BusinessInterest } from 'app/shared/model/business-interest.model';
import { BusinessInterestService } from './business-interest.service';
import { BusinessInterestComponent } from './business-interest.component';
import { BusinessInterestDetailComponent } from './business-interest-detail.component';
import { BusinessInterestUpdateComponent } from './business-interest-update.component';
import { BusinessInterestDeletePopupComponent } from './business-interest-delete-dialog.component';
import { IBusinessInterest } from 'app/shared/model/business-interest.model';

@Injectable({ providedIn: 'root' })
export class BusinessInterestResolve implements Resolve<IBusinessInterest> {
  constructor(private service: BusinessInterestService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBusinessInterest> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<BusinessInterest>) => response.ok),
        map((businessInterest: HttpResponse<BusinessInterest>) => businessInterest.body)
      );
    }
    return of(new BusinessInterest());
  }
}

export const businessInterestRoute: Routes = [
  {
    path: '',
    component: BusinessInterestComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jobMatApp.businessInterest.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BusinessInterestDetailComponent,
    resolve: {
      businessInterest: BusinessInterestResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.businessInterest.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BusinessInterestUpdateComponent,
    resolve: {
      businessInterest: BusinessInterestResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.businessInterest.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BusinessInterestUpdateComponent,
    resolve: {
      businessInterest: BusinessInterestResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.businessInterest.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const businessInterestPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BusinessInterestDeletePopupComponent,
    resolve: {
      businessInterest: BusinessInterestResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.businessInterest.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
