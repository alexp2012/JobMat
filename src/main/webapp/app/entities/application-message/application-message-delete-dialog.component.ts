import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IApplicationMessage } from 'app/shared/model/application-message.model';
import { ApplicationMessageService } from './application-message.service';

@Component({
  selector: 'jhi-application-message-delete-dialog',
  templateUrl: './application-message-delete-dialog.component.html'
})
export class ApplicationMessageDeleteDialogComponent {
  applicationMessage: IApplicationMessage;

  constructor(
    protected applicationMessageService: ApplicationMessageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.applicationMessageService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'applicationMessageListModification',
        content: 'Deleted an applicationMessage'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-application-message-delete-popup',
  template: ''
})
export class ApplicationMessageDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ applicationMessage }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ApplicationMessageDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.applicationMessage = applicationMessage;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/application-message', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/application-message', { outlets: { popup: null } }]);
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
