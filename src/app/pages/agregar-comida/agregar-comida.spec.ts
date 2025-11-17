import { ComponentFixture, TestBed } from '@angular/core/testing';

import {AgregarComidaComponent } from './agregar-comida';

describe('AgregarComidaComponent', () => {
  let component: AgregarComidaComponent;
  let fixture: ComponentFixture<AgregarComidaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgregarComidaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgregarComidaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
