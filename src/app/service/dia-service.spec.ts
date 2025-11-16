import { TestBed } from '@angular/core/testing';

import { DiaService } from './dia-service';

describe('DiaService', () => {
  let service: DiaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
