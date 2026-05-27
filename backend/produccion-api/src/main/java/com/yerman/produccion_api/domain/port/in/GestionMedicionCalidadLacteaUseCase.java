package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.MedicionCalidadLactea;

import java.util.List;

public interface GestionMedicionCalidadLacteaUseCase {

    MedicionCalidadLactea registrar(MedicionCalidadLactea medicion);

    MedicionCalidadLactea actualizar(Long id, MedicionCalidadLactea medicion);

    void eliminar(Long id);

    MedicionCalidadLactea obtenerPorId(Long id);

    List<MedicionCalidadLactea> listarPorProduccion(Long idProduccionLactea);

    List<MedicionCalidadLactea> listarPorOrden(Long idOrdenProduccion);
}
