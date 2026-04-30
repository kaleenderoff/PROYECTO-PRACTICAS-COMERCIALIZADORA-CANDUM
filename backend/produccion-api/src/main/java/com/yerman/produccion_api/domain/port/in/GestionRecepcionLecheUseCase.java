package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.RecepcionLeche;

import java.time.LocalDate;
import java.util.List;

public interface GestionRecepcionLecheUseCase {

    RecepcionLeche registrarRecepcion(RecepcionLeche recepcionLeche);

    RecepcionLeche obtenerPorId(Long id);

    List<RecepcionLeche> listarTodas();

    List<RecepcionLeche> listarPorFecha(LocalDate fechaRecepcion);

    List<RecepcionLeche> listarPorProveedor(String proveedor);
}