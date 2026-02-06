import { Component, EventEmitter, Inject, Input, OnInit, Optional, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ApiService } from '../../../core/services/api.service';
import { ExternalConnectionDTO } from '../../../core/models/dto.models';

@Component({
    selector: 'app-connection-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatSelectModule,
        MatCheckboxModule,
        MatSlideToggleModule,
        MatSnackBarModule
    ],
    templateUrl: './connection-form.component.html',
    styleUrls: ['./connection-form.component.css']
})
export class ConnectionFormComponent implements OnInit {
    // For inline mode (expandable rows)
    @Input() connection?: ExternalConnectionDTO | null;
    @Input() isInlineMode = false;
    @Output() save = new EventEmitter<ExternalConnectionDTO>();
    @Output() cancel = new EventEmitter<void>();

    form: FormGroup;
    isEditMode = false;

    authMethods = ['API Key', 'Bearer Token', 'Basic Auth', 'OAuth'];
    authPlaces: ('Header' | 'QueryParameters')[] = ['Header', 'QueryParameters'];

    constructor(
        private fb: FormBuilder,
        private apiService: ApiService,
        private snackBar: MatSnackBar,
        @Optional() public dialogRef: MatDialogRef<ConnectionFormComponent>,
        @Optional() @Inject(MAT_DIALOG_DATA) public data: { connection: ExternalConnectionDTO | null } | null
    ) {
        this.form = this.fb.group({
            name: ['', Validators.required],
            baseUrl: ['', [Validators.required, Validators.pattern(/^https?:\/\/.+/)]],
            authenticationMethod: ['', Validators.required],
            keyField: ['', Validators.required],
            valueField: [''],
            authenticationPlace: ['Header', Validators.required],
            isDefault: [false]
        });
    }

    ngOnInit(): void {
        // Handle both inline mode and dialog mode
        const connectionData = this.isInlineMode ? this.connection : this.data?.connection;
        if (connectionData?.id) {
            this.isEditMode = true;
            this.form.patchValue(connectionData);

            // Disable valueField during edit - it can only be set during creation
            this.form.get('valueField')?.disable();
        } else if (connectionData) {
            // New connection with pre-filled data
            this.form.patchValue(connectionData);
        }
    }

    onSubmit(): void {
        if (this.form.valid) {
            const connectionData: ExternalConnectionDTO = {
                ...this.form.value,
                // Auto-set valueFieldSet based on whether value is provided
                valueFieldSet: !!this.form.get('valueField')?.value
            };

            // In inline mode, emit the data instead of calling API
            if (this.isInlineMode) {
                const connId = this.connection?.id;
                this.save.emit({ ...connectionData, id: connId });
                return;
            }

            // Dialog mode: call API directly
            const saveObservable = this.isEditMode
                ? this.apiService.updateExternalConnection(this.data!.connection!.id!, connectionData)
                : this.apiService.createExternalConnection(connectionData);

            saveObservable.subscribe({
                next: () => {
                    this.snackBar.open(
                        `Connection ${this.isEditMode ? 'updated' : 'created'} successfully`,
                        'Close',
                        { duration: 3000 }
                    );
                    this.dialogRef?.close(true);
                },
                error: (error) => {
                    console.error('Error saving connection:', error);
                    this.snackBar.open('Error saving connection', 'Close', { duration: 3000 });
                }
            });
        }
    }

    onCancel(): void {
        if (this.isInlineMode) {
            this.cancel.emit();
        } else {
            this.dialogRef?.close(false);
        }
    }
}
