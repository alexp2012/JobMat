import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IApplicationMessage, ApplicationMessage } from 'app/shared/model/application-message.model';
import { ApplicationMessageService } from './application-message.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';
import { IApplication } from 'app/shared/model/application.model';
import { ApplicationService } from 'app/entities/application';

@Component({
  selector: 'jhi-application-message-update',
  templateUrl: './application-message-update.component.html'
})
export class ApplicationMessageUpdateComponent implements OnInit {
  isSaving: boolean;

  companies: ICompany[];

  applications: IApplication[];

  editForm = this.fb.group({
    id: [],
    text: [null, [Validators.required, Validators.minLength(2)]],
    date: [null, [Validators.required]],
    company: [null, Validators.required],
    application: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected applicationMessageService: ApplicationMessageService,
    protected companyService: CompanyService,
    protected applicationService: ApplicationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ applicationMessage }) => {
      this.updateForm(applicationMessage);
    });
    this.companyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe((res: ICompany[]) => (this.companies = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.applicationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IApplication[]>) => mayBeOk.ok),
        map((response: HttpResponse<IApplication[]>) => response.body)
      )
      .subscribe((res: IApplication[]) => (this.applications = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(applicationMessage: IApplicationMessage) {
    this.editForm.patchValue({
      id: applicationMessage.id,
      text: applicationMessage.text,
      date: applicationMessage.date != null ? applicationMessage.date.format(DATE_TIME_FORMAT) : null,
      company: applicationMessage.company,
      application: applicationMessage.application
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const applicationMessage = this.createFromForm();
    if (applicationMessage.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationMessageService.update(applicationMessage));
    } else {
      this.subscribeToSaveResponse(this.applicationMessageService.create(applicationMessage));
    }
  }

  private createFromForm(): IApplicationMessage {
    return {
      ...new ApplicationMessage(),
      id: this.editForm.get(['id']).value,
      text: this.editForm.get(['text']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      company: this.editForm.get(['company']).value,
      application: this.editForm.get(['application']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationMessage>>) {
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

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }

  trackApplicationById(index: number, item: IApplication) {
    return item.id;
  }
}
