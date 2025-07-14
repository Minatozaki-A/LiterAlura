package com.challenge.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CallingApi {

    // Cliente HttpClient reutilizable para todas las solicitudes.
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String URL_BASE  = "https://gutendex.com/books";

    private HttpResponse<String> Respuesta(String query) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE +"?"+ query))
                .header("Accept", "application/json")
                .GET()
                .build();

        return CLIENT.send(request , HttpResponse.BodyHandlers.ofString());
    }



}
