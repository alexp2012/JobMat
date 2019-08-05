import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBusinessInterest } from 'app/shared/model/business-interest.model';

@Component({
  selector: 'jhi-business-interest-detail',
  templateUrl: './business-interest-detail.component.html'
})
export class BusinessInterestDetailComponent implements OnInit {
  businessInterest: IBusinessInterest;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ businessInterest }) => {
      this.businessInterest = businessInterest;
    });
  }

  previousState() {
    window.history.back();
  }
}
