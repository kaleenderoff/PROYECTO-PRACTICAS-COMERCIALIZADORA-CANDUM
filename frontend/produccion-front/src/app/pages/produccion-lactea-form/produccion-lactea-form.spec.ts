import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProduccionLacteaForm } from './produccion-lactea-form';

describe('ProduccionLacteaForm', () => {
  let component: ProduccionLacteaForm;
  let fixture: ComponentFixture<ProduccionLacteaForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProduccionLacteaForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ProduccionLacteaForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
