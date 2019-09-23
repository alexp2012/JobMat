import { Moment } from 'moment';
import { IRecruitmentStep } from 'app/shared/model/recruitment-step.model';
import { ICity } from 'app/shared/model/city.model';
import { ITag } from 'app/shared/model/tag.model';
import { ICompany } from 'app/shared/model/company.model';
import { OpeningStatus } from 'app/shared/model/enumerations/opening-status.model';

export interface IOpening {
  id?: number;
  status?: OpeningStatus;
  title?: string;
  jDContentType?: string;
  jD?: any;
  positionsNo?: number;
  mentions?: string;
  publicForNonCollaborators?: boolean;
  date?: Moment;
  steps?: IRecruitmentStep[];
  city?: ICity;
  tags?: ITag[];
  company?: ICompany;
}

export class Opening implements IOpening {
  constructor(
    public id?: number,
    public status?: OpeningStatus,
    public title?: string,
    public jDContentType?: string,
    public jD?: any,
    public positionsNo?: number,
    public mentions?: string,
    public publicForNonCollaborators?: boolean,
    public date?: Moment,
    public steps?: IRecruitmentStep[],
    public city?: ICity,
    public tags?: ITag[],
    public company?: ICompany
  ) {
    this.publicForNonCollaborators = this.publicForNonCollaborators || false;
  }
}
