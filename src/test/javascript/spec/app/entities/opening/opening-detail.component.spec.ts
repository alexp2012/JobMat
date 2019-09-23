import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { OpeningDetailComponent } from 'app/entities/opening/opening-detail.component';
import { Opening } from 'app/shared/model/opening.model';

describe('Component Tests', () => {
  describe('Opening Management Detail Component', () => {
    let comp: OpeningDetailComponent;
    let fixture: ComponentFixture<OpeningDetailComponent>;
    const route = ({ data: of({ opening: new Opening(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [OpeningDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(OpeningDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OpeningDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.opening).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
