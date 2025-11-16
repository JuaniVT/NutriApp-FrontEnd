import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgregarComida } from './agregar-comida';

describe('AgregarComida', () => {
  let component: AgregarComida;
  let fixture: ComponentFixture<AgregarComida>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgregarComida]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgregarComida);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
