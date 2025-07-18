package com.challenge.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

public class Solicitud {

    private static final String Url_Base = "https://gutendex.com/books";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private enum TipoConsulta {
        POR_ID("ids"),
        POR_BUSQUEDA("search");

        private final String parametro;

        TipoConsulta(String parametro) {
            this.parametro = parametro;
        }

        private String construirQuery(String valor) {
            if (valor == null || valor.trim().isEmpty()) {
                throw new IllegalArgumentException("El valor no puede ser nulo o vacío");
            }

            String valorProcesado = valor.trim().toLowerCase();
            if (this == POR_BUSQUEDA){
                valorProcesado = URLEncoder.encode(valorProcesado, StandardCharsets.UTF_8);
            }
            return  parametro+"="+valorProcesado;
        }
    }

    private HttpResponse<String> getBooks(TipoConsulta tipo, String valor) throws IOException, InterruptedException {
        String query = tipo.construirQuery(valor);
        String url = Url_Base +"?"+ query;


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "Java-HttpClient/Book-Search")
                .timeout(Duration.ofSeconds(25))
                .GET()
                .build();


        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private  HttpResponse<String> getBooksById(String id) throws IOException, InterruptedException {
        return getBooks(TipoConsulta.POR_ID, id);
    }

    private HttpResponse<String> getBookBySearch(String searchTitle) throws IOException, InterruptedException {
        return  getBooks(TipoConsulta.POR_BUSQUEDA, searchTitle);
    }

    public Optional<HttpResponse<String>> searchBooksTitleSafely(String searchTitle) {
        try {
            HttpResponse<String> response = getBookBySearch(searchTitle);
            return response.statusCode() == 200 ?
                    Optional.of(response) : Optional.empty();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error en la solicitud: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<HttpResponse<String>> searchBooksIdSafely(String id){
        try{
            HttpResponse<String> response = getBooksById(id);
            return response.statusCode() == 200 ?
                    Optional.of(response):Optional.empty();
        } catch (IOException | InterruptedException e){
            System.err.println("Error en la solicitud: "+ e.getMessage());
            return Optional.empty();
        }
    }

    public boolean isSuccessfulResponse(HttpResponse<String> response) {
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }
}
