import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { ExternalConnectionDTO } from '../../../core/models/dto.models';
import { ConnectionFormComponent } from '../connection-form/connection-form.component';

@Component({
    selector: 'app-connection-row',
    standalone: true,
    imports: [
        CommonModule,
        MatIconModule,
        MatButtonModule,
        MatTooltipModule,
        MatChipsModule,
        ConnectionFormComponent
    ],
    templateUrl: './connection-row.component.html',
    styleUrl: './connection-row.component.css'
})
export class ConnectionRowComponent {
    @Input() connection!: ExternalConnectionDTO;
    @Input() isExpanded = false;
    @Output() toggle = new EventEmitter<void>();
    @Output() save = new EventEmitter<ExternalConnectionDTO>();
    @Output() deleteConnection = new EventEmitter<void>();
    @Output() copyConnection = new EventEmitter<void>();

    onRowClick(event: MouseEvent): void {
        // Don't toggle if clicking on action buttons
        const target = event.target as HTMLElement;
        if (!target.closest('button')) {
            this.toggle.emit();
        }
    }

    onSave(connection: ExternalConnectionDTO): void {
        this.save.emit(connection);
    }

    onCancel(): void {
        this.toggle.emit(); // Collapse on cancel
    }

    onDelete(event: MouseEvent): void {
        event.stopPropagation();
        this.deleteConnection.emit();
    }

    onCopy(event: MouseEvent): void {
        event.stopPropagation();
        this.copyConnection.emit();
    }
}
