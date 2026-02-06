import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
    ExternalConnectionDTO,
    PlannerDTO,
    FundDTO,
    FundAliasDTO,
    SourceNameDTO,
    RunNameDTO,
    ReportTypeDTO,
    ReportNameDTO,
    PageResponse
} from '../models/dto.models';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private readonly baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) { }

    // External Connections
    getExternalConnections(page: number = 0, size: number = 20, search?: string): Observable<PageResponse<ExternalConnectionDTO>> {
        let params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());

        if (search) {
            params = params.set('search', search);
        }

        return this.http.get<PageResponse<ExternalConnectionDTO>>(`${this.baseUrl}/external-connections`, { params });
    }

    getExternalConnection(id: number): Observable<ExternalConnectionDTO> {
        return this.http.get<ExternalConnectionDTO>(`${this.baseUrl}/external-connections/${id}`);
    }

    createExternalConnection(connection: ExternalConnectionDTO): Observable<ExternalConnectionDTO> {
        return this.http.post<ExternalConnectionDTO>(`${this.baseUrl}/external-connections`, connection);
    }

    updateExternalConnection(id: number, connection: ExternalConnectionDTO): Observable<ExternalConnectionDTO> {
        return this.http.put<ExternalConnectionDTO>(`${this.baseUrl}/external-connections/${id}`, connection);
    }

    deleteExternalConnection(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/external-connections/${id}`);
    }

    // Planners
    getPlanners(page: number = 0, size: number = 20, status?: string): Observable<PageResponse<PlannerDTO>> {
        let params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());

        if (status) {
            params = params.set('status', status);
        }

        return this.http.get<PageResponse<PlannerDTO>>(`${this.baseUrl}/planners`, { params });
    }

    getPlanner(id: number): Observable<PlannerDTO> {
        return this.http.get<PlannerDTO>(`${this.baseUrl}/planners/${id}`);
    }

    createPlanner(planner: PlannerDTO): Observable<PlannerDTO> {
        return this.http.post<PlannerDTO>(`${this.baseUrl}/planners`, planner);
    }

    updatePlanner(id: number, planner: PlannerDTO): Observable<PlannerDTO> {
        return this.http.put<PlannerDTO>(`${this.baseUrl}/planners/${id}`, planner);
    }

    deletePlanner(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/planners/${id}`);
    }

    searchPlanners(query: string, page: number = 0, size: number = 20, status?: string): Observable<PageResponse<PlannerDTO>> {
        let params = new HttpParams()
            .set('q', query)
            .set('page', page.toString())
            .set('size', size.toString());

        if (status) {
            params = params.set('status', status);
        }

        return this.http.get<PageResponse<PlannerDTO>>(`${this.baseUrl}/planners/search`, { params });
    }

    // Master Data - Source Names
    getSourceNames(): Observable<SourceNameDTO[]> {
        return this.http.get<SourceNameDTO[]>(`${this.baseUrl}/master-data/source-names`);
    }

    // Master Data - Run Names
    getRunNames(): Observable<RunNameDTO[]> {
        return this.http.get<RunNameDTO[]>(`${this.baseUrl}/master-data/run-names`);
    }

    // Master Data - Report Types
    getReportTypes(): Observable<ReportTypeDTO[]> {
        return this.http.get<ReportTypeDTO[]>(`${this.baseUrl}/master-data/report-types`);
    }

    // Master Data - Report Names
    getReportNames(): Observable<ReportNameDTO[]> {
        return this.http.get<ReportNameDTO[]>(`${this.baseUrl}/master-data/report-names`);
    }

    // Master Data - Funds
    getFunds(): Observable<FundDTO[]> {
        return this.http.get<FundDTO[]>(`${this.baseUrl}/master-data/funds`);
    }

    // Master Data - Fund Aliases
    getFundAliases(): Observable<FundAliasDTO[]> {
        return this.http.get<FundAliasDTO[]>(`${this.baseUrl}/master-data/fund-aliases`);
    }

    // Performance Statistics
    getPerformanceStatistics(): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/statistics/performance`);
    }
}
