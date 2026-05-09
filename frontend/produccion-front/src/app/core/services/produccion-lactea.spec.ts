import { TestBed } from '@angular/core/testing';

import { ProduccionLactea } from './produccion-lactea';

describe('ProduccionLactea', () => {
  let service: ProduccionLactea;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProduccionLactea);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
