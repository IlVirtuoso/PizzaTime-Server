import { TestBed } from '@angular/core/testing';

import { DataBridgeService } from './data-bridge.service';

describe('DataBridgeService', () => {
  let service: DataBridgeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DataBridgeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
