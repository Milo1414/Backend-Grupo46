package tpi_grupo46.logistica.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpi_grupo46.logistica.client.RecursosClient;
import tpi_grupo46.logistica.dto.CamionDTO;
import tpi_grupo46.logistica.dto.DepositoDTO;
import tpi_grupo46.logistica.dto.TarifaDTO;

import java.util.List;

/**
 * Servicio de ejemplo que consume datos del microservicio de recursos.
 * 
 * Este servicio demuestra cómo usar el RecursosClient para obtener información
 * de camiones, depósitos y tarifas desde el microservicio de recursos.
 */
@Service
@AllArgsConstructor
@Slf4j
public class RecursosIntegrationService {

    private final RecursosClient recursosClient;

    /**
     * Obtiene todos los camiones disponibles desde el microservicio de recursos.
     */
    public List<CamionDTO> obtenerCamionesDisponibles() {
        log.info("Obteniendo camiones desde microservicio de recursos");
        try {
            return recursosClient.obtenerTodosLosCamiones(CamionDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener camiones: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de recursos", e);
        }
    }

    /**
     * Obtiene un camión específico por su dominio.
     */
    public CamionDTO obtenerCamionPorDominio(String dominio) {
        log.info("Obteniendo camión con dominio: {}", dominio);
        try {
            return recursosClient.obtenerCamionPorDominio(dominio, CamionDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener camión {}: {}", dominio, e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de recursos", e);
        }
    }

    /**
     * Obtiene todos los depósitos desde el microservicio de recursos.
     */
    public List<DepositoDTO> obtenerDepositos() {
        log.info("Obteniendo depósitos desde microservicio de recursos");
        try {
            return recursosClient.obtenerTodosLosDepositos(DepositoDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener depósitos: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de recursos", e);
        }
    }

    /**
     * Obtiene un depósito específico por su ID.
     */
    public DepositoDTO obtenerDepositoPorId(Integer id) {
        log.info("Obteniendo depósito con ID: {}", id);
        try {
            return recursosClient.obtenerDepositoPorId(id, DepositoDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener depósito {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de recursos", e);
        }
    }

    /**
     * Obtiene todas las tarifas desde el microservicio de recursos.
     */
    public List<TarifaDTO> obtenerTarifas() {
        log.info("Obteniendo tarifas desde microservicio de recursos");
        try {
            return recursosClient.obtenerTodasLasTarifas(TarifaDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener tarifas: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de recursos", e);
        }
    }

    /**
     * Obtiene una tarifa específica por su ID.
     */
    public TarifaDTO obtenerTarifaPorId(Integer id) {
        log.info("Obteniendo tarifa con ID: {}", id);
        try {
            return recursosClient.obtenerTarifaPorId(id, TarifaDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener tarifa {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de recursos", e);
        }
    }
}
