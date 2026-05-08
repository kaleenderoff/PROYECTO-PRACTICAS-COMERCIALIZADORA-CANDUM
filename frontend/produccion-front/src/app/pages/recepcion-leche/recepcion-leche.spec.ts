import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecepcionLeche } from './recepcion-leche';

describe('RecepcionLeche', () => {
  let component: RecepcionLeche;
  let fixture: ComponentFixture<RecepcionLeche>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecepcionLeche],
    }).compileComponents();

    fixture = TestBed.createComponent(RecepcionLeche);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
