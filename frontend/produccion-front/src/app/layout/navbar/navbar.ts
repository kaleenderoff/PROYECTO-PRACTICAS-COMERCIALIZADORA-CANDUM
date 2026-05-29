import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {

  @Output() menuClick = new EventEmitter<void>();

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  abrirMenu(): void {
    this.menuClick.emit();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}