package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.RecepcionLeche;
import com.yerman.produccion_api.domain.model.RecepcionLechePesaje;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
import com.yerman.produccion_api.domain.port.in.GestionRecepcionLecheUseCase;
import com.yerman.produccion_api.domain.port.out.RecepcionLecheRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GestionRecepcionLecheService implements GestionRecepcionLecheUseCase {

    private static final String TIPO_MATERIA_PRIMA_DEFAULT = "LECHE CRUDA";
    private static final BigDecimal FACTOR_KG_POR_LITRO = new BigDecimal("1.03");

    private final RecepcionLecheRepositoryPort recepcionRepository;
    private final GestionMovimientoLecheUseCase movimientoLecheUseCase;

    public GestionRecepcionLecheService(
            RecepcionLecheRepositoryPort recepcionRepository,
            GestionMovimientoLecheUseCase movimientoLecheUseCase) {
        this.recepcionRepository = recepcionRepository;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
    }

    @Override
    @Transactional
    public RecepcionLeche registrarRecepcion(RecepcionLeche recepcionLeche) {
        validarRecepcion(recepcionLeche);

        if (recepcionLeche.getTipoMateriaPrima() == null || recepcionLeche.getTipoMateriaPrima().isBlank()) {
            recepcionLeche.setTipoMateriaPrima(TIPO_MATERIA_PRIMA_DEFAULT);
        }

        BigDecimal litrosCalculadosDesdePesajes = calcularLitrosDesdePesajes(recepcionLeche.getPesajes());

        if (litrosCalculadosDesdePesajes.compareTo(BigDecimal.ZERO) > 0) {
            recepcionLeche.setCantidadRecibidaLitros(litrosCalculadosDesdePesajes);
        }

        MovimientoLeche movimiento = movimientoLecheUseCase.registrarMovimiento(
                recepcionLeche.getIdTanque(),
                TipoMovimientoLeche.ENTRADA_RECEPCION,
                recepcionLeche.getCantidadRecibidaLitros(),
                recepcionLeche.getIdUsuario(),
                construirReferencia(recepcionLeche),
                recepcionLeche.getObservaciones());

        recepcionLeche.setIdMovimientoLeche(movimiento.getId());

        return recepcionRepository.guardar(recepcionLeche);
    }

    @Override
    public RecepcionLeche obtenerPorId(Long id) {
        return recepcionRepository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la recepción de leche con ID: " + id));
    }

    @Override
    public List<RecepcionLeche> listarTodas() {
        return recepcionRepository.listarTodas();
    }

    @Override
    public List<RecepcionLeche> listarPorFecha(LocalDate fechaRecepcion) {
        return recepcionRepository.listarPorFecha(fechaRecepcion);
    }

    @Override
    public List<RecepcionLeche> listarPorProveedor(String proveedor) {
        return recepcionRepository.listarPorProveedor(proveedor);
    }

    private void validarRecepcion(RecepcionLeche recepcionLeche) {
        if (recepcionLeche == null) {
            throw new ReglaNegocioException("La recepción de leche es obligatoria.");
        }

        if (recepcionLeche.getFechaRecepcion() == null) {
            throw new ReglaNegocioException("La fecha de recepción es obligatoria.");
        }

        if (recepcionLeche.getProveedor() == null || recepcionLeche.getProveedor().isBlank()) {
            throw new ReglaNegocioException("El proveedor es obligatorio.");
        }

        if (recepcionLeche.getIdUsuario() == null) {
            throw new ReglaNegocioException("El usuario que registra la recepción es obligatorio.");
        }

        if (recepcionLeche.getIdTanque() == null) {
            throw new ReglaNegocioException("El tanque destino de la leche es obligatorio.");
        }

        boolean tienePesajes = recepcionLeche.getPesajes() != null && !recepcionLeche.getPesajes().isEmpty();

        if (!tienePesajes &&
                (recepcionLeche.getCantidadRecibidaLitros() == null
                        || recepcionLeche.getCantidadRecibidaLitros().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new ReglaNegocioException(
                    "Debe registrar una cantidad recibida mayor que cero o al menos un pesaje.");
        }

        if (tienePesajes) {
            validarPesajes(recepcionLeche.getPesajes());
        }

        if (recepcionLeche.getCantidadRecibidaLitros() != null
                && recepcionLeche.getCantidadRecibidaLitros().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La cantidad recibida debe ser mayor que cero.");
        }
    }

    private void validarPesajes(List<RecepcionLechePesaje> pesajes) {
        for (RecepcionLechePesaje pesaje : pesajes) {
            if (pesaje == null) {
                throw new ReglaNegocioException("El detalle de pesaje no puede ser nulo.");
            }

            if (pesaje.getNumeroPesaje() == null || pesaje.getNumeroPesaje() <= 0) {
                throw new ReglaNegocioException("Cada pesaje debe tener un número de pesaje válido.");
            }

            if (pesaje.getPesoBrutoKg() == null || pesaje.getPesoBrutoKg().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ReglaNegocioException("Cada pesaje debe tener un peso bruto mayor que cero.");
            }

            if (pesaje.getTaraKg() == null || pesaje.getTaraKg().compareTo(BigDecimal.ZERO) < 0) {
                throw new ReglaNegocioException("Cada pesaje debe tener una tara válida.");
            }

            BigDecimal pesoNetoCalculado = pesaje.getPesoBrutoKg().subtract(pesaje.getTaraKg());

            if (pesoNetoCalculado.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ReglaNegocioException("El peso neto del pesaje debe ser mayor que cero.");
            }

            pesaje.setPesoNetoKg(pesoNetoCalculado);
        }
    }

    private BigDecimal calcularLitrosDesdePesajes(List<RecepcionLechePesaje> pesajes) {
        BigDecimal totalPesoNetoKg = calcularTotalPesoNetoKg(pesajes);

        if (totalPesoNetoKg.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return totalPesoNetoKg.divide(FACTOR_KG_POR_LITRO, 3, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularTotalPesoNetoKg(List<RecepcionLechePesaje> pesajes) {
        if (pesajes == null || pesajes.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return pesajes.stream()
                .map(RecepcionLechePesaje::getPesoNetoKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String construirReferencia(RecepcionLeche recepcionLeche) {
        String proveedor = recepcionLeche.getProveedor() == null
                ? "Proveedor no especificado"
                : recepcionLeche.getProveedor();

        String remision = recepcionLeche.getNumeroRemision() == null || recepcionLeche.getNumeroRemision().isBlank()
                ? "sin remisión"
                : "remisión " + recepcionLeche.getNumeroRemision();

        return "Recepción leche - " + proveedor + " - " + remision;
    }
}