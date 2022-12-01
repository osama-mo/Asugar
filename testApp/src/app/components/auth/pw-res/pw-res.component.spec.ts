import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PwResComponent } from './pw-res.component';

describe('PwResComponent', () => {
  let component: PwResComponent;
  let fixture: ComponentFixture<PwResComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PwResComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PwResComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
