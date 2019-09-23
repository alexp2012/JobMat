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
import { ICompany, Company } from 'app/shared/model/company.model';
import { CompanyService } from './company.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';
import { IBusinessInterest } from 'app/shared/model/business-interest.model';
import { BusinessInterestService } from 'app/entities/business-interest/business-interest.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html'
})
export class CompanyUpdateComponent implements OnInit {
  isSaving: boolean;

  cities: ICity[];

  businessinterests: IBusinessInterest[];

  editForm = this.fb.group({
    id: [],
    companyType: [null, [Validators.required]],
    name: [null, [Validators.required, Validators.minLength(2)]],
    cui: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(12)]],
    joinDate: [null, [Validators.required]],
    cities: [],
    interests: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected companyService: CompanyService,
    protected cityService: CityService,
    protected businessInterestService: BusinessInterestService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);
    });
    this.cityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICity[]>) => response.body)
      )
      .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.businessInterestService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBusinessInterest[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBusinessInterest[]>) => response.body)
      )
      .subscribe((res: IBusinessInterest[]) => (this.businessinterests = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(company: ICompany) {
    this.editForm.patchValue({
      id: company.id,
      companyType: company.companyType,
      name: company.name,
      cui: company.cui,
      joinDate: company.joinDate != null ? company.joinDate.format(DATE_TIME_FORMAT) : null,
      cities: company.cities,
      interests: company.interests
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  private createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id']).value,
      companyType: this.editForm.get(['companyType']).value,
      name: this.editForm.get(['name']).value,
      cui: this.editForm.get(['cui']).value,
      joinDate: this.editForm.get(['joinDate']).value != null ? moment(this.editForm.get(['joinDate']).value, DATE_TIME_FORMAT) : undefined,
      cities: this.editForm.get(['cities']).value,
      interests: this.editForm.get(['interests']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>) {
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

  trackCityById(index: number, item: ICity) {
    return item.id;
  }

  trackBusinessInterestById(index: number, item: IBusinessInterest) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
