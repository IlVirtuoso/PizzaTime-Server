import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PizzaMenuAdderComponent } from './pizza-menu-adder.component';

describe('PizzaMenuAdderComponent', () => {
  let component: PizzaMenuAdderComponent;
  let fixture: ComponentFixture<PizzaMenuAdderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PizzaMenuAdderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PizzaMenuAdderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
