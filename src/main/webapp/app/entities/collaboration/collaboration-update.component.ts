import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ICollaboration, Collaboration } from 'app/shared/model/collaboration.model';
import { CollaborationService } from './collaboration.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company/company.service';

@Component({
  selector: 'jhi-collaboration-update',
  templateUrl: './collaboration-update.component.html'
})
export class CollaborationUpdateComponent implements OnInit {
  isSaving: boolean;

  companies: ICompany[];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required]],
    initiator: [null, [Validators.required]],
    invitationsNo: [null, [Validators.required]],
    message: [null, [Validators.required, Validators.minLength(20)]],
    contract: [],
    contractContentType: [],
    supplier: [null, Validators.required],
    customer: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected collaborationService: CollaborationService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ collaboration }) => {
      this.updateForm(collaboration);
    });
    this.companyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe((res: ICompany[]) => (this.companies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(collaboration: ICollaboration) {
    this.editForm.patchValue({
      id: collaboration.id,
      status: collaboration.status,
      initiator: collaboration.initiator,
      invitationsNo: collaboration.invitationsNo,
      message: collaboration.message,
      contract: collaboration.contract,
      contractContentType: collaboration.contractContentType,
      supplier: collaboration.supplier,
      customer: collaboration.customer
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
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
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
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const collaboration = this.createFromForm();
    if (collaboration.id !== undefined) {
      this.subscribeToSaveResponse(this.collaborationService.update(collaboration));
    } else {
      this.subscribeToSaveResponse(this.collaborationService.create(collaboration));
    }
  }

  private createFromForm(): ICollaboration {
    return {
      ...new Collaboration(),
      id: this.editForm.get(['id']).value,
      status: this.editForm.get(['status']).value,
      initiator: this.editForm.get(['initiator']).value,
      invitationsNo: this.editForm.get(['invitationsNo']).value,
      message: this.editForm.get(['message']).value,
      contractContentType: this.editForm.get(['contractContentType']).value,
      contract: this.editForm.get(['contract']).value,
      supplier: this.editForm.get(['supplier']).value,
      customer: this.editForm.get(['customer']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollaboration>>) {
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
}
