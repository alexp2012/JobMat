import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICandidate } from 'app/shared/model/candidate.model';
import { CandidateService } from './candidate.service';

@Component({
  selector: 'jhi-candidate-delete-dialog',
  templateUrl: './candidate-delete-dialog.component.html'
})
export class CandidateDeleteDialogComponent {
  candidate: ICandidate;

  constructor(protected candidateService: CandidateService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.candidateService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'candidateListModification',
        content: 'Deleted an candidate'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-candidate-delete-popup',
  template: ''
})
export class CandidateDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ candidate }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CandidateDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.candidate = candidate;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/candidate', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/candidate', { outlets: { popup: null } }]);
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
