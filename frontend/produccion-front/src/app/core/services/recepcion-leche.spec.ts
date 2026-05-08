import { TestBed } from '@angular/core/testing';

import { RecepcionLeche } from './recepcion-leche';

describe('RecepcionLeche', () => {
  let service: RecepcionLeche;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecepcionLeche);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
