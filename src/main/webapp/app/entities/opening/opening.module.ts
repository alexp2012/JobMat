import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobMatSharedModule } from 'app/shared/shared.module';
import { OpeningComponent } from './opening.component';
import { OpeningDetailComponent } from './opening-detail.component';
import { OpeningUpdateComponent } from './opening-update.component';
import { OpeningDeletePopupComponent, OpeningDeleteDialogComponent } from './opening-delete-dialog.component';
import { openingRoute, openingPopupRoute } from './opening.route';

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
  entryComponents: [OpeningDeleteDialogComponent]
})
export class JobMatOpeningModule {}
