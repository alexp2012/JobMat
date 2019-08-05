import { Moment } from 'moment';
import { IApplicationMessage } from 'app/shared/model/application-message.model';
import { IRecruitmentStep } from 'app/shared/model/recruitment-step.model';
import { ICandidate } from 'app/shared/model/candidate.model';

export interface IApplication {
  id?: number;
  date?: Moment;
  messages?: IApplicationMessage[];
  step?: IRecruitmentStep;
  candidate?: ICandidate;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public date?: Moment,
    public messages?: IApplicationMessage[],
    public step?: IRecruitmentStep,
    public candidate?: ICandidate
  ) {}
}
