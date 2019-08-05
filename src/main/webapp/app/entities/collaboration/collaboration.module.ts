import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JobMatSharedModule } from 'app/shared';
import {
  CollaborationComponent,
  CollaborationDetailComponent,
  CollaborationUpdateComponent,
  CollaborationDeletePopupComponent,
  CollaborationDeleteDialogComponent,
  collaborationRoute,
  collaborationPopupRoute
} from './';

const ENTITY_STATES = [...collaborationRoute, ...collaborationPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CollaborationComponent,
    CollaborationDetailComponent,
    CollaborationUpdateComponent,
    CollaborationDeleteDialogComponent,
    CollaborationDeletePopupComponent
  ],
  entryComponents: [
    CollaborationComponent,
    CollaborationUpdateComponent,
    CollaborationDeleteDialogComponent,
    CollaborationDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatCollaborationModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
