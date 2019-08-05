/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JobMatTestModule } from '../../../test.module';
import { CandidateDeleteDialogComponent } from 'app/entities/candidate/candidate-delete-dialog.component';
import { CandidateService } from 'app/entities/candidate/candidate.service';

describe('Component Tests', () => {
  describe('Candidate Management Delete Component', () => {
    let comp: CandidateDeleteDialogComponent;
    let fixture: ComponentFixture<CandidateDeleteDialogComponent>;
    let service: CandidateService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [CandidateDeleteDialogComponent]
      })
        .overrideTemplate(CandidateDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CandidateDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CandidateService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
