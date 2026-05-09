import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Descremado } from './descremado';

describe('Descremado', () => {
  let component: Descremado;
  let fixture: ComponentFixture<Descremado>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Descremado],
    }).compileComponents();

    fixture = TestBed.createComponent(Descremado);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
