import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DescremadoForm } from './descremado-form';

describe('DescremadoForm', () => {
  let component: DescremadoForm;
  let fixture: ComponentFixture<DescremadoForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DescremadoForm],
    }).compileComponents();

    fixture = TestBed.createComponent(DescremadoForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
