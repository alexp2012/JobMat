import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JobMatTestModule } from '../../../test.module';
import { RecruitmentStepDeleteDialogComponent } from 'app/entities/recruitment-step/recruitment-step-delete-dialog.component';
import { RecruitmentStepService } from 'app/entities/recruitment-step/recruitment-step.service';

describe('Component Tests', () => {
  describe('RecruitmentStep Management Delete Component', () => {
    let comp: RecruitmentStepDeleteDialogComponent;
    let fixture: ComponentFixture<RecruitmentStepDeleteDialogComponent>;
    let service: RecruitmentStepService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [RecruitmentStepDeleteDialogComponent]
      })
        .overrideTemplate(RecruitmentStepDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RecruitmentStepDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RecruitmentStepService);
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
