import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProduccionLactea } from './produccion-lactea';

describe('ProduccionLactea', () => {
  let component: ProduccionLactea;
  let fixture: ComponentFixture<ProduccionLactea>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProduccionLactea],
    }).compileComponents();

    fixture = TestBed.createComponent(ProduccionLactea);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
