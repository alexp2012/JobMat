import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  CompanyComponent,
  CompanyDetailComponent,
  CompanyUpdateComponent,
  CompanyDeletePopupComponent,
  CompanyDeleteDialogComponent,
  companyRoute,
  companyPopupRoute
} from './';

const ENTITY_STATES = [...companyRoute, ...companyPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CompanyComponent,
    CompanyDetailComponent,
    CompanyUpdateComponent,
    CompanyDeleteDialogComponent,
    CompanyDeletePopupComponent
  ],
  entryComponents: [CompanyComponent, CompanyUpdateComponent, CompanyDeleteDialogComponent, CompanyDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatCompanyModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
