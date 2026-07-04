import { TestBed } from '@angular/core/testing';

import { HidratacionService } from './hidratacion-service';

describe('HidratacionService', () => {
  let service: HidratacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HidratacionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
