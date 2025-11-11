package tpi_grupo46.logistica.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tpi_grupo46.logistica.dto.DistanciaDTO;

/**
 * Servicio que consume la API de Google Maps Distance Matrix
 * Calcula distancias y duraciones entre dos puntos geográficos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeoService {

    @Value("${google.maps.apikey}")
    private String apiKey;

    private final RestClient.Builder builder;

    /**
     * Calcula la distancia y duración entre dos ubicaciones
     * 
     * @param origen  ubicación de origen (ej: "-34.603722,-58.381592" o "Buenos
     *                Aires, Argentina")
     * @param destino ubicación de destino (ej: "-34.615851,-58.433238" o "La Plata,
     *                Argentina")
     * @return DistanciaDTO con la información calculada
     * @throws Exception si hay error en la petición o parseo de respuesta
     */
    public DistanciaDTO calcularDistancia(String origen, String destino) throws Exception {
        log.info("Calculando distancia entre {} y {}", origen, destino);

        // Construir cliente REST con URL base de Google Maps
        RestClient client = builder.baseUrl("https://maps.googleapis.com/maps/api").build();

        // Construir URL con parámetros
        String url = "/distancematrix/json?origins=" + origen +
                "&destinations=" + destino +
                "&units=metric&key=" + apiKey;

        // Realizar petición GET
        ResponseEntity<String> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class);

        log.debug("Respuesta de Google Maps: {}", response.getBody());

        // Parsear respuesta JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        // Extraer elemento con la distancia y duración
        JsonNode leg = root.path("rows").get(0).path("elements").get(0);

        // Crear DTO con los datos extraídos
        DistanciaDTO dto = new DistanciaDTO();
        dto.setOrigen(origen);
        dto.setDestino(destino);

        // Convertir metros a kilómetros
        dto.setKilometros(leg.path("distance").path("value").asDouble() / 1000);

        // Obtener duración en formato legible
        dto.setDuracionTexto(leg.path("duration").path("text").asText());

        log.info("Distancia calculada: {} km, Duración: {}", dto.getKilometros(), dto.getDuracionTexto());

        return dto;
    }
}
