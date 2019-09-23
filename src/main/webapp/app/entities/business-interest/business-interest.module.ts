import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobMatSharedModule } from 'app/shared/shared.module';
import { BusinessInterestComponent } from './business-interest.component';
import { BusinessInterestDetailComponent } from './business-interest-detail.component';
import { BusinessInterestUpdateComponent } from './business-interest-update.component';
import { BusinessInterestDeletePopupComponent, BusinessInterestDeleteDialogComponent } from './business-interest-delete-dialog.component';
import { businessInterestRoute, businessInterestPopupRoute } from './business-interest.route';

const ENTITY_STATES = [...businessInterestRoute, ...businessInterestPopupRoute];

@NgModule({
  imports: [JobMatSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BusinessInterestComponent,
    BusinessInterestDetailComponent,
    BusinessInterestUpdateComponent,
    BusinessInterestDeleteDialogComponent,
    BusinessInterestDeletePopupComponent
  ],
  entryComponents: [BusinessInterestDeleteDialogComponent]
})
export class JobMatBusinessInterestModule {}
