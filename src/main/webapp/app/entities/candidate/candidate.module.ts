import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  CandidateComponent,
  CandidateDetailComponent,
  CandidateUpdateComponent,
  CandidateDeletePopupComponent,
  CandidateDeleteDialogComponent,
  candidateRoute,
  candidatePopupRoute
} from './';

const ENTITY_STATES = [...candidateRoute, ...candidatePopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CandidateComponent,
    CandidateDetailComponent,
    CandidateUpdateComponent,
    CandidateDeleteDialogComponent,
    CandidateDeletePopupComponent
  ],
  entryComponents: [CandidateComponent, CandidateUpdateComponent, CandidateDeleteDialogComponent, CandidateDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatCandidateModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
