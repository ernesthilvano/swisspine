import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ApiService } from '../../../core/services/api.service';
import { PlannerDTO, PageResponse } from '../../../core/models/dto.models';
import { PlannerRowComponent } from '../planner-row/planner-row.component';

@Component({
    selector: 'app-planners-list',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        MatButtonModule,
        MatIconModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatProgressSpinnerModule,
        MatSnackBarModule,
        PlannerRowComponent
    ],
    templateUrl: './planners-list.component.html',
    styleUrls: ['./planners-list.component.css']
})
export class PlannersListComponent implements OnInit {
    planners: PlannerDTO[] = [];
    loading = false;
    totalElements = 0;
    pageSize = 10;
    pageIndex = 0;
    statusFilter = '';
    searchQuery = '';
    searchDebounceTimer: any;
    expandedPlannerId: number | null = null;
    newPlanner: PlannerDTO | null = null;

    statuses = ['', 'Draft', 'In Progress', 'Finished', 'Failed'];

    constructor(
        private apiService: ApiService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit(): void {
        this.loadPlanners();
    }

    loadPlanners(): void {
        this.loading = true;

        // Use search if query is present, otherwise use regular get with status filter
        const apiCall = this.searchQuery.trim()
            ? this.apiService.searchPlanners(this.searchQuery, this.pageIndex, this.pageSize, this.statusFilter || undefined)
            : this.apiService.getPlanners(this.pageIndex, this.pageSize, this.statusFilter);

        apiCall.subscribe({
            next: (response: PageResponse<PlannerDTO>) => {
                this.planners = response.content;
                this.totalElements = response.totalElements;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading planners:', error);
                this.snackBar.open('Error loading planners', 'Close', { duration: 3000 });
                this.loading = false;
            }
        });
    }

    onPageChange(event: PageEvent): void {
        this.pageIndex = event.pageIndex;
        this.pageSize = event.pageSize;
        this.loadPlanners();
    }

    onStatusFilterChange(): void {
        this.pageIndex = 0;
        this.loadPlanners();
    }

    onSearchChange(): void {
        // Clear the existing timer
        if (this.searchDebounceTimer) {
            clearTimeout(this.searchDebounceTimer);
        }

        // Set a new timer to debounce the search
        this.searchDebounceTimer = setTimeout(() => {
            this.pageIndex = 0; // Reset to first page on new search
            this.loadPlanners();
        }, 300); // 300ms debounce
    }

    createNew(): void {
        this.newPlanner = {
            name: '',
            description: '',
            plannerType: 'Standard',
            status: 'Draft',
            funds: [],
            sources: []
        };
        this.expandedPlannerId = null;
    }

    toggleExpand(plannerId: number): void {
        this.expandedPlannerId = this.expandedPlannerId === plannerId ? null : plannerId;
        this.newPlanner = null;
    }

    isExpanded(plannerId?: number): boolean {
        return plannerId !== undefined && this.expandedPlannerId === plannerId;
    }

    savePlanner(planner: PlannerDTO): void {
        if (planner.id) {
            this.apiService.updatePlanner(planner.id, planner).subscribe({
                next: () => {
                    this.snackBar.open('Planner updated successfully', 'Close', { duration: 3000 });
                    this.expandedPlannerId = null;
                    this.loadPlanners();
                },
                error: (error) => {
                    console.error('Error updating planner:', error);
                    this.snackBar.open(error.error?.message || 'Error updating planner', 'Close', { duration: 3000 });
                }
            });
        } else {
            this.apiService.createPlanner(planner).subscribe({
                next: () => {
                    this.snackBar.open('Planner created successfully', 'Close', { duration: 3000 });
                    this.newPlanner = null;
                    this.loadPlanners();
                },
                error: (error) => {
                    console.error('Error creating planner:', error);
                    this.snackBar.open(error.error?.message || 'Error creating planner', 'Close', { duration: 3000 });
                }
            });
        }
    }

    deletePlanner(planner: PlannerDTO): void {
        if (confirm(`Are you sure you want to delete "${planner.name}"?`)) {
            this.apiService.deletePlanner(planner.id!).subscribe({
                next: () => {
                    this.snackBar.open('Planner deleted successfully', 'Close', { duration: 3000 });
                    this.loadPlanners();
                },
                error: (error) => {
                    console.error('Error deleting planner:', error);
                    this.snackBar.open('Error deleting planner', 'Close', { duration: 3000 });
                }
            });
        }
    }

    copyPlanner(planner: PlannerDTO): void {
        const copiedPlanner: PlannerDTO = {
            name: `${planner.name} (Copy)`,
            description: planner.description,
            plannerType: planner.plannerType,
            status: 'Draft',
            externalSystemConfigId: planner.externalSystemConfigId,
            funds: planner.funds ? [...planner.funds] : [],
            sources: planner.sources ? [...planner.sources] : []
        };

        this.apiService.createPlanner(copiedPlanner).subscribe({
            next: () => {
                this.snackBar.open('Planner copied successfully', 'Close', { duration: 3000 });
                this.loadPlanners();
            },
            error: (error) => {
                console.error('Error copying planner:', error);
                this.snackBar.open('Error copying planner', 'Close', { duration: 3000 });
            }
        });
    }

    playPlanner(planner: PlannerDTO): void {
        // Placeholder for Play/Run functionality
        this.snackBar.open(`Play/Run functionality for "${planner.name}" not yet implemented`, 'Close', { duration: 3000 });
    }

    cancelNew(): void {
        this.newPlanner = null;
    }
}
