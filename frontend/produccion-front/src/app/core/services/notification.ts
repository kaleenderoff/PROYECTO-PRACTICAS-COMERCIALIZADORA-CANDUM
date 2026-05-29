import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

export interface ConfirmOptions {
  title: string;
  text: string;
  confirmText?: string;
  cancelText?: string;
  icon?: 'warning' | 'error' | 'success' | 'info' | 'question';
  confirmButtonColor?: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  success(message: string, title: string = 'Éxito'): void {
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

  warning(message: string, title: string = 'Atención'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'warning',
      confirmButtonColor: '#f59e0b'
    });
  }

  info(message: string, title: string = 'Información'): void {
    Swal.fire({
      title,
      text: message,
      icon: 'info',
      confirmButtonColor: '#2563eb'
    });
  }

  async confirm(options: ConfirmOptions): Promise<boolean> {
    const result = await Swal.fire({
      title: options.title,
      text: options.text,
      icon: options.icon || 'warning',
      showCancelButton: true,
      confirmButtonText: options.confirmText || 'Sí, continuar',
      cancelButtonText: options.cancelText || 'Cancelar',
      confirmButtonColor: options.confirmButtonColor || '#dc2626',
      cancelButtonColor: '#64748b',
      reverseButtons: true,
      focusCancel: true
    });

    return result.isConfirmed;
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