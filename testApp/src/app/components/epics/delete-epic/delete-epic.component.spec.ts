import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteEpicComponent } from './delete-epic.component';

describe('DeleteEpicComponent', () => {
  let component: DeleteEpicComponent;
  let fixture: ComponentFixture<DeleteEpicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteEpicComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteEpicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
