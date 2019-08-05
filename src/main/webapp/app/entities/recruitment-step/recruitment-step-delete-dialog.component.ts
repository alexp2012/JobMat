import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRecruitmentStep } from 'app/shared/model/recruitment-step.model';
import { RecruitmentStepService } from './recruitment-step.service';

@Component({
  selector: 'jhi-recruitment-step-delete-dialog',
  templateUrl: './recruitment-step-delete-dialog.component.html'
})
export class RecruitmentStepDeleteDialogComponent {
  recruitmentStep: IRecruitmentStep;

  constructor(
    protected recruitmentStepService: RecruitmentStepService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.recruitmentStepService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'recruitmentStepListModification',
        content: 'Deleted an recruitmentStep'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-recruitment-step-delete-popup',
  template: ''
})
export class RecruitmentStepDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ recruitmentStep }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RecruitmentStepDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.recruitmentStep = recruitmentStep;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/recruitment-step', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/recruitment-step', { outlets: { popup: null } }]);
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
