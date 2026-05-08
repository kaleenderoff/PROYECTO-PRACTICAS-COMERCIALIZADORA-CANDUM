import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecepcionLecheForm } from './recepcion-leche-form';

describe('RecepcionLecheForm', () => {
  let component: RecepcionLecheForm;
  let fixture: ComponentFixture<RecepcionLecheForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecepcionLecheForm],
    }).compileComponents();

    fixture = TestBed.createComponent(RecepcionLecheForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
