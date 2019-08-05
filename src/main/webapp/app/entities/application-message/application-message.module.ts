import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  ApplicationMessageComponent,
  ApplicationMessageDetailComponent,
  ApplicationMessageUpdateComponent,
  ApplicationMessageDeletePopupComponent,
  ApplicationMessageDeleteDialogComponent,
  applicationMessageRoute,
  applicationMessagePopupRoute
} from './';

const ENTITY_STATES = [...applicationMessageRoute, ...applicationMessagePopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ApplicationMessageComponent,
    ApplicationMessageDetailComponent,
    ApplicationMessageUpdateComponent,
    ApplicationMessageDeleteDialogComponent,
    ApplicationMessageDeletePopupComponent
  ],
  entryComponents: [
    ApplicationMessageComponent,
    ApplicationMessageUpdateComponent,
    ApplicationMessageDeleteDialogComponent,
    ApplicationMessageDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatApplicationMessageModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
