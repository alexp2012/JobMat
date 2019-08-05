/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobMatTestModule } from '../../../test.module';
import { ApplicationMessageDetailComponent } from 'app/entities/application-message/application-message-detail.component';
import { ApplicationMessage } from 'app/shared/model/application-message.model';

describe('Component Tests', () => {
  describe('ApplicationMessage Management Detail Component', () => {
    let comp: ApplicationMessageDetailComponent;
    let fixture: ComponentFixture<ApplicationMessageDetailComponent>;
    const route = ({ data: of({ applicationMessage: new ApplicationMessage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JobMatTestModule],
        declarations: [ApplicationMessageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ApplicationMessageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApplicationMessageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.applicationMessage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
