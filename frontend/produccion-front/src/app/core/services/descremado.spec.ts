import { TestBed } from '@angular/core/testing';

import { Descremado } from './descremado';

describe('Descremado', () => {
  let service: Descremado;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Descremado);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
