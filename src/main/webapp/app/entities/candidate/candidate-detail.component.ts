import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICandidate } from 'app/shared/model/candidate.model';

@Component({
  selector: 'jhi-candidate-detail',
  templateUrl: './candidate-detail.component.html'
})
export class CandidateDetailComponent implements OnInit {
  candidate: ICandidate;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ candidate }) => {
      this.candidate = candidate;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
