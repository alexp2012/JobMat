import { ICompany } from 'app/shared/model/company.model';

export const enum CollaborationStatus {
  ACTIVE = 'ACTIVE',
  INVITATION = 'INVITATION',
  REJECTED = 'REJECTED'
}

export const enum CompanyType {
  SUPPLIER = 'SUPPLIER',
  CUSTOMER = 'CUSTOMER'
}

export interface ICollaboration {
  id?: number;
  status?: CollaborationStatus;
  initiator?: CompanyType;
  invitationsNo?: number;
  message?: string;
  contractContentType?: string;
  contract?: any;
  supplier?: ICompany;
  customer?: ICompany;
}

export class Collaboration implements ICollaboration {
  constructor(
    public id?: number,
    public status?: CollaborationStatus,
    public initiator?: CompanyType,
    public invitationsNo?: number,
    public message?: string,
    public contractContentType?: string,
    public contract?: any,
    public supplier?: ICompany,
    public customer?: ICompany
  ) {}
}
