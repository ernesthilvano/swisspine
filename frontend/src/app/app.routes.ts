import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/connections',
        pathMatch: 'full'
    },
    {
        path: 'connections',
        loadComponent: () => import('./features/external-connections/connections-list/connections-list.component')
            .then(m => m.ConnectionsListComponent)
    },
    {
        path: 'planners',
        loadComponent: () => import('./features/planners/planners-list/planners-list.component')
            .then(m => m.PlannersListComponent)
    },
    {
        path: 'planners/:id',
        loadComponent: () => import('./features/planners/planner-detail/planner-detail.component')
            .then(m => m.PlannerDetailComponent)
    },
    {
        path: 'statistics',
        loadComponent: () => import('./features/statistics/statistics-dashboard/statistics-dashboard.component')
            .then(m => m.StatisticsDashboardComponent)
    },
    {
        path: '**',
        redirectTo: '/connections'
    }
];
