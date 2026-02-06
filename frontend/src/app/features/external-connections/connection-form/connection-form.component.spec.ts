import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConnectionFormComponent } from './connection-form.component';
import { ApiService } from '../../../core/services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { provideAnimations } from '@angular/platform-browser/animations';
import { ExternalConnectionDTO } from '../../../core/models/dto.models';
import { vi } from 'vitest';

describe('ConnectionFormComponent', () => {
    let component: ConnectionFormComponent;
    let fixture: ComponentFixture<ConnectionFormComponent>;
    let apiServiceSpy: any;
    let snackBarSpy: any;
    let dialogRefSpy: any;

    beforeEach(async () => {
        apiServiceSpy = {
            createExternalConnection: vi.fn(),
            updateExternalConnection: vi.fn()
        };
        snackBarSpy = { open: vi.fn() };
        dialogRefSpy = { close: vi.fn() };

        await TestBed.configureTestingModule({
            imports: [ConnectionFormComponent, ReactiveFormsModule],
            providers: [
                { provide: ApiService, useValue: apiServiceSpy },
                { provide: MatSnackBar, useValue: snackBarSpy },
                { provide: MatDialogRef, useValue: dialogRefSpy },
                { provide: MAT_DIALOG_DATA, useValue: null },
                provideAnimations()
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(ConnectionFormComponent);
        component = fixture.componentInstance;
        component.isInlineMode = false; // Default
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should have invalid form initially', () => {
        expect(component.form.valid).toBe(false);
    });

    it('should validate form', () => {
        component.form.controls['name'].setValue('Test');
        component.form.controls['baseUrl'].setValue('http://test.com');
        component.form.controls['authenticationMethod'].setValue('None'); // Ensure this value matches component's expected values
        component.form.controls['keyField'].setValue('key');
        component.form.controls['authenticationPlace'].setValue('Header');

        expect(component.form.valid).toBe(true);
    });

    it('should initialize in edit mode when data passed', () => {
        const connData: ExternalConnectionDTO = { id: 1, name: 'Edit', baseUrl: 'http://test.com', authenticationMethod: 'API Key', keyField: 'k', authenticationPlace: 'Header', isDefault: false, valueFieldSet: true };

        component.data = { connection: connData };
        component.ngOnInit();

        expect(component.isEditMode).toBe(true);
        expect(component.form.get('name')?.value).toBe('Edit');
        expect(component.form.get('valueField')?.disabled).toBe(true);
    });

    it('should emit save in inline mode', () => {
        component.isInlineMode = true;
        component.connection = { id: 1, name: 'Edit', baseUrl: 'http://test.com', authenticationMethod: 'API Key', keyField: 'k', authenticationPlace: 'Header', isDefault: false, valueFieldSet: true };
        component.ngOnInit();

        vi.spyOn(component.save, 'emit');

        component.onSubmit();

        expect(component.save.emit).toHaveBeenCalled();
        expect(apiServiceSpy.createExternalConnection).not.toHaveBeenCalled();
        expect(apiServiceSpy.updateExternalConnection).not.toHaveBeenCalled();
    });

    it('should call API in dialog create mode', () => {
        component.form.setValue({
            name: 'New',
            baseUrl: 'http://new.com',
            authenticationMethod: 'API Key',
            keyField: 'k',
            authenticationPlace: 'Header',
            isDefault: false,
            valueField: 'secret'
        });

        apiServiceSpy.createExternalConnection.mockReturnValue(of({ name: 'New' } as any));

        component.onSubmit();

        expect(apiServiceSpy.createExternalConnection).toHaveBeenCalled();
        expect(dialogRefSpy.close).toHaveBeenCalledWith(true);
    });

    it('should call API in dialog update mode', () => {
        component.data = { connection: { id: 1, name: 'Edit', baseUrl: 'http://test.com', authenticationMethod: 'API Key', keyField: 'k', authenticationPlace: 'Header', isDefault: false, valueFieldSet: true } };
        component.ngOnInit(); // Set edit mode

        apiServiceSpy.updateExternalConnection.mockReturnValue(of({ name: 'Edit' } as any));

        component.onSubmit();

        expect(apiServiceSpy.updateExternalConnection).toHaveBeenCalledWith(1, expect.any(Object));
        expect(dialogRefSpy.close).toHaveBeenCalledWith(true);
    });
});
