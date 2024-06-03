import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PizzaListViewComponent } from './pizza-list-view.component';

describe('PizzaListViewComponent', () => {
  let component: PizzaListViewComponent;
  let fixture: ComponentFixture<PizzaListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PizzaListViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PizzaListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
