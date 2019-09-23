import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JobMatTestModule } from '../../../test.module';
import { ApplicationMessageDeleteDialogComponent } from 'app/entities/application-message/application-message-delete-dialog.component';
import { ApplicationMessageService } from 'app/entities/application-message/application-message.service';

describe('Component Tests', () => {
  describe('ApplicationMessage Management Delete Component', () => {
    let comp: ApplicationMessageDeleteDialogComponent;
    let fixture: ComponentFixture<ApplicationMessageDeleteDialogComponent>;
    let service: ApplicationMessageService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [ApplicationMessageDeleteDialogComponent]
      })
        .overrideTemplate(ApplicationMessageDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApplicationMessageDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationMessageService);
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
