import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { ApiService } from '../../../core/services/api.service';
import { PlannerDTO, ExternalConnectionDTO, FundDTO, SourceNameDTO, RunNameDTO, ReportTypeDTO, ReportNameDTO } from '../../../core/models/dto.models';

@Component({
    selector: 'app-planner-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatIconModule,
        MatDividerModule
    ],
    templateUrl: './planner-form.component.html',
    styleUrl: './planner-form.component.css'
})
export class PlannerFormComponent implements OnInit {
    @Input() planner?: PlannerDTO | null;
    @Input() isInlineMode = true;
    @Output() save = new EventEmitter<PlannerDTO>();
    @Output() cancel = new EventEmitter<void>();

    form!: FormGroup;

    // Master data
    externalConnections: ExternalConnectionDTO[] = [];
    funds: FundDTO[] = [];
    sourceNames: SourceNameDTO[] = [];
    runNames: RunNameDTO[] = [];
    reportTypes: ReportTypeDTO[] = [];
    reportNames: ReportNameDTO[] = [];

    plannerTypes = ['Standard', 'Custom', 'Template'];
    statuses = ['Draft', 'In Progress', 'Finished', 'Failed'];

    constructor(
        private fb: FormBuilder,
        private apiService: ApiService
    ) { }

    ngOnInit(): void {
        this.initForm();
        this.loadMasterData();
        if (this.planner) {
            this.patchFormValue();
        }
    }

    private initForm(): void {
        this.form = this.fb.group({
            name: ['', [Validators.required, Validators.maxLength(255)]],
            description: [''],
            plannerType: ['Standard'],
            status: ['Draft'],
            externalSystemConfigId: [null],
            funds: this.fb.array([]),
            sources: this.fb.array([])
        });
    }

    private loadMasterData(): void {
        // Load all master data in parallel
        this.apiService.getExternalConnections(0, 100).subscribe(response => {
            this.externalConnections = response.content.sort((a, b) => a.name!.localeCompare(b.name!));
        });

        this.apiService.getFunds().subscribe(data => {
            this.funds = data.sort((a, b) => a.name!.localeCompare(b.name!));
        });

        this.apiService.getSourceNames().subscribe(data => {
            this.sourceNames = data.sort((a, b) => a.name!.localeCompare(b.name!));
        });

        this.apiService.getRunNames().subscribe(data => {
            this.runNames = data.sort((a, b) => a.name!.localeCompare(b.name!));
        });

        this.apiService.getReportTypes().subscribe(data => {
            this.reportTypes = data.sort((a, b) => a.name!.localeCompare(b.name!));
        });

        this.apiService.getReportNames().subscribe(data => {
            this.reportNames = data.sort((a, b) => a.name!.localeCompare(b.name!));
        });
    }

    private patchFormValue(): void {
        if (!this.planner) return;

        this.form.patchValue({
            name: this.planner.name,
            description: this.planner.description,
            plannerType: this.planner.plannerType,
            status: this.planner.status,
            externalSystemConfigId: this.planner.externalSystemConfigId
        });

        // Add existing funds
        if (this.planner.funds && this.planner.funds.length > 0) {
            this.planner.funds.forEach(fund => this.addFund(fund));
        }

        // Add existing sources
        if (this.planner.sources && this.planner.sources.length > 0) {
            this.planner.sources.forEach(source => this.addSource(source));
        }
    }

    get fundsArray(): FormArray {
        return this.form.get('funds') as FormArray;
    }

    get sourcesArray(): FormArray {
        return this.form.get('sources') as FormArray;
    }

    addFund(fund?: any): void {
        const fundGroup = this.fb.group({
            id: [fund?.id || null],
            fundId: [fund?.fundId || fund?.fund?.id || null, Validators.required],
            fundAlias: [fund?.fundAlias || '']
        });
        this.fundsArray.push(fundGroup);
    }

    removeFund(index: number): void {
        this.fundsArray.removeAt(index);
    }

    addSource(source?: any): void {
        const sourceGroup = this.fb.group({
            id: [source?.id || null],
            sourceNameId: [source?.sourceNameId || source?.sourceName?.id || null, Validators.required],
            runNameId: [source?.runNameId || source?.runName?.id || null, Validators.required],
            reportTypeId: [source?.reportTypeId || source?.reportType?.id || null, Validators.required],
            reportNameId: [source?.reportNameId || source?.reportName?.id || null, Validators.required],
            displayOrder: [source?.displayOrder || this.sourcesArray.length + 1]
        });
        this.sourcesArray.push(sourceGroup);
    }

    removeSource(index: number): void {
        this.sourcesArray.removeAt(index);
        // Reorder remaining sources
        this.sourcesArray.controls.forEach((control, idx) => {
            control.get('displayOrder')?.setValue(idx + 1);
        });
    }

    onSubmit(): void {
        if (this.form.valid) {
            const plannerData: PlannerDTO = {
                ...this.planner,
                ...this.form.value,
                id: this.planner?.id
            };
            this.save.emit(plannerData);
        }
    }

    onCancel(): void {
        this.cancel.emit();
    }

    getFundName(fundId: number): string {
        return this.funds.find(f => f.id === fundId)?.name || '';
    }
}
