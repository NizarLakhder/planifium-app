package com.diro.ift2255.util;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientApi {

    private final HttpClient client;
    private ObjectMapper mapper;

    public HttpClientApi() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.mapper = new ObjectMapper();
    }

    /** Perform a GET request */
    public HttpClientApiResponse get(URI uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new HttpClientApiResponse(
                    response.statusCode(),
                    HttpStatus.reasonPhrase(response.statusCode()),
                    response.body());

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // best practice if interrupted
            return new HttpClientApiResponse(500, "Internal Server Error", e.getMessage());
        }
    }

    /** GET and map JSON body to a given class */
    public <T> T get(URI uri, Class<T> clazz) {
        HttpClientApiResponse raw = get(uri);
        if (raw.getStatusCode() >= 200 && raw.getStatusCode() < 300) {
            try {
                return mapper.readValue(raw.getBody(), clazz);
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse JSON: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("Request failed: " + raw.getStatusCode() + " - " + raw.getStatusMessage());
        }
    }

    /** GET and map JSON body to collection or complex type */
    public <T> T get(URI uri, TypeReference<T> typeRef) {
        HttpClientApiResponse raw = get(uri);
        if (raw.getStatusCode() >= 200 && raw.getStatusCode() < 300) {
            try {
                return mapper.readValue(raw.getBody(), typeRef);
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse JSON: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("Request failed: " + raw.getStatusCode() + " - " + raw.getStatusMessage());
        }
    }

    /** Perform a POST request with JSON body */
    public HttpClientApiResponse post(URI uri, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new HttpClientApiResponse(
                    response.statusCode(),
                    HttpStatus.reasonPhrase(response.statusCode()),
                    response.body());

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return new HttpClientApiResponse(500, "Internal Server Error", e.getMessage());
        }
    }

    /** Helper to build URIs with query parameters */
    public static URI buildUri(String baseUrl, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(baseUrl);
        if (params != null && !params.isEmpty()) {
            sb.append("?");
            params.forEach((key, value) -> {
                sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                        .append("&");
            });
            sb.deleteCharAt(sb.length() - 1); // remove trailing &
        }
        return URI.create(sb.toString());
    }
}
