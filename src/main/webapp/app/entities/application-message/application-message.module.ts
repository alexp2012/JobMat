import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobMatSharedModule } from 'app/shared/shared.module';
import { ApplicationMessageComponent } from './application-message.component';
import { ApplicationMessageDetailComponent } from './application-message-detail.component';
import { ApplicationMessageUpdateComponent } from './application-message-update.component';
import {
  ApplicationMessageDeletePopupComponent,
  ApplicationMessageDeleteDialogComponent
} from './application-message-delete-dialog.component';
import { applicationMessageRoute, applicationMessagePopupRoute } from './application-message.route';

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
  entryComponents: [ApplicationMessageDeleteDialogComponent]
})
export class JobMatApplicationMessageModule {}
