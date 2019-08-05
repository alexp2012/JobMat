import { IApplication } from 'app/shared/model/application.model';
import { ICompany } from 'app/shared/model/company.model';

export interface ICandidate {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  cVContentType?: string;
  cV?: any;
  mentions?: string;
  expectedSalaryEur?: number;
  applications?: IApplication[];
  company?: ICompany;
}

export class Candidate implements ICandidate {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public cVContentType?: string,
    public cV?: any,
    public mentions?: string,
    public expectedSalaryEur?: number,
    public applications?: IApplication[],
    public company?: ICompany
  ) {}
}
