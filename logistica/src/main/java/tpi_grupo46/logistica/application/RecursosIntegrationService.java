package tpi_grupo46.logistica.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpi_grupo46.logistica.client.RecursosClient;
import tpi_grupo46.logistica.dto.CamionDTO;
import tpi_grupo46.logistica.dto.DepositoDTO;
import tpi_grupo46.logistica.dto.TarifaDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
            return recursosClient.getCamiones().stream()
                    .map(this::convertirMapACamionDTO)
                    .toList();
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
            return convertirMapACamionDTO(recursosClient.getCamion(dominio));
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
            return recursosClient.getDepositos().stream()
                    .map(this::convertirMapADepositoDTO)
                    .toList();
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
            return convertirMapADepositoDTO(recursosClient.getDeposito(id));
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
            return recursosClient.getTarifas().stream()
                    .map(this::convertirMapATarifaDTO)
                    .toList();
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
            return convertirMapATarifaDTO(recursosClient.getTarifa(id));
        } catch (Exception e) {
            log.error("Error al obtener tarifa {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de recursos", e);
        }
    }

    // ==================== MÉTODOS AUXILIARES DE CONVERSIÓN ====================

    private CamionDTO convertirMapACamionDTO(Map<String, Object> map) {
        if (map == null)
            return null;
        CamionDTO dto = new CamionDTO();
        dto.setDominioCamion((String) map.get("dominio_camion"));
        dto.setCapacidadPeso(new BigDecimal(map.get("capacidad_peso").toString()));
        dto.setCapacidadVolumen(new BigDecimal(map.get("capacidad_volumen").toString()));
        dto.setDisponibilidad((Boolean) map.get("disponibilidad"));
        dto.setNombreTransportista((String) map.get("nombre_transportista"));
        dto.setTelefonoTransportista(map.get("telefono_transportista").toString());
        dto.setCostoBaseKm(new BigDecimal(map.get("costo_base_km").toString()));
        dto.setConsumoLKm(new BigDecimal(map.get("consumo_l_km").toString()));
        return dto;
    }

    private DepositoDTO convertirMapADepositoDTO(Map<String, Object> map) {
        if (map == null)
            return null;
        DepositoDTO dto = new DepositoDTO();
        dto.setIdDeposito(((Number) map.get("id_deposito")).intValue());
        dto.setLatitudDeposito(new BigDecimal(map.get("latitud_deposito").toString()));
        dto.setLongitudDeposito(new BigDecimal(map.get("longitud_deposito").toString()));
        dto.setCalleDeposito((String) map.get("calle_deposito"));
        dto.setNroDeposito(((Number) map.get("nro_deposito")).intValue());
        dto.setCostoDiaTransportista(new BigDecimal(map.get("costo_dia_transportista").toString()));
        return dto;
    }

    private TarifaDTO convertirMapATarifaDTO(Map<String, Object> map) {
        if (map == null)
            return null;
        TarifaDTO dto = new TarifaDTO();
        dto.setIdTarifa(((Number) map.get("id_tarifa")).intValue());
        dto.setDescripcion((String) map.get("descripcion"));
        dto.setValor(new BigDecimal(map.get("valor").toString()));
        dto.setCostoKmBase(new BigDecimal(map.get("costo_km_base").toString()));
        dto.setValorLitroCombustible(new BigDecimal(map.get("valor_litro_combustible").toString()));
        return dto;
    }
}
