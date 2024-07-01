import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientsMenuAdderComponent } from './ingredients-menu-adder.component';

describe('IngredientsMenuAdderComponent', () => {
  let component: IngredientsMenuAdderComponent;
  let fixture: ComponentFixture<IngredientsMenuAdderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngredientsMenuAdderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(IngredientsMenuAdderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
