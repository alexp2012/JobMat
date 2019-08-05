import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  BusinessInterestComponent,
  BusinessInterestDetailComponent,
  BusinessInterestUpdateComponent,
  BusinessInterestDeletePopupComponent,
  BusinessInterestDeleteDialogComponent,
  businessInterestRoute,
  businessInterestPopupRoute
} from './';

const ENTITY_STATES = [...businessInterestRoute, ...businessInterestPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BusinessInterestComponent,
    BusinessInterestDetailComponent,
    BusinessInterestUpdateComponent,
    BusinessInterestDeleteDialogComponent,
    BusinessInterestDeletePopupComponent
  ],
  entryComponents: [
    BusinessInterestComponent,
    BusinessInterestUpdateComponent,
    BusinessInterestDeleteDialogComponent,
    BusinessInterestDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatBusinessInterestModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
