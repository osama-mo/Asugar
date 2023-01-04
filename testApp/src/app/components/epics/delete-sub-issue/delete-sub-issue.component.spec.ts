import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteSubIssueComponent } from './delete-sub-issue.component';

describe('DeleteSubIssueComponent', () => {
  let component: DeleteSubIssueComponent;
  let fixture: ComponentFixture<DeleteSubIssueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteSubIssueComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteSubIssueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
