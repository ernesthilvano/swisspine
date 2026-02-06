// Core TypeScript models matching backend DTOs

export interface ExternalConnectionDTO {
    id?: number;
    name: string;
    baseUrl: string;
    authenticationMethod: string;
    keyField: string;
    valueField?: string;
    valueFieldSet: boolean;
    authenticationPlace: 'Header' | 'QueryParameters';
    isDefault: boolean;
    createdAt?: string;
    updatedAt?: string;
}

export interface PlannerDTO {
    id?: number;
    name: string;
    description?: string;
    plannerType?: string;
    externalSystemConfigId?: number;
    status?: string;
    funds?: PlannerFundDTO[];
    sources?: PlannerSourceDTO[];
    createdAt?: string;
    updatedAt?: string;
}

export interface FundDTO {
    id: number;
    name: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface FundAliasDTO {
    id: number;
    fundId: number;
    aliasName: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface SourceNameDTO {
    id: number;
    name: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface RunNameDTO {
    id: number;
    name: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ReportTypeDTO {
    id: number;
    name: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ReportNameDTO {
    id: number;
    name: string;
    reportTypeId?: number;
    createdAt?: string;
    updatedAt?: string;
}

export interface PlannerFundDTO {
    id?: number;
    plannerId: number;
    fundId: number;
    fundAliasId?: number;
    createdAt?: string;
    updatedAt?: string;
}

export interface PlannerSourceDTO {
    id?: number;
    plannerId: number;
    sourceNameId?: number;
    displayOrder: number;
    createdAt?: string;
    updatedAt?: string;
}

export interface PlannerRunDTO {
    id?: number;
    plannerSourceId: number;
    runNameId?: number;
    displayOrder: number;
    createdAt?: string;
    updatedAt?: string;
}

export interface PlannerReportDTO {
    id?: number;
    plannerSourceId: number;
    reportTypeId?: number;
    reportNameId?: number;
    displayOrder: number;
    createdAt?: string;
    updatedAt?: string;
}

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    first: boolean;
    last: boolean;
    empty: boolean;
}
