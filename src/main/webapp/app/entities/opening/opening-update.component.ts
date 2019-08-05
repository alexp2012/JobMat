import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IOpening, Opening } from 'app/shared/model/opening.model';
import { OpeningService } from './opening.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
  selector: 'jhi-opening-update',
  templateUrl: './opening-update.component.html'
})
export class OpeningUpdateComponent implements OnInit {
  isSaving: boolean;

  cities: ICity[];

  tags: ITag[];

  companies: ICompany[];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required]],
    title: [null, [Validators.required, Validators.minLength(2)]],
    jD: [],
    jDContentType: [],
    positionsNo: [null, [Validators.required, Validators.min(1), Validators.max(999)]],
    mentions: [],
    publicForNonCollaborators: [null, [Validators.required]],
    date: [null, [Validators.required]],
    city: [null, Validators.required],
    tags: [],
    company: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected openingService: OpeningService,
    protected cityService: CityService,
    protected tagService: TagService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ opening }) => {
      this.updateForm(opening);
    });
    this.cityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICity[]>) => response.body)
      )
      .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tagService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITag[]>) => response.body)
      )
      .subscribe((res: ITag[]) => (this.tags = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.companyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe((res: ICompany[]) => (this.companies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(opening: IOpening) {
    this.editForm.patchValue({
      id: opening.id,
      status: opening.status,
      title: opening.title,
      jD: opening.jD,
      jDContentType: opening.jDContentType,
      positionsNo: opening.positionsNo,
      mentions: opening.mentions,
      publicForNonCollaborators: opening.publicForNonCollaborators,
      date: opening.date != null ? opening.date.format(DATE_TIME_FORMAT) : null,
      city: opening.city,
      tags: opening.tags,
      company: opening.company
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const opening = this.createFromForm();
    if (opening.id !== undefined) {
      this.subscribeToSaveResponse(this.openingService.update(opening));
    } else {
      this.subscribeToSaveResponse(this.openingService.create(opening));
    }
  }

  private createFromForm(): IOpening {
    return {
      ...new Opening(),
      id: this.editForm.get(['id']).value,
      status: this.editForm.get(['status']).value,
      title: this.editForm.get(['title']).value,
      jDContentType: this.editForm.get(['jDContentType']).value,
      jD: this.editForm.get(['jD']).value,
      positionsNo: this.editForm.get(['positionsNo']).value,
      mentions: this.editForm.get(['mentions']).value,
      publicForNonCollaborators: this.editForm.get(['publicForNonCollaborators']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      city: this.editForm.get(['city']).value,
      tags: this.editForm.get(['tags']).value,
      company: this.editForm.get(['company']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOpening>>) {
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

  trackTagById(index: number, item: ITag) {
    return item.id;
  }

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
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
