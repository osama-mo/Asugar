import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignToEpicComponent } from './assign-to-epic.component';

describe('AssignToEpicComponent', () => {
  let component: AssignToEpicComponent;
  let fixture: ComponentFixture<AssignToEpicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssignToEpicComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignToEpicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
