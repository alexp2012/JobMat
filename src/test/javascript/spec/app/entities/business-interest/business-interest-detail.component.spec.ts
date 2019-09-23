import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { BusinessInterestDetailComponent } from 'app/entities/business-interest/business-interest-detail.component';
import { BusinessInterest } from 'app/shared/model/business-interest.model';

describe('Component Tests', () => {
  describe('BusinessInterest Management Detail Component', () => {
    let comp: BusinessInterestDetailComponent;
    let fixture: ComponentFixture<BusinessInterestDetailComponent>;
    const route = ({ data: of({ businessInterest: new BusinessInterest(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [BusinessInterestDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BusinessInterestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusinessInterestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.businessInterest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
