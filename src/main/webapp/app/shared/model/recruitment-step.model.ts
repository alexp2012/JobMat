import { IApplication } from 'app/shared/model/application.model';
import { IOpening } from 'app/shared/model/opening.model';

export interface IRecruitmentStep {
  id?: number;
  description?: string;
  sequence?: number;
  applications?: IApplication[];
  opening?: IOpening;
}

export class RecruitmentStep implements IRecruitmentStep {
  constructor(
    public id?: number,
    public description?: string,
    public sequence?: number,
    public applications?: IApplication[],
    public opening?: IOpening
  ) {}
}
