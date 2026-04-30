package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RecepcionLecheRequest {

    @NotNull
    private LocalDate fechaRecepcion;

    @Size(max = 80)
    private String tipoMateriaPrima;

    @NotBlank
    @Size(max = 120)
    private String proveedor;

    @DecimalMin(value = "0.001")
    private BigDecimal cantidadRecibidaLitros;

    @Size(max = 120)
    private String recibidoPor;

    @NotNull
    private Long idTanque;

    @NotNull
    private Long idUsuario;

    @Size(max = 60)
    private String numeroRemision;

    @DecimalMin(value = "0.001")
    private BigDecimal cantidadRemisionLitros;

    @Size(max = 500)
    private String observaciones;

    @Valid
    private List<RecepcionLechePesajeRequest> pesajes;

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getTipoMateriaPrima() {
        return tipoMateriaPrima;
    }

    public void setTipoMateriaPrima(String tipoMateriaPrima) {
        this.tipoMateriaPrima = tipoMateriaPrima;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public BigDecimal getCantidadRecibidaLitros() {
        return cantidadRecibidaLitros;
    }

    public void setCantidadRecibidaLitros(BigDecimal cantidadRecibidaLitros) {
        this.cantidadRecibidaLitros = cantidadRecibidaLitros;
    }

    public String getRecibidoPor() {
        return recibidoPor;
    }

    public void setRecibidoPor(String recibidoPor) {
        this.recibidoPor = recibidoPor;
    }

    public Long getIdTanque() {
        return idTanque;
    }

    public void setIdTanque(Long idTanque) {
        this.idTanque = idTanque;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNumeroRemision() {
        return numeroRemision;
    }

    public void setNumeroRemision(String numeroRemision) {
        this.numeroRemision = numeroRemision;
    }

    public BigDecimal getCantidadRemisionLitros() {
        return cantidadRemisionLitros;
    }

    public void setCantidadRemisionLitros(BigDecimal cantidadRemisionLitros) {
        this.cantidadRemisionLitros = cantidadRemisionLitros;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<RecepcionLechePesajeRequest> getPesajes() {
        return pesajes;
    }

    public void setPesajes(List<RecepcionLechePesajeRequest> pesajes) {
        this.pesajes = pesajes;
    }
}