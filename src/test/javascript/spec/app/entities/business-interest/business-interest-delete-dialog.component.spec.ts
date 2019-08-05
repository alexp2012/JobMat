/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JobMatTestModule } from '../../../test.module';
import { BusinessInterestDeleteDialogComponent } from 'app/entities/business-interest/business-interest-delete-dialog.component';
import { BusinessInterestService } from 'app/entities/business-interest/business-interest.service';

describe('Component Tests', () => {
  describe('BusinessInterest Management Delete Component', () => {
    let comp: BusinessInterestDeleteDialogComponent;
    let fixture: ComponentFixture<BusinessInterestDeleteDialogComponent>;
    let service: BusinessInterestService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [BusinessInterestDeleteDialogComponent]
      })
        .overrideTemplate(BusinessInterestDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusinessInterestDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusinessInterestService);
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
