import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdenProduccionDetalle } from './orden-produccion-detalle';

describe('OrdenProduccionDetalle', () => {
  let component: OrdenProduccionDetalle;
  let fixture: ComponentFixture<OrdenProduccionDetalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrdenProduccionDetalle],
    }).compileComponents();

    fixture = TestBed.createComponent(OrdenProduccionDetalle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
