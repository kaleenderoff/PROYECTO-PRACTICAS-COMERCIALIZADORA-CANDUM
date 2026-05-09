package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class SimularProgramacionRequest {

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El idProducto debe ser mayor que cero")
    private Long idProducto;

    @Valid
    @NotEmpty(message = "Debe enviar al menos un SKU")
    private List<SimularSkuRequest> skus;

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public List<SimularSkuRequest> getSkus() {
        return skus;
    }

    public void setSkus(List<SimularSkuRequest> skus) {
        this.skus = skus;
    }

    public static class SimularSkuRequest {

        @NotNull(message = "El SKU es obligatorio")
        @Positive(message = "El idSku debe ser mayor que cero")
        private Long idSku;

        @NotNull(message = "Las unidades son obligatorias")
        @Positive(message = "Las unidades deben ser mayores que cero")
        private Integer unidades;

        public Long getIdSku() {
            return idSku;
        }

        public void setIdSku(Long idSku) {
            this.idSku = idSku;
        }

        public Integer getUnidades() {
            return unidades;
        }

        public void setUnidades(Integer unidades) {
            this.unidades = unidades;
        }
    }
}