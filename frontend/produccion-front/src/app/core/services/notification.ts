import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  success(message: string, title: string = 'Exito'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'success',
      confirmButtonColor: '#2563eb',
      timer: 3000,
      timerProgressBar: true
    });
  }

  error(message: string, title: string = 'Error'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'error',
      confirmButtonColor: '#dc2626'
    });
  }

  warning(message: string, title: string = 'Atencion'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'warning',
      confirmButtonColor: '#f59e0b'
    });
  }

  info(message: string, title: string = 'Informacion'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'info',
      confirmButtonColor: '#2563eb'
    });
  }

  toast(message: string, icon: 'success' | 'error' | 'warning' | 'info' = 'success'): void {
    const toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      didOpen: (element) => {
        element.addEventListener('mouseenter', Swal.stopTimer);
        element.addEventListener('mouseleave', Swal.resumeTimer);
      }
    });

    toast.fire({
      icon,
      title: message
    });
  }
}
