import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSubIssueComponent } from './create-sub-issue.component';

describe('CreateSubIssueComponent', () => {
  let component: CreateSubIssueComponent;
  let fixture: ComponentFixture<CreateSubIssueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateSubIssueComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateSubIssueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
