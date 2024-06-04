import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PizzaPartyPageComponent } from './pizza-party-page.component';

describe('PizzaPartyPageComponent', () => {
  let component: PizzaPartyPageComponent;
  let fixture: ComponentFixture<PizzaPartyPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PizzaPartyPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PizzaPartyPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
