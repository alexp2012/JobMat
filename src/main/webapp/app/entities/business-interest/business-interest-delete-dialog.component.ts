import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBusinessInterest } from 'app/shared/model/business-interest.model';
import { BusinessInterestService } from './business-interest.service';

@Component({
  selector: 'jhi-business-interest-delete-dialog',
  templateUrl: './business-interest-delete-dialog.component.html'
})
export class BusinessInterestDeleteDialogComponent {
  businessInterest: IBusinessInterest;

  constructor(
    protected businessInterestService: BusinessInterestService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.businessInterestService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'businessInterestListModification',
        content: 'Deleted an businessInterest'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-business-interest-delete-popup',
  template: ''
})
export class BusinessInterestDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ businessInterest }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BusinessInterestDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.businessInterest = businessInterest;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/business-interest', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/business-interest', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
