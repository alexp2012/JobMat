import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IApplication, Application } from 'app/shared/model/application.model';
import { ApplicationService } from './application.service';
import { IRecruitmentStep } from 'app/shared/model/recruitment-step.model';
import { RecruitmentStepService } from 'app/entities/recruitment-step/recruitment-step.service';
import { ICandidate } from 'app/shared/model/candidate.model';
import { CandidateService } from 'app/entities/candidate/candidate.service';

@Component({
  selector: 'jhi-application-update',
  templateUrl: './application-update.component.html'
})
export class ApplicationUpdateComponent implements OnInit {
  isSaving: boolean;

  recruitmentsteps: IRecruitmentStep[];

  candidates: ICandidate[];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    step: [null, Validators.required],
    candidate: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected applicationService: ApplicationService,
    protected recruitmentStepService: RecruitmentStepService,
    protected candidateService: CandidateService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ application }) => {
      this.updateForm(application);
    });
    this.recruitmentStepService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRecruitmentStep[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRecruitmentStep[]>) => response.body)
      )
      .subscribe((res: IRecruitmentStep[]) => (this.recruitmentsteps = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.candidateService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICandidate[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICandidate[]>) => response.body)
      )
      .subscribe((res: ICandidate[]) => (this.candidates = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(application: IApplication) {
    this.editForm.patchValue({
      id: application.id,
      date: application.date != null ? application.date.format(DATE_TIME_FORMAT) : null,
      step: application.step,
      candidate: application.candidate
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const application = this.createFromForm();
    if (application.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationService.update(application));
    } else {
      this.subscribeToSaveResponse(this.applicationService.create(application));
    }
  }

  private createFromForm(): IApplication {
    return {
      ...new Application(),
      id: this.editForm.get(['id']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      step: this.editForm.get(['step']).value,
      candidate: this.editForm.get(['candidate']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplication>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackRecruitmentStepById(index: number, item: IRecruitmentStep) {
    return item.id;
  }

  trackCandidateById(index: number, item: ICandidate) {
    return item.id;
  }
}
