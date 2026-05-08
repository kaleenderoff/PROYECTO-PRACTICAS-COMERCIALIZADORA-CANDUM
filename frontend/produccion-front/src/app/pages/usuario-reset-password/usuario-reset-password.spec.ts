import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsuarioResetPassword } from './usuario-reset-password';

describe('UsuarioResetPassword', () => {
  let component: UsuarioResetPassword;
  let fixture: ComponentFixture<UsuarioResetPassword>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsuarioResetPassword],
    }).compileComponents();

    fixture = TestBed.createComponent(UsuarioResetPassword);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
