import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  ApplicationComponent,
  ApplicationDetailComponent,
  ApplicationUpdateComponent,
  ApplicationDeletePopupComponent,
  ApplicationDeleteDialogComponent,
  applicationRoute,
  applicationPopupRoute
} from './';

const ENTITY_STATES = [...applicationRoute, ...applicationPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ApplicationComponent,
    ApplicationDetailComponent,
    ApplicationUpdateComponent,
    ApplicationDeleteDialogComponent,
    ApplicationDeletePopupComponent
  ],
  entryComponents: [ApplicationComponent, ApplicationUpdateComponent, ApplicationDeleteDialogComponent, ApplicationDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatApplicationModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
