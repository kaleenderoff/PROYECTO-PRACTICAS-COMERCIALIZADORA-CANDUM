package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RecepcionLecheResponse {

    private Long id;
    private LocalDate fechaRecepcion;
    private String tipoMateriaPrima;
    private String proveedor;
    private BigDecimal cantidadRecibidaLitros;
    private String recibidoPor;
    private Long idTanque;
    private Long idUsuario;
    private Long idMovimientoLeche;
    private String numeroRemision;
    private BigDecimal cantidadRemisionLitros;
    private String observaciones;
    private List<RecepcionLechePesajeResponse> pesajes;

    public RecepcionLecheResponse(Long id, LocalDate fechaRecepcion, String tipoMateriaPrima,
            String proveedor, BigDecimal cantidadRecibidaLitros, String recibidoPor,
            Long idTanque, Long idUsuario, Long idMovimientoLeche, String numeroRemision,
            BigDecimal cantidadRemisionLitros, String observaciones,
            List<RecepcionLechePesajeResponse> pesajes) {
        this.id = id;
        this.fechaRecepcion = fechaRecepcion;
        this.tipoMateriaPrima = tipoMateriaPrima;
        this.proveedor = proveedor;
        this.cantidadRecibidaLitros = cantidadRecibidaLitros;
        this.recibidoPor = recibidoPor;
        this.idTanque = idTanque;
        this.idUsuario = idUsuario;
        this.idMovimientoLeche = idMovimientoLeche;
        this.numeroRemision = numeroRemision;
        this.cantidadRemisionLitros = cantidadRemisionLitros;
        this.observaciones = observaciones;
        this.pesajes = pesajes;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public String getTipoMateriaPrima() {
        return tipoMateriaPrima;
    }

    public String getProveedor() {
        return proveedor;
    }

    public BigDecimal getCantidadRecibidaLitros() {
        return cantidadRecibidaLitros;
    }

    public String getRecibidoPor() {
        return recibidoPor;
    }

    public Long getIdTanque() {
        return idTanque;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdMovimientoLeche() {
        return idMovimientoLeche;
    }

    public String getNumeroRemision() {
        return numeroRemision;
    }

    public BigDecimal getCantidadRemisionLitros() {
        return cantidadRemisionLitros;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<RecepcionLechePesajeResponse> getPesajes() {
        return pesajes;
    }
}
