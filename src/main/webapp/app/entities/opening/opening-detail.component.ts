import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IOpening } from 'app/shared/model/opening.model';

@Component({
  selector: 'jhi-opening-detail',
  templateUrl: './opening-detail.component.html'
})
export class OpeningDetailComponent implements OnInit {
  opening: IOpening;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ opening }) => {
      this.opening = opening;
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
