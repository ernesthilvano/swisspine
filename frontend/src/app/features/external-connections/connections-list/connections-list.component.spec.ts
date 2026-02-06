import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConnectionsListComponent } from './connections-list.component';
import { ApiService } from '../../../core/services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { PageResponse, ExternalConnectionDTO } from '../../../core/models/dto.models';
import { provideAnimations } from '@angular/platform-browser/animations';
import { vi } from 'vitest';

describe('ConnectionsListComponent', () => {
    let component: ConnectionsListComponent;
    let fixture: ComponentFixture<ConnectionsListComponent>;
    let apiServiceSpy: any;
    let snackBarSpy: any;

    const mockPageResponse: PageResponse<ExternalConnectionDTO> = {
        content: [
            { id: 1, name: 'Conn 1', baseUrl: 'http://test.com', authenticationMethod: 'API Key', keyField: 'k', authenticationPlace: 'Header', isDefault: false, valueFieldSet: true }
        ],
        totalElements: 1,
        totalPages: 1,
        size: 10,
        number: 0,
        first: true,
        last: true,
        empty: false
    };

    beforeEach(async () => {
        apiServiceSpy = {
            getExternalConnections: vi.fn(),
            deleteExternalConnection: vi.fn(),
            createExternalConnection: vi.fn(),
            updateExternalConnection: vi.fn()
        };
        snackBarSpy = { open: vi.fn() };

        apiServiceSpy.getExternalConnections.mockReturnValue(of(mockPageResponse));

        await TestBed.configureTestingModule({
            imports: [ConnectionsListComponent],
            providers: [
                { provide: ApiService, useValue: apiServiceSpy },
                { provide: MatSnackBar, useValue: snackBarSpy },
                provideAnimations()
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(ConnectionsListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should load connections on init', () => {
        expect(apiServiceSpy.getExternalConnections).toHaveBeenCalled();
        expect(component.connections.length).toBe(1);
    });

    it('should call createExternalConnection when saving new connection', () => {
        const newConn: ExternalConnectionDTO = { name: 'New', baseUrl: 'url', authenticationMethod: 'None', keyField: 'k', authenticationPlace: 'Header', isDefault: false, valueFieldSet: false };

        apiServiceSpy.createExternalConnection.mockReturnValue(of(newConn));

        component.saveConnection(newConn);

        expect(apiServiceSpy.createExternalConnection).toHaveBeenCalledWith(newConn);
    });

    it('should call updateExternalConnection when saving existing connection', () => {
        const existingConn: ExternalConnectionDTO = { id: 1, name: 'Updated', baseUrl: 'url', authenticationMethod: 'None', keyField: 'k', authenticationPlace: 'Header', isDefault: false, valueFieldSet: false };

        apiServiceSpy.updateExternalConnection.mockReturnValue(of(existingConn));

        component.saveConnection(existingConn);

        expect(apiServiceSpy.updateExternalConnection).toHaveBeenCalledWith(1, existingConn);
    });

    it('should call deleteExternalConnection when deleting connection', () => {
        vi.spyOn(window, 'confirm').mockReturnValueOnce(true);
        const connToDelete = mockPageResponse.content[0];

        apiServiceSpy.deleteExternalConnection.mockReturnValue(of(void 0));

        component.deleteConnection(connToDelete);

        expect(apiServiceSpy.deleteExternalConnection).toHaveBeenCalledWith(1);
    });

    it('should copy connection with modified name', () => {
        const connToCopy = mockPageResponse.content[0];

        apiServiceSpy.createExternalConnection.mockReturnValue(of({ ...connToCopy, name: 'Conn 1 (Copy)' }));

        component.copyConnection(connToCopy);

        expect(apiServiceSpy.createExternalConnection).toHaveBeenCalledWith(expect.objectContaining({
            name: 'Conn 1 (Copy)',
            baseUrl: connToCopy.baseUrl
        }));
    });
});
