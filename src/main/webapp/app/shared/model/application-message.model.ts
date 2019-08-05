import { Moment } from 'moment';
import { ICompany } from 'app/shared/model/company.model';
import { IApplication } from 'app/shared/model/application.model';

export interface IApplicationMessage {
  id?: number;
  text?: string;
  date?: Moment;
  company?: ICompany;
  application?: IApplication;
}

export class ApplicationMessage implements IApplicationMessage {
  constructor(
    public id?: number,
    public text?: string,
    public date?: Moment,
    public company?: ICompany,
    public application?: IApplication
  ) {}
}
