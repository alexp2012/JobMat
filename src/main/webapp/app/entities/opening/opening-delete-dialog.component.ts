import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOpening } from 'app/shared/model/opening.model';
import { OpeningService } from './opening.service';

@Component({
  selector: 'jhi-opening-delete-dialog',
  templateUrl: './opening-delete-dialog.component.html'
})
export class OpeningDeleteDialogComponent {
  opening: IOpening;

  constructor(protected openingService: OpeningService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.openingService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'openingListModification',
        content: 'Deleted an opening'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-opening-delete-popup',
  template: ''
})
export class OpeningDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ opening }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(OpeningDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.opening = opening;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/opening', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/opening', { outlets: { popup: null } }]);
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
