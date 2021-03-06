import { Moment } from 'moment';
import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { IOpening } from 'app/shared/model/opening.model';
import { ICandidate } from 'app/shared/model/candidate.model';
import { ICity } from 'app/shared/model/city.model';
import { IBusinessInterest } from 'app/shared/model/business-interest.model';
import { CompanyType } from 'app/shared/model/enumerations/company-type.model';

export interface ICompany {
  id?: number;
  companyType?: CompanyType;
  name?: string;
  cui?: string;
  joinDate?: Moment;
  users?: IExtendedUser[];
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
    public users?: IExtendedUser[],
    public openings?: IOpening[],
    public candidates?: ICandidate[],
    public cities?: ICity[],
    public interests?: IBusinessInterest[]
  ) {}
}
