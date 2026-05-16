import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor() { }

  success(message: string, title: string = '¡Éxito!'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'success',
      confirmButtonColor: '#2563eb', // blue-600
      timer: 3000,
      timerProgressBar: true
    });
  }

  error(message: string, title: string = 'Error'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'error',
      confirmButtonColor: '#dc2626', // red-600
    });
  }

  warning(message: string, title: string = 'Atención'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'warning',
      confirmButtonColor: '#f59e0b', // amber-500
    });
  }

  info(message: string, title: string = 'Información'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'info',
      confirmButtonColor: '#2563eb',
    });
  }

  toast(message: string, icon: 'success' | 'error' | 'warning' | 'info' = 'success'): void {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer);
        toast.addEventListener('mouseleave', Swal.resumeTimer);
      }
    });

    Toast.fire({
      icon,
      title: message
    });
  }
}
