import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { BusinessInterestUpdateComponent } from 'app/entities/business-interest/business-interest-update.component';
import { BusinessInterestService } from 'app/entities/business-interest/business-interest.service';
import { BusinessInterest } from 'app/shared/model/business-interest.model';

describe('Component Tests', () => {
  describe('BusinessInterest Management Update Component', () => {
    let comp: BusinessInterestUpdateComponent;
    let fixture: ComponentFixture<BusinessInterestUpdateComponent>;
    let service: BusinessInterestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [BusinessInterestUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BusinessInterestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusinessInterestUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusinessInterestService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BusinessInterest(123);
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
        const entity = new BusinessInterest();
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
