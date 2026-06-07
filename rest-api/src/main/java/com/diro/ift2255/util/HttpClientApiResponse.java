package com.diro.ift2255.util;


public class HttpClientApiResponse {
    private final int statusCode;
    private final String statusMessage;
    private final String body;

    public HttpClientApiResponse(int statusCode, String message, String body) {
        this.statusCode = statusCode;
        this.statusMessage = message;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Status Code: " + statusCode + ", Message: " + statusMessage + ", Body: " + body;
    }
}
