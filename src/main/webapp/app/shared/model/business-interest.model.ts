export interface IBusinessInterest {
  id?: number;
  interest?: string;
}

export class BusinessInterest implements IBusinessInterest {
  constructor(public id?: number, public interest?: string) {}
}
