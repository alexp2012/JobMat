import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  RecruitmentStepComponent,
  RecruitmentStepDetailComponent,
  RecruitmentStepUpdateComponent,
  RecruitmentStepDeletePopupComponent,
  RecruitmentStepDeleteDialogComponent,
  recruitmentStepRoute,
  recruitmentStepPopupRoute
} from './';

const ENTITY_STATES = [...recruitmentStepRoute, ...recruitmentStepPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RecruitmentStepComponent,
    RecruitmentStepDetailComponent,
    RecruitmentStepUpdateComponent,
    RecruitmentStepDeleteDialogComponent,
    RecruitmentStepDeletePopupComponent
  ],
  entryComponents: [
    RecruitmentStepComponent,
    RecruitmentStepUpdateComponent,
    RecruitmentStepDeleteDialogComponent,
    RecruitmentStepDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatRecruitmentStepModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
