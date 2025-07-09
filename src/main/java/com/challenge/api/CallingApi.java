package com.challenge.api;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.IOException;

public class CallingApi {


    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();


    private HttpResponse<String> solicitarInfo () throws IOException, InterruptedException{

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://gutendex.com/books/1513"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void main(String[] args) {
        try {
            CallingApi callingApi = new CallingApi();
            HttpResponse<String> response = callingApi.solicitarInfo();
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
