import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ApiService } from './api.service';
import { ExternalConnectionDTO, PageResponse, PlannerDTO } from '../models/dto.models';

describe('ApiService', () => {
    let service: ApiService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                ApiService,
                provideHttpClient(),
                provideHttpClientTesting()
            ]
        });
        service = TestBed.inject(ApiService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    describe('External Connections', () => {
        it('getExternalConnections should return paged results', () => {
            const mockResponse: PageResponse<ExternalConnectionDTO> = {
                content: [{ id: 1, name: 'Test Conn', baseUrl: 'http://test.com', authenticationMethod: 'None', keyField: '', authenticationPlace: 'Header', isDefault: false, valueFieldSet: false }],
                totalElements: 1,
                totalPages: 1,
                size: 20,
                number: 0,
                first: true,
                last: true,
                empty: false
            };

            service.getExternalConnections(0, 5, 'search').subscribe(res => {
                expect(res.content.length).toBe(1);
                expect(res.totalElements).toBe(1);
            });

            const req = httpMock.expectOne(req =>
                req.url === 'http://localhost:8080/api/external-connections' &&
                req.params.get('page') === '0' &&
                req.params.get('size') === '5' &&
                req.params.get('search') === 'search'
            );
            expect(req.request.method).toBe('GET');
            req.flush(mockResponse);
        });

        it('createExternalConnection should POST data', () => {
            const newConn: ExternalConnectionDTO = { name: 'New', baseUrl: 'http://new.com', authenticationMethod: 'None', keyField: 'k', authenticationPlace: 'Header', isDefault: false, valueFieldSet: false };

            service.createExternalConnection(newConn).subscribe(res => {
                expect(res.name).toBe('New');
            });

            const req = httpMock.expectOne('http://localhost:8080/api/external-connections');
            expect(req.request.method).toBe('POST');
            expect(req.request.body).toEqual(newConn);
            req.flush(newConn);
        });
    });

    describe('Planners', () => {
        it('getPlanners should return paged results', () => {
            const mockResponse: PageResponse<PlannerDTO> = {
                content: [{ id: 1, name: 'Plan 1', plannerType: 'Standard', status: 'Draft', description: 'Desc' }],
                totalElements: 1,
                totalPages: 1,
                size: 20,
                number: 0,
                first: true,
                last: true,
                empty: false
            };

            service.getPlanners(0, 10, 'Draft').subscribe(res => {
                expect(res.content.length).toBe(1);
            });

            const req = httpMock.expectOne(req =>
                req.url === 'http://localhost:8080/api/planners' &&
                req.params.get('status') === 'Draft'
            );
            expect(req.request.method).toBe('GET');
            req.flush(mockResponse);
        });

        it('searchPlanners should call search endpoint', () => {
            const mockResponse: PageResponse<PlannerDTO> = {
                content: [],
                totalElements: 0,
                totalPages: 0,
                size: 20,
                number: 0,
                first: true,
                last: true,
                empty: true
            };

            service.searchPlanners('Test', 0, 10).subscribe(res => {
                expect(res.content.length).toBe(0);
            });

            const req = httpMock.expectOne(req =>
                req.url === 'http://localhost:8080/api/planners/search' &&
                req.params.get('q') === 'Test'
            );
            expect(req.request.method).toBe('GET');
            req.flush(mockResponse);
        });
    });
});
