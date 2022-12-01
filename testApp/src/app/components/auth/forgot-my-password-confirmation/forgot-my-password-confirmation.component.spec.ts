import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotMyPasswordConfirmationComponent } from './forgot-my-password-confirmation.component';

describe('ForgotMyPasswordConfirmationComponent', () => {
  let component: ForgotMyPasswordConfirmationComponent;
  let fixture: ComponentFixture<ForgotMyPasswordConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ForgotMyPasswordConfirmationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForgotMyPasswordConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
