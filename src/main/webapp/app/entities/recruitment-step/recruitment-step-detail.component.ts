import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecruitmentStep } from 'app/shared/model/recruitment-step.model';

@Component({
  selector: 'jhi-recruitment-step-detail',
  templateUrl: './recruitment-step-detail.component.html'
})
export class RecruitmentStepDetailComponent implements OnInit {
  recruitmentStep: IRecruitmentStep;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ recruitmentStep }) => {
      this.recruitmentStep = recruitmentStep;
    });
  }

  previousState() {
    window.history.back();
  }
}
