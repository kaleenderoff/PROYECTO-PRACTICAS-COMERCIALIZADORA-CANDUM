package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RecepcionLeche {

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
    private List<RecepcionLechePesaje> pesajes;

    public RecepcionLeche() {
    }

    public RecepcionLeche(Long id, LocalDate fechaRecepcion, String tipoMateriaPrima, String proveedor,
            BigDecimal cantidadRecibidaLitros, String recibidoPor, Long idTanque, Long idUsuario,
            Long idMovimientoLeche, String numeroRemision, BigDecimal cantidadRemisionLitros,
            String observaciones, List<RecepcionLechePesaje> pesajes) {
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

    public List<RecepcionLechePesaje> getPesajes() {
        return pesajes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public void setTipoMateriaPrima(String tipoMateriaPrima) {
        this.tipoMateriaPrima = tipoMateriaPrima;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setCantidadRecibidaLitros(BigDecimal cantidadRecibidaLitros) {
        this.cantidadRecibidaLitros = cantidadRecibidaLitros;
    }

    public void setRecibidoPor(String recibidoPor) {
        this.recibidoPor = recibidoPor;
    }

    public void setIdTanque(Long idTanque) {
        this.idTanque = idTanque;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdMovimientoLeche(Long idMovimientoLeche) {
        this.idMovimientoLeche = idMovimientoLeche;
    }

    public void setNumeroRemision(String numeroRemision) {
        this.numeroRemision = numeroRemision;
    }

    public void setCantidadRemisionLitros(BigDecimal cantidadRemisionLitros) {
        this.cantidadRemisionLitros = cantidadRemisionLitros;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setPesajes(List<RecepcionLechePesaje> pesajes) {
        this.pesajes = pesajes;
    }
}