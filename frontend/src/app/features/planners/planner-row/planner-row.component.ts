import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { MatBadgeModule } from '@angular/material/badge';
import { PlannerDTO } from '../../../core/models/dto.models';
import { PlannerFormComponent } from '../planner-form/planner-form.component';

@Component({
    selector: 'app-planner-row',
    standalone: true,
    imports: [
        CommonModule,
        MatIconModule,
        MatButtonModule,
        MatTooltipModule,
        MatChipsModule,
        MatBadgeModule,
        PlannerFormComponent
    ],
    templateUrl: './planner-row.component.html',
    styleUrl: './planner-row.component.css'
})
export class PlannerRowComponent {
    @Input() planner!: PlannerDTO;
    @Input() isExpanded = false;
    @Output() toggle = new EventEmitter<void>();
    @Output() save = new EventEmitter<PlannerDTO>();
    @Output() deletePlanner = new EventEmitter<void>();
    @Output() copyPlanner = new EventEmitter<void>();
    @Output() playPlanner = new EventEmitter<void>();

    onRowClick(event: MouseEvent): void {
        const target = event.target as HTMLElement;
        if (!target.closest('button')) {
            this.toggle.emit();
        }
    }

    onSave(planner: PlannerDTO): void {
        this.save.emit(planner);
    }

    onCancel(): void {
        this.toggle.emit();
    }

    onDelete(event: MouseEvent): void {
        event.stopPropagation();
        this.deletePlanner.emit();
    }

    onCopy(event: MouseEvent): void {
        event.stopPropagation();
        this.copyPlanner.emit();
    }

    onPlay(event: MouseEvent): void {
        event.stopPropagation();
        this.playPlanner.emit();
    }

    getStatusColor(status?: string): string {
        switch (status?.toLowerCase()) {
            case 'finished': return 'primary';
            case 'in progress': return 'accent';
            case 'failed': return 'warn';
            default: return '';
        }
    }
}
