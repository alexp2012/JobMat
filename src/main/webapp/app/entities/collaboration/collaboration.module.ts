import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobMatSharedModule } from 'app/shared/shared.module';
import { CollaborationComponent } from './collaboration.component';
import { CollaborationDetailComponent } from './collaboration-detail.component';
import { CollaborationUpdateComponent } from './collaboration-update.component';
import { CollaborationDeletePopupComponent, CollaborationDeleteDialogComponent } from './collaboration-delete-dialog.component';
import { collaborationRoute, collaborationPopupRoute } from './collaboration.route';

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
  entryComponents: [CollaborationDeleteDialogComponent]
})
export class JobMatCollaborationModule {}
