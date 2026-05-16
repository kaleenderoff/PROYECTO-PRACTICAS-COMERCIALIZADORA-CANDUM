import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EjecucionProduccion } from './ejecucion-produccion';

describe('EjecucionProduccion', () => {

  let component: EjecucionProduccion;
  let fixture: ComponentFixture<EjecucionProduccion>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [EjecucionProduccion],
    }).compileComponents();

    fixture = TestBed.createComponent(EjecucionProduccion);

    component = fixture.componentInstance;

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
