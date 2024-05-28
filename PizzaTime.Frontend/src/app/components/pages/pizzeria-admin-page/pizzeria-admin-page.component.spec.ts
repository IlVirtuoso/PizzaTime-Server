import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PizzeriaAdminPageComponent } from './pizzeria-admin-page.component';

describe('PizzeriaAdminPageComponent', () => {
  let component: PizzeriaAdminPageComponent;
  let fixture: ComponentFixture<PizzeriaAdminPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PizzeriaAdminPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PizzeriaAdminPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
