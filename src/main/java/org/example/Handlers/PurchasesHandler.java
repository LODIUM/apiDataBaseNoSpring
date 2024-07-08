package org.example.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.entity.Purchases;
import org.example.repository.PurchasesRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class  PurchasesHandler implements HttpHandler {
    private PurchasesRepository purchasesRepository = new PurchasesRepository();
    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    response = handleGetRequest(exchange);
                    break;
                case "POST":
                    response = handlePostRequest(exchange);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange);
                    break;
                default:
                    statusCode = 405; // Method Not Allowed
                    response = "Method not allowed";
                    break;
            }
        } catch (Exception e) {
            statusCode = 500; // Internal Server Error
            response = "Internal Server Error: " + e.getMessage();
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String handleGetRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.contains("id=")) {
            return getPurchasesById(query);
        } else {
            return getAllPurchases();
        }
    }

    private String getPurchasesById(String query) {
        int id = Integer.parseInt(getQueryParam(query, "id"));
        Optional<Purchases> purchases = purchasesRepository.findById(id);
        if (purchases.isPresent()) {
            return gson.toJson(purchases.get());
        } else {
            return "Purchases not found";
        }
    }

    private String getAllPurchases() {
        List<Purchases> purchasesList = purchasesRepository.findAll();
        return gson.toJson(purchasesList);
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        Purchases purchases = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Purchases.class);
        purchasesRepository.save(purchases);
        return "Purchases saved";
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException {
        Purchases purchases = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Purchases.class);
        purchasesRepository.update(purchases);
        return "Purchases updated";
    }

    private String handleDeleteRequest(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(getQueryParam(exchange.getRequestURI().getQuery(), "id"));
        purchasesRepository.delete(id);
        return "Purchases deleted";
    }

    private String getQueryParam(String query, String param) {
        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue[0].equals(param)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
