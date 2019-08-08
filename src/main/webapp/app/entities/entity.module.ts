import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.JobMatCityModule)
      },
      {
        path: 'business-interest',
        loadChildren: () => import('./business-interest/business-interest.module').then(m => m.JobMatBusinessInterestModule)
      },
      {
        path: 'company',
        loadChildren: () => import('./company/company.module').then(m => m.JobMatCompanyModule)
      },
      {
        path: 'candidate',
        loadChildren: () => import('./candidate/candidate.module').then(m => m.JobMatCandidateModule)
      },
      {
        path: 'opening',
        loadChildren: () => import('./opening/opening.module').then(m => m.JobMatOpeningModule)
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.JobMatTagModule)
      },
      {
        path: 'recruitment-step',
        loadChildren: () => import('./recruitment-step/recruitment-step.module').then(m => m.JobMatRecruitmentStepModule)
      },
      {
        path: 'collaboration',
        loadChildren: () => import('./collaboration/collaboration.module').then(m => m.JobMatCollaborationModule)
      },
      {
        path: 'application-message',
        loadChildren: () => import('./application-message/application-message.module').then(m => m.JobMatApplicationMessageModule)
      },
      {
        path: 'application',
        loadChildren: () => import('./application/application.module').then(m => m.JobMatApplicationModule)
      },
      {
        path: 'extended-user',
        loadChildren: () => import('./extended-user/extended-user.module').then(m => m.JobMatExtendedUserModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatEntityModule {}
