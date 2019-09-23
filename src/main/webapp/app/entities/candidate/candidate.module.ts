import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobMatSharedModule } from 'app/shared/shared.module';
import { CandidateComponent } from './candidate.component';
import { CandidateDetailComponent } from './candidate-detail.component';
import { CandidateUpdateComponent } from './candidate-update.component';
import { CandidateDeletePopupComponent, CandidateDeleteDialogComponent } from './candidate-delete-dialog.component';
import { candidateRoute, candidatePopupRoute } from './candidate.route';

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
  entryComponents: [CandidateDeleteDialogComponent]
})
export class JobMatCandidateModule {}
