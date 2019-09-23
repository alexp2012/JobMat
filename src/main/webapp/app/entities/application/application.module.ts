import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobMatSharedModule } from 'app/shared/shared.module';
import { ApplicationComponent } from './application.component';
import { ApplicationDetailComponent } from './application-detail.component';
import { ApplicationUpdateComponent } from './application-update.component';
import { ApplicationDeletePopupComponent, ApplicationDeleteDialogComponent } from './application-delete-dialog.component';
import { applicationRoute, applicationPopupRoute } from './application.route';

const ENTITY_STATES = [...applicationRoute, ...applicationPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ApplicationComponent,
    ApplicationDetailComponent,
    ApplicationUpdateComponent,
    ApplicationDeleteDialogComponent,
    ApplicationDeletePopupComponent
  ],
  entryComponents: [ApplicationDeleteDialogComponent]
})
export class JobMatApplicationModule {}
