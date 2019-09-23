import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobMatSharedModule } from 'app/shared/shared.module';
import { RecruitmentStepComponent } from './recruitment-step.component';
import { RecruitmentStepDetailComponent } from './recruitment-step-detail.component';
import { RecruitmentStepUpdateComponent } from './recruitment-step-update.component';
import { RecruitmentStepDeletePopupComponent, RecruitmentStepDeleteDialogComponent } from './recruitment-step-delete-dialog.component';
import { recruitmentStepRoute, recruitmentStepPopupRoute } from './recruitment-step.route';

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
  entryComponents: [RecruitmentStepDeleteDialogComponent]
})
export class JobMatRecruitmentStepModule {}
