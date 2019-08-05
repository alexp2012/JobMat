/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { ApplicationMessageUpdateComponent } from 'app/entities/application-message/application-message-update.component';
import { ApplicationMessageService } from 'app/entities/application-message/application-message.service';
import { ApplicationMessage } from 'app/shared/model/application-message.model';

describe('Component Tests', () => {
  describe('ApplicationMessage Management Update Component', () => {
    let comp: ApplicationMessageUpdateComponent;
    let fixture: ComponentFixture<ApplicationMessageUpdateComponent>;
    let service: ApplicationMessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [ApplicationMessageUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ApplicationMessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationMessageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationMessageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ApplicationMessage(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ApplicationMessage();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
