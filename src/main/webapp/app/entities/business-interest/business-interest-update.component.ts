import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IBusinessInterest, BusinessInterest } from 'app/shared/model/business-interest.model';
import { BusinessInterestService } from './business-interest.service';

@Component({
  selector: 'jhi-business-interest-update',
  templateUrl: './business-interest-update.component.html'
})
export class BusinessInterestUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    interest: [null, [Validators.required]]
  });

  constructor(
    protected businessInterestService: BusinessInterestService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ businessInterest }) => {
      this.updateForm(businessInterest);
    });
  }

  updateForm(businessInterest: IBusinessInterest) {
    this.editForm.patchValue({
      id: businessInterest.id,
      interest: businessInterest.interest
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const businessInterest = this.createFromForm();
    if (businessInterest.id !== undefined) {
      this.subscribeToSaveResponse(this.businessInterestService.update(businessInterest));
    } else {
      this.subscribeToSaveResponse(this.businessInterestService.create(businessInterest));
    }
  }

  private createFromForm(): IBusinessInterest {
    return {
      ...new BusinessInterest(),
      id: this.editForm.get(['id']).value,
      interest: this.editForm.get(['interest']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessInterest>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
