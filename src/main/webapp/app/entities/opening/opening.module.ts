import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  OpeningComponent,
  OpeningDetailComponent,
  OpeningUpdateComponent,
  OpeningDeletePopupComponent,
  OpeningDeleteDialogComponent,
  openingRoute,
  openingPopupRoute
} from './';

const ENTITY_STATES = [...openingRoute, ...openingPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    OpeningComponent,
    OpeningDetailComponent,
    OpeningUpdateComponent,
    OpeningDeleteDialogComponent,
    OpeningDeletePopupComponent
  ],
  entryComponents: [OpeningComponent, OpeningUpdateComponent, OpeningDeleteDialogComponent, OpeningDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatOpeningModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
