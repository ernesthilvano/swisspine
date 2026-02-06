import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ApiService } from '../../../core/services/api.service';
import { PerformanceStatistics } from '../../../core/models/performance.model';
import { interval, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
    selector: 'app-statistics-dashboard',
    standalone: true,
    imports: [
        CommonModule,
        DatePipe,
        DecimalPipe,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        MatProgressSpinnerModule
    ],
    templateUrl: './statistics-dashboard.component.html',
    styleUrls: ['./statistics-dashboard.component.css']
})
export class StatisticsDashboardComponent implements OnInit, OnDestroy {
    statistics: PerformanceStatistics | null = null;
    loading = true;
    error: string | null = null;
    autoRefresh = false;
    private refreshSubscription?: Subscription;

    constructor(private apiService: ApiService) { }

    ngOnInit(): void {
        this.loadStatistics();
    }

    ngOnDestroy(): void {
        this.stopAutoRefresh();
    }

    loadStatistics(): void {
        this.loading = true;
        this.error = null;

        this.apiService.getPerformanceStatistics().subscribe({
            next: (data) => {
                this.statistics = data;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading statistics:', err);
                this.error = 'Failed to load performance statistics';
                this.loading = false;
            }
        });
    }

    toggleAutoRefresh(): void {
        this.autoRefresh = !this.autoRefresh;

        if (this.autoRefresh) {
            this.startAutoRefresh();
        } else {
            this.stopAutoRefresh();
        }
    }

    private startAutoRefresh(): void {
        // Refresh every 5 seconds
        this.refreshSubscription = interval(5000)
            .pipe(switchMap(() => this.apiService.getPerformanceStatistics()))
            .subscribe({
                next: (data) => {
                    this.statistics = data;
                },
                error: (err) => {
                    console.error('Auto-refresh error:', err);
                }
            });
    }

    private stopAutoRefresh(): void {
        if (this.refreshSubscription) {
            this.refreshSubscription.unsubscribe();
        }
    }

    getMemoryUsagePercentage(): number {
        if (!this.statistics?.jvm) return 0;
        return Math.round((this.statistics.jvm.memoryUsed / this.statistics.jvm.memoryMax) * 100);
    }

    getConnectionPoolUsagePercentage(): number {
        if (!this.statistics?.database?.connectionPool) return 0;
        const pool = this.statistics.database.connectionPool;
        return Math.round(((pool.active + pool.idle) / pool.max) * 100);
    }

    getStatusColor(value: number, thresholds: { good: number, warning: number }): string {
        if (value <= thresholds.good) return 'status-good';
        if (value <= thresholds.warning) return 'status-warning';
        return 'status-critical';
    }

    getTotalRecords(): number {
        if (!this.statistics?.database?.totalRecords) return 0;
        return Object.values(this.statistics.database.totalRecords).reduce((sum, count) => sum + count, 0);
    }

    getEndpointsArray(): Array<{ name: string, stats: any }> {
        if (!this.statistics?.endpoints) return [];
        return Object.entries(this.statistics.endpoints).map(([name, stats]) => ({ name, stats }));
    }

    formatBytes(bytes: number): string {
        return `${bytes} MB`;
    }

    formatTime(ms: number): string {
        if (ms == null || isNaN(ms)) {
            return 'N/A';
        }
        return `${ms.toFixed(2)} ms`;
    }

    formatPercentage(value: number): string {
        return `${value.toFixed(1)}%`;
    }
}
