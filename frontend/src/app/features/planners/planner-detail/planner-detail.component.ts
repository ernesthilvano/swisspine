import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ApiService } from '../../../core/services/api.service';
import { PlannerDTO } from '../../../core/models/dto.models';

@Component({
    selector: 'app-planner-detail',
    standalone: true,
    imports: [
        CommonModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatChipsModule,
        MatProgressSpinnerModule,
        MatSnackBarModule
    ],
    templateUrl: './planner-detail.component.html',
    styleUrls: ['./planner-detail.component.css']
})
export class PlannerDetailComponent implements OnInit {
    planner: PlannerDTO | null = null;
    loading = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private apiService: ApiService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.loadPlanner(+id);
        }
    }

    loadPlanner(id: number): void {
        this.loading = true;
        this.apiService.getPlanner(id).subscribe({
            next: (planner) => {
                this.planner = planner;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading planner:', error);
                this.snackBar.open('Error loading planner details', 'Close', { duration: 3000 });
                this.loading = false;
                this.router.navigate(['/planners']);
            }
        });
    }

    goBack(): void {
        this.router.navigate(['/planners']);
    }

    getStatusColor(status?: string): string {
        switch (status?.toLowerCase()) {
            case 'active': return 'primary';
            case 'draft': return 'accent';
            case 'completed': return '';
            default: return '';
        }
    }
}
