import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ApiService } from '../../../core/services/api.service';
import { ExternalConnectionDTO, PageResponse } from '../../../core/models/dto.models';
import { ConnectionRowComponent } from '../connection-row/connection-row.component';

@Component({
    selector: 'app-connections-list',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        MatButtonModule,
        MatIconModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatInputModule,
        MatProgressSpinnerModule,
        MatSnackBarModule,
        ConnectionRowComponent
    ],
    templateUrl: './connections-list.component.html',
    styleUrls: ['./connections-list.component.css']
})
export class ConnectionsListComponent implements OnInit {
    connections: ExternalConnectionDTO[] = [];
    loading = false;
    totalElements = 0;
    pageSize = 10;
    pageIndex = 0;
    searchTerm = '';
    expandedConnectionId: number | null = null;
    newConnection: ExternalConnectionDTO | null = null;

    constructor(
        private apiService: ApiService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit(): void {
        this.loadConnections();
    }

    loadConnections(): void {
        this.loading = true;
        this.apiService.getExternalConnections(this.pageIndex, this.pageSize, this.searchTerm)
            .subscribe({
                next: (response: PageResponse<ExternalConnectionDTO>) => {
                    this.connections = response.content;
                    this.totalElements = response.totalElements;
                    this.loading = false;
                },
                error: (error) => {
                    console.error('Error loading connections:', error);
                    this.snackBar.open('Error loading connections', 'Close', { duration: 3000 });
                    this.loading = false;
                }
            });
    }

    onPageChange(event: PageEvent): void {
        this.pageIndex = event.pageIndex;
        this.pageSize = event.pageSize;
        this.loadConnections();
    }

    onSearch(): void {
        this.pageIndex = 0;
        this.loadConnections();
    }

    createNew(): void {
        this.newConnection = {
            name: '',
            baseUrl: '',
            authenticationMethod: 'API Key',
            keyField: '',
            authenticationPlace: 'Header',
            isDefault: false,
            valueFieldSet: false
        };
        this.expandedConnectionId = null;
    }

    toggleExpand(connectionId: number): void {
        this.expandedConnectionId = this.expandedConnectionId === connectionId ? null : connectionId;
        this.newConnection = null;
    }

    isExpanded(connectionId?: number): boolean {
        return connectionId !== undefined && this.expandedConnectionId === connectionId;
    }

    saveConnection(connection: ExternalConnectionDTO): void {
        if (connection.id) {
            this.apiService.updateExternalConnection(connection.id, connection).subscribe({
                next: () => {
                    this.snackBar.open('Connection updated successfully', 'Close', { duration: 3000 });
                    this.expandedConnectionId = null;
                    this.loadConnections();
                },
                error: (error) => {
                    console.error('Error updating connection:', error);
                    this.snackBar.open(error.error?.message || 'Error updating connection', 'Close', { duration: 3000 });
                }
            });
        } else {
            this.apiService.createExternalConnection(connection).subscribe({
                next: () => {
                    this.snackBar.open('Connection created successfully', 'Close', { duration: 3000 });
                    this.newConnection = null;
                    this.loadConnections();
                },
                error: (error) => {
                    console.error('Error creating connection:', error);
                    this.snackBar.open(error.error?.message || 'Error creating connection', 'Close', { duration: 3000 });
                }
            });
        }
    }

    deleteConnection(connection: ExternalConnectionDTO): void {
        if (confirm(`Are you sure you want to delete "${connection.name}"?`)) {
            this.apiService.deleteExternalConnection(connection.id!).subscribe({
                next: () => {
                    this.snackBar.open('Connection deleted successfully', 'Close', { duration: 3000 });
                    this.loadConnections();
                },
                error: (error) => {
                    console.error('Error deleting connection:', error);
                    this.snackBar.open('Error deleting connection', 'Close', { duration: 3000 });
                }
            });
        }
    }

    copyConnection(connection: ExternalConnectionDTO): void {
        const copiedConnection: ExternalConnectionDTO = {
            name: `${connection.name} (Copy)`,
            baseUrl: connection.baseUrl,
            authenticationMethod: connection.authenticationMethod,
            keyField: connection.keyField,
            authenticationPlace: connection.authenticationPlace,
            isDefault: false,
            valueField: '',
            valueFieldSet: false
        };

        this.apiService.createExternalConnection(copiedConnection).subscribe({
            next: () => {
                this.snackBar.open('Connection copied successfully', 'Close', { duration: 3000 });
                this.loadConnections();
            },
            error: (error) => {
                console.error('Error copying connection:', error);
                this.snackBar.open('Error copying connection', 'Close', { duration: 3000 });
            }
        });
    }

    cancelNew(): void {
        this.newConnection = null;
    }
}
