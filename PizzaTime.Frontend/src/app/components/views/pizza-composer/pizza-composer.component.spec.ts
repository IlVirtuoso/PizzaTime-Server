import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PizzaComposerComponent } from './pizza-composer.component';

describe('PizzaComposerComponent', () => {
  let component: PizzaComposerComponent;
  let fixture: ComponentFixture<PizzaComposerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PizzaComposerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PizzaComposerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
