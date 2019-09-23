import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRecruitmentStep, RecruitmentStep } from 'app/shared/model/recruitment-step.model';
import { RecruitmentStepService } from './recruitment-step.service';
import { IOpening } from 'app/shared/model/opening.model';
import { OpeningService } from 'app/entities/opening/opening.service';

@Component({
  selector: 'jhi-recruitment-step-update',
  templateUrl: './recruitment-step-update.component.html'
})
export class RecruitmentStepUpdateComponent implements OnInit {
  isSaving: boolean;

  openings: IOpening[];

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(10)]],
    sequence: [null, [Validators.required]],
    opening: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected recruitmentStepService: RecruitmentStepService,
    protected openingService: OpeningService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ recruitmentStep }) => {
      this.updateForm(recruitmentStep);
    });
    this.openingService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IOpening[]>) => mayBeOk.ok),
        map((response: HttpResponse<IOpening[]>) => response.body)
      )
      .subscribe((res: IOpening[]) => (this.openings = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(recruitmentStep: IRecruitmentStep) {
    this.editForm.patchValue({
      id: recruitmentStep.id,
      description: recruitmentStep.description,
      sequence: recruitmentStep.sequence,
      opening: recruitmentStep.opening
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const recruitmentStep = this.createFromForm();
    if (recruitmentStep.id !== undefined) {
      this.subscribeToSaveResponse(this.recruitmentStepService.update(recruitmentStep));
    } else {
      this.subscribeToSaveResponse(this.recruitmentStepService.create(recruitmentStep));
    }
  }

  private createFromForm(): IRecruitmentStep {
    return {
      ...new RecruitmentStep(),
      id: this.editForm.get(['id']).value,
      description: this.editForm.get(['description']).value,
      sequence: this.editForm.get(['sequence']).value,
      opening: this.editForm.get(['opening']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecruitmentStep>>) {
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

  trackOpeningById(index: number, item: IOpening) {
    return item.id;
  }
}
