import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IOpening } from 'app/shared/model/opening.model';
import { ICandidate } from 'app/shared/model/candidate.model';
import { ICity } from 'app/shared/model/city.model';
import { IBusinessInterest } from 'app/shared/model/business-interest.model';

export const enum CompanyType {
  SUPPLIER = 'SUPPLIER',
  CUSTOMER = 'CUSTOMER'
}

export interface ICompany {
  id?: number;
  companyType?: CompanyType;
  name?: string;
  cui?: string;
  joinDate?: Moment;
  users?: IUser[];
  openings?: IOpening[];
  candidates?: ICandidate[];
  cities?: ICity[];
  interests?: IBusinessInterest[];
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public companyType?: CompanyType,
    public name?: string,
    public cui?: string,
    public joinDate?: Moment,
    public users?: IUser[],
    public openings?: IOpening[],
    public candidates?: ICandidate[],
    public cities?: ICity[],
    public interests?: IBusinessInterest[]
  ) {}
}
