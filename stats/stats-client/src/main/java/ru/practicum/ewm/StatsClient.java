package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
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

    public ResponseEntity<Object> hitUri(String app, String uri, String ip, LocalDateTime timestamp) {

        String path = statsServerUri + API_HIT_PREFIX;

        HitObject object = HitObject.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        return prepareAndSendRequest(path, HttpMethod.POST, object, null);

    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uris, Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        ParamObject params = ParamObject.builder()
                .start(start.format(formatter))
                .end(end.format(formatter))
                .build();

        if (uris != null) {
            params.setUris(uris);
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

                String urlWithParams = UriComponentsBuilder.fromHttpUrl(path)
                        .queryParam("start", URLEncoder.encode(params.getStart(), StandardCharsets.UTF_8))
                        .queryParam("end", URLEncoder.encode(params.getEnd(), StandardCharsets.UTF_8))
                        .queryParam("unique", params.getUnique())
                        .queryParam("uris", params.getUris())
                        .build(false)
                        .toUriString();
                statsServerResponse = rest.exchange(urlWithParams, method, requestEntity, Object.class);
            }

        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
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
