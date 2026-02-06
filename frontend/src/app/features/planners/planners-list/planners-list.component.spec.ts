import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlannersListComponent } from './planners-list.component';
import { ApiService } from '../../../core/services/api.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { PageResponse, PlannerDTO } from '../../../core/models/dto.models';
import { provideAnimations } from '@angular/platform-browser/animations';
import { vi } from 'vitest';

describe('PlannersListComponent', () => {
    let component: PlannersListComponent;
    let fixture: ComponentFixture<PlannersListComponent>;
    let apiServiceSpy: any;
    let snackBarSpy: any;

    const mockPageResponse: PageResponse<PlannerDTO> = {
        content: [
            { id: 1, name: 'Plan 1', plannerType: 'Standard', status: 'Draft', description: 'Desc' }
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
            getPlanners: vi.fn(),
            searchPlanners: vi.fn(),
            deletePlanner: vi.fn(),
            createPlanner: vi.fn(),
            updatePlanner: vi.fn()
        };
        snackBarSpy = { open: vi.fn() };

        apiServiceSpy.getPlanners.mockReturnValue(of(mockPageResponse));
        apiServiceSpy.searchPlanners.mockReturnValue(of(mockPageResponse));

        await TestBed.configureTestingModule({
            imports: [PlannersListComponent],
            providers: [
                { provide: ApiService, useValue: apiServiceSpy },
                { provide: MatSnackBar, useValue: snackBarSpy },
                provideAnimations()
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(PlannersListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

        vi.useFakeTimers();
    });

    afterEach(() => {
        vi.useRealTimers();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should load planners on init', () => {
        expect(apiServiceSpy.getPlanners).toHaveBeenCalled();
        expect(component.planners.length).toBe(1);
    });

    it('should search planners when query provided', () => {
        component.searchQuery = 'test';
        component.onSearchChange();
        vi.advanceTimersByTime(300); // Debounce
        expect(apiServiceSpy.searchPlanners).toHaveBeenCalledWith('test', 0, 10, undefined);
    });

    it('should create new planner', () => {
        component.createNew();
        expect(component.newPlanner).toBeTruthy();
        expect(component.expandedPlannerId).toBeNull();
    });

    it('should call createPlanner when saving new planner', () => {
        const newPlanner: PlannerDTO = { name: 'New', plannerType: 'Standard', status: 'Draft' };

        apiServiceSpy.createPlanner.mockReturnValue(of(newPlanner));

        component.savePlanner(newPlanner);

        expect(apiServiceSpy.createPlanner).toHaveBeenCalledWith(newPlanner);
    });

    it('should call updatePlanner when saving existing planner', () => {
        const existingPlanner: PlannerDTO = { id: 1, name: 'Updated', plannerType: 'Standard', status: 'Draft' };

        apiServiceSpy.updatePlanner.mockReturnValue(of(existingPlanner));

        component.savePlanner(existingPlanner);

        expect(apiServiceSpy.updatePlanner).toHaveBeenCalledWith(1, existingPlanner);
    });

    it('should copy planner with modified name', () => {
        const plannerToCopy = mockPageResponse.content[0];

        apiServiceSpy.createPlanner.mockReturnValue(of({ ...plannerToCopy, name: 'Plan 1 (Copy)' }));

        component.copyPlanner(plannerToCopy);

        expect(apiServiceSpy.createPlanner).toHaveBeenCalledWith(expect.objectContaining({
            name: 'Plan 1 (Copy)'
        }));
    });

    it('should call deletePlanner when deleting planner', () => {
        vi.spyOn(window, 'confirm').mockReturnValueOnce(true);
        const plannerToDelete = mockPageResponse.content[0];

        apiServiceSpy.deletePlanner.mockReturnValue(of(void 0));

        component.deletePlanner(plannerToDelete);

        expect(apiServiceSpy.deletePlanner).toHaveBeenCalledWith(1);
    });
});
