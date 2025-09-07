package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;


public class DecryptionService {
    public String url;
    public String secret;

    public DecryptionService(String url, String secret) {
        this.url = url;
        this.secret = secret;
    }

    public String decrypt(String encryptedText) {

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(this.url))
                    .GET()
                    .header("secret-key", this.secret)
                    .header("encrypted-text", encryptedText)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            response.body();
            ObjectMapper mapper = new ObjectMapper();
            DecryptionServiceResponse resp = mapper.readValue(response.body(), DecryptionServiceResponse.class);
            if (resp.error_code.equals("00000")) {
                throw new RuntimeException("API failed with error: " + resp.error_code);
            }
            return resp.decrypted_text;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
