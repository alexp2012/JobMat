import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Opening } from 'app/shared/model/opening.model';
import { OpeningService } from './opening.service';
import { OpeningComponent } from './opening.component';
import { OpeningDetailComponent } from './opening-detail.component';
import { OpeningUpdateComponent } from './opening-update.component';
import { OpeningDeletePopupComponent } from './opening-delete-dialog.component';
import { IOpening } from 'app/shared/model/opening.model';

@Injectable({ providedIn: 'root' })
export class OpeningResolve implements Resolve<IOpening> {
  constructor(private service: OpeningService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOpening> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Opening>) => response.ok),
        map((opening: HttpResponse<Opening>) => opening.body)
      );
    }
    return of(new Opening());
  }
}

export const openingRoute: Routes = [
  {
    path: '',
    component: OpeningComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jobMatApp.opening.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: OpeningDetailComponent,
    resolve: {
      opening: OpeningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.opening.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: OpeningUpdateComponent,
    resolve: {
      opening: OpeningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.opening.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: OpeningUpdateComponent,
    resolve: {
      opening: OpeningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.opening.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const openingPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: OpeningDeletePopupComponent,
    resolve: {
      opening: OpeningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.opening.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
