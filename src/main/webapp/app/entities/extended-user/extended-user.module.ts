import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  ExtendedUserComponent,
  ExtendedUserDetailComponent,
  ExtendedUserUpdateComponent,
  ExtendedUserDeletePopupComponent,
  ExtendedUserDeleteDialogComponent,
  extendedUserRoute,
  extendedUserPopupRoute
} from './';

const ENTITY_STATES = [...extendedUserRoute, ...extendedUserPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExtendedUserComponent,
    ExtendedUserDetailComponent,
    ExtendedUserUpdateComponent,
    ExtendedUserDeleteDialogComponent,
    ExtendedUserDeletePopupComponent
  ],
  entryComponents: [
    ExtendedUserComponent,
    ExtendedUserUpdateComponent,
    ExtendedUserDeleteDialogComponent,
    ExtendedUserDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatExtendedUserModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
