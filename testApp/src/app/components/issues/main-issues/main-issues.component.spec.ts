import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainIssuesComponent } from './main-issues.component';

describe('MainIssuesComponent', () => {
  let component: MainIssuesComponent;
  let fixture: ComponentFixture<MainIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MainIssuesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MainIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
