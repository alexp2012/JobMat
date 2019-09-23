import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { RecruitmentStepDetailComponent } from 'app/entities/recruitment-step/recruitment-step-detail.component';
import { RecruitmentStep } from 'app/shared/model/recruitment-step.model';

describe('Component Tests', () => {
  describe('RecruitmentStep Management Detail Component', () => {
    let comp: RecruitmentStepDetailComponent;
    let fixture: ComponentFixture<RecruitmentStepDetailComponent>;
    const route = ({ data: of({ recruitmentStep: new RecruitmentStep(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [RecruitmentStepDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RecruitmentStepDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RecruitmentStepDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.recruitmentStep).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
