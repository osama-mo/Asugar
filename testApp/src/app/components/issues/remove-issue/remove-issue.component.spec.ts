import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveIssueComponent } from './remove-issue.component';

describe('RemoveIssueComponent', () => {
  let component: RemoveIssueComponent;
  let fixture: ComponentFixture<RemoveIssueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RemoveIssueComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemoveIssueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
