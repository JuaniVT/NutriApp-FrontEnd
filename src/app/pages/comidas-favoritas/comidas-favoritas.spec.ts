import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComidasFavoritasComponent } from './comidas-favoritas';

describe('ComidasFavoritasComponent', () => {
  let component: ComidasFavoritasComponent;
  let fixture: ComponentFixture<ComidasFavoritasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComidasFavoritasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComidasFavoritasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
