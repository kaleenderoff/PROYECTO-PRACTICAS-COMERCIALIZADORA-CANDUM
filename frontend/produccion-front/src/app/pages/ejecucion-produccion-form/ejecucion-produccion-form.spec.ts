import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EjecucionProduccionForm } from './ejecucion-produccion-form';

describe('EjecucionProduccionForm', () => {

  let component: EjecucionProduccionForm;
  let fixture: ComponentFixture<EjecucionProduccionForm>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [EjecucionProduccionForm],
    }).compileComponents();

    fixture = TestBed.createComponent(EjecucionProduccionForm);

    component = fixture.componentInstance;

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
