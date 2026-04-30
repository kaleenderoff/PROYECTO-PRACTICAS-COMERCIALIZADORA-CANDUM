package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.RecepcionLeche;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecepcionLecheRepositoryPort {

    RecepcionLeche guardar(RecepcionLeche recepcionLeche);

    Optional<RecepcionLeche> obtenerPorId(Long id);

    List<RecepcionLeche> listarTodas();

    List<RecepcionLeche> listarPorFecha(LocalDate fechaRecepcion);

    List<RecepcionLeche> listarPorProveedor(String proveedor);
}