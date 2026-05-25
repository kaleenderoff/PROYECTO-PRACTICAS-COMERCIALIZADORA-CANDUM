package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.FinalizarBatchRequest;
import com.yerman.produccion_api.application.dto.request.IniciarBatchRequest;
import com.yerman.produccion_api.application.dto.response.EjecucionBatchResponse;
import com.yerman.produccion_api.application.mapper.EjecucionBatchRestMapper;
import com.yerman.produccion_api.domain.model.EjecucionBatch;
import com.yerman.produccion_api.domain.port.in.GestionEjecucionBatchUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ejecucion-batch")
public class EjecucionBatchController {

    private final GestionEjecucionBatchUseCase useCase;

    public EjecucionBatchController(GestionEjecucionBatchUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/iniciar")
    public EjecucionBatchResponse iniciar(@RequestBody @Valid IniciarBatchRequest request) {
        return EjecucionBatchRestMapper.toResponse(
                useCase.iniciarBatch(
                        request.idOrdenProduccion(),
                        request.idMarmita(),
                        request.kgEntrada()
                )
        );
    }

    @PatchMapping("/{id}/finalizar")
    public EjecucionBatchResponse finalizar(@PathVariable Long id, @RequestBody @Valid FinalizarBatchRequest request) {
        EjecucionBatch batch = useCase.finalizarBatch(id, request.kgProducidos(), request.observaciones(), 
                request.conNovedad(), request.huboReproceso(), request.batchConforme(), request.brixFinal());
        return EjecucionBatchRestMapper.toResponse(batch);
    }

    @GetMapping("/orden/{idOrden}")
    public List<EjecucionBatchResponse> listarPorOrden(@PathVariable Long idOrden) {
        return useCase.listarBatchesPorOrden(idOrden).stream()
                .map(EjecucionBatchRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public EjecucionBatchResponse obtener(@PathVariable Long id) {
        return EjecucionBatchRestMapper.toResponse(useCase.obtenerBatch(id));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        useCase.eliminarBatch(id);
    }
}
