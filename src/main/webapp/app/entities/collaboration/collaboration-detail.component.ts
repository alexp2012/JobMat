import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICollaboration } from 'app/shared/model/collaboration.model';

@Component({
  selector: 'jhi-collaboration-detail',
  templateUrl: './collaboration-detail.component.html'
})
export class CollaborationDetailComponent implements OnInit {
  collaboration: ICollaboration;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ collaboration }) => {
      this.collaboration = collaboration;
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
