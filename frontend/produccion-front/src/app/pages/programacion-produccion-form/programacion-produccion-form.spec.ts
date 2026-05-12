import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramacionProduccionForm } from './programacion-produccion-form';

describe('ProgramacionProduccionForm', () => {
  let component: ProgramacionProduccionForm;
  let fixture: ComponentFixture<ProgramacionProduccionForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgramacionProduccionForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ProgramacionProduccionForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
