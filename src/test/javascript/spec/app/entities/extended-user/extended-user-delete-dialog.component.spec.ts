import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JobMatTestModule } from '../../../test.module';
import { ExtendedUserDeleteDialogComponent } from 'app/entities/extended-user/extended-user-delete-dialog.component';
import { ExtendedUserService } from 'app/entities/extended-user/extended-user.service';

describe('Component Tests', () => {
  describe('ExtendedUser Management Delete Component', () => {
    let comp: ExtendedUserDeleteDialogComponent;
    let fixture: ComponentFixture<ExtendedUserDeleteDialogComponent>;
    let service: ExtendedUserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [ExtendedUserDeleteDialogComponent]
      })
        .overrideTemplate(ExtendedUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExtendedUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExtendedUserService);
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
