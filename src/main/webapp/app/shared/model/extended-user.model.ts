import { IUser } from 'app/core/user/user.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IExtendedUser {
  id?: number;
  user?: IUser;
  company?: ICompany;
}

export class ExtendedUser implements IExtendedUser {
  constructor(public id?: number, public user?: IUser, public company?: ICompany) {}
}
