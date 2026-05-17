package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.MedicionCalidadLactea;

import java.util.List;
import java.util.Optional;

public interface MedicionCalidadLacteaRepositoryPort {

    MedicionCalidadLactea guardar(MedicionCalidadLactea medicion);

    Optional<MedicionCalidadLactea> obtenerPorId(Long id);

    List<MedicionCalidadLactea> listarPorProduccion(Long idProduccionLactea);

    List<MedicionCalidadLactea> listarPorOrden(Long idOrdenProduccion);
}
