import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ApplicationMessage } from 'app/shared/model/application-message.model';
import { ApplicationMessageService } from './application-message.service';
import { ApplicationMessageComponent } from './application-message.component';
import { ApplicationMessageDetailComponent } from './application-message-detail.component';
import { ApplicationMessageUpdateComponent } from './application-message-update.component';
import { ApplicationMessageDeletePopupComponent } from './application-message-delete-dialog.component';
import { IApplicationMessage } from 'app/shared/model/application-message.model';

@Injectable({ providedIn: 'root' })
export class ApplicationMessageResolve implements Resolve<IApplicationMessage> {
  constructor(private service: ApplicationMessageService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IApplicationMessage> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ApplicationMessage>) => response.ok),
        map((applicationMessage: HttpResponse<ApplicationMessage>) => applicationMessage.body)
      );
    }
    return of(new ApplicationMessage());
  }
}

export const applicationMessageRoute: Routes = [
  {
    path: '',
    component: ApplicationMessageComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jobMatApp.applicationMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ApplicationMessageDetailComponent,
    resolve: {
      applicationMessage: ApplicationMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.applicationMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ApplicationMessageUpdateComponent,
    resolve: {
      applicationMessage: ApplicationMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.applicationMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ApplicationMessageUpdateComponent,
    resolve: {
      applicationMessage: ApplicationMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.applicationMessage.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const applicationMessagePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ApplicationMessageDeletePopupComponent,
    resolve: {
      applicationMessage: ApplicationMessageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jobMatApp.applicationMessage.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
