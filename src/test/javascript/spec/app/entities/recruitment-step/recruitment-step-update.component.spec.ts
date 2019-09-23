import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { RecruitmentStepUpdateComponent } from 'app/entities/recruitment-step/recruitment-step-update.component';
import { RecruitmentStepService } from 'app/entities/recruitment-step/recruitment-step.service';
import { RecruitmentStep } from 'app/shared/model/recruitment-step.model';

describe('Component Tests', () => {
  describe('RecruitmentStep Management Update Component', () => {
    let comp: RecruitmentStepUpdateComponent;
    let fixture: ComponentFixture<RecruitmentStepUpdateComponent>;
    let service: RecruitmentStepService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [RecruitmentStepUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RecruitmentStepUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RecruitmentStepUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RecruitmentStepService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RecruitmentStep(123);
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
        const entity = new RecruitmentStep();
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
