package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.RecepcionLechePesaje;

import java.util.List;

public interface RecepcionLechePesajeRepositoryPort {

    RecepcionLechePesaje guardar(RecepcionLechePesaje pesaje);

    List<RecepcionLechePesaje> listarPorRecepcion(Long idRecepcionLeche);
}