import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IExtendedUser, ExtendedUser } from 'app/shared/model/extended-user.model';
import { ExtendedUserService } from './extended-user.service';
import { IUser, UserService } from 'app/core';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
  selector: 'jhi-extended-user-update',
  templateUrl: './extended-user-update.component.html'
})
export class ExtendedUserUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  companies: ICompany[];

  editForm = this.fb.group({
    id: [],
    user: [],
    company: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected extendedUserService: ExtendedUserService,
    protected userService: UserService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ extendedUser }) => {
      this.updateForm(extendedUser);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.companyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe((res: ICompany[]) => (this.companies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(extendedUser: IExtendedUser) {
    this.editForm.patchValue({
      id: extendedUser.id,
      user: extendedUser.user,
      company: extendedUser.company
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const extendedUser = this.createFromForm();
    if (extendedUser.id !== undefined) {
      this.subscribeToSaveResponse(this.extendedUserService.update(extendedUser));
    } else {
      this.subscribeToSaveResponse(this.extendedUserService.create(extendedUser));
    }
  }

  private createFromForm(): IExtendedUser {
    return {
      ...new ExtendedUser(),
      id: this.editForm.get(['id']).value,
      user: this.editForm.get(['user']).value,
      company: this.editForm.get(['company']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtendedUser>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }
}
