import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApplicationMessage } from 'app/shared/model/application-message.model';

@Component({
  selector: 'jhi-application-message-detail',
  templateUrl: './application-message-detail.component.html'
})
export class ApplicationMessageDetailComponent implements OnInit {
  applicationMessage: IApplicationMessage;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ applicationMessage }) => {
      this.applicationMessage = applicationMessage;
    });
  }

  previousState() {
    window.history.back();
  }
}
