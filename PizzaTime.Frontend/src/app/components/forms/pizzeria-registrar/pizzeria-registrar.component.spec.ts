import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PizzeriaRegistrarComponent } from './pizzeria-registrar.component';

describe('PizzeriaRegistrarComponent', () => {
  let component: PizzeriaRegistrarComponent;
  let fixture: ComponentFixture<PizzeriaRegistrarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PizzeriaRegistrarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PizzeriaRegistrarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
