package org.example.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.entity.Clients;
import org.example.entity.Purchases;
import org.example.repository.ClientsRepository;
import org.example.repository.PurchasesRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class PurchasesHandler implements HttpHandler {
    private PurchasesRepository purchasesRepository = new PurchasesRepository();
    private Gson gson = new Gson();


    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";
        int statusCode = 200;

        try {
            if ("GET".equals(method)) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("id=")) {
                    int id = Integer.parseInt(getQueryParam(query, "id"));
                    Optional<Purchases> purchases = purchasesRepository.findById(id);
                    if (purchases.isPresent()) {
                        response = gson.toJson(purchases.get());
                    } else {
                        statusCode = 404;
                        response = "Purchases not found";
                    }
                }
                else {
                    List<Purchases> purchasesList = purchasesRepository.findAll();
                    response = gson.toJson(purchasesList);
                }
            } else if ("POST".equals(method)) {
                Purchases purchases = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Purchases.class);
                purchasesRepository.save(purchases);
                response = "Purchases saved";
            } else if ("PUT".equals(method)) {
                Purchases purchases = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Purchases.class);
                purchasesRepository.update(purchases);
                response = "Purchases updated";
            } else if ("DELETE".equals(method)) {
                int id = Integer.parseInt(getQueryParam(exchange.getRequestURI().getQuery(), "id"));
                purchasesRepository.delete(id);
                response = "Purchases deleted";
            } else {
                statusCode = 405;
                response = "Method not allowed";
            }
        } catch (Exception e) {
            statusCode = 500;
            response = "Internal Server Error: " + e.getMessage();
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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