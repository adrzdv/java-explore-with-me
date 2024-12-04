package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsdto.HitObject;
import ru.practicum.statsdto.ParamObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Base client for StatsServer
 */

public class StatsClient {

    private static final String API_HIT_PREFIX = "/hit";
    private static final String API_GET_STATS_PREFIX = "/stats";

    private final RestTemplate rest;
    private final String statsServerUri;

    public StatsClient(@Value("${stats-server.uri}") String statsServerUri, RestTemplate rest) {
        this.rest = rest;
        this.statsServerUri = statsServerUri;
    }

    public <T> ResponseEntity<Object> hitUri(String app, String uri, String ip, LocalDateTime timestamp) {

        String path = statsServerUri + API_HIT_PREFIX;

        HitObject object = HitObject.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        return prepareAndSendRequest(path, HttpMethod.POST, object, null);

    }

    public <T> ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                               List<String> uries, Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStart = start.format(formatter);
        String formattedEnd = end.format(formatter);

        ParamObject params = ParamObject.builder()
                .start(URLEncoder.encode(formattedStart, StandardCharsets.UTF_8))
                .end(URLEncoder.encode(formattedEnd, StandardCharsets.UTF_8))
                .build();

        if (uries != null) {
            params.setUries(uries);
        }

        if (unique != null) {
            params.setUnique(unique);
        }


        String path = statsServerUri + API_GET_STATS_PREFIX;
        return prepareAndSendRequest(path, HttpMethod.GET, null, params);
    }

    public <T> ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                               List<String> uries) {

        return getStats(start, end, uries, false);
    }

    public <T> ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                               boolean unique) {

        return getStats(start, end, null, unique);
    }

    public <T> ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end) {

        return getStats(start, end, null, null);
    }


    private <T> ResponseEntity<Object> prepareAndSendRequest(String path, HttpMethod method,
                                                             @Nullable T body, @Nullable ParamObject params) {

        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statsServerResponse;

        try {
            if (params == null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, params);
            }

        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

}
