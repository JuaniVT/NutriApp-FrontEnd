import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerDiaComponent } from './ver-dia-component';

describe('VerDiaComponent', () => {
  let component: VerDiaComponent;
  let fixture: ComponentFixture<VerDiaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerDiaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerDiaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
