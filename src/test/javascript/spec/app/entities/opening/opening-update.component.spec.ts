/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { OpeningUpdateComponent } from 'app/entities/opening/opening-update.component';
import { OpeningService } from 'app/entities/opening/opening.service';
import { Opening } from 'app/shared/model/opening.model';

describe('Component Tests', () => {
  describe('Opening Management Update Component', () => {
    let comp: OpeningUpdateComponent;
    let fixture: ComponentFixture<OpeningUpdateComponent>;
    let service: OpeningService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [OpeningUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(OpeningUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OpeningUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OpeningService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Opening(123);
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
        const entity = new Opening();
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
