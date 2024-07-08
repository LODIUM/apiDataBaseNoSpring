package org.example.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.entity.Clients;
import org.example.entity.Products;
import org.example.repository.ProductsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class ProductsHandler implements HttpHandler {
    private ProductsRepository productsRepository = new ProductsRepository();
    private Gson gson = new Gson();


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
        String response;
        int statusCode = 200;

        if (query != null && query.contains("id=")) {
            int id = Integer.parseInt(getQueryParam(query, "id"));
            Optional<Products> products = productsRepository.findById(id);
            if (products.isPresent()) {
                response = gson.toJson(products.get());
            } else {
                statusCode = 404; // Not Found
                response = "Product not found";
            }
        } else {
            List<Products> productsList = productsRepository.findAll();
            response = gson.toJson(productsList);
        }

        return response;
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        Products products = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Products.class);
        productsRepository.save(products);
        return "Product saved";
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException {
        Products products = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Products.class);
        productsRepository.update(products);
        return "Product updated";
    }

    private String handleDeleteRequest(HttpExchange exchange) {
        int id = Integer.parseInt(getQueryParam(exchange.getRequestURI().getQuery(), "id"));
        productsRepository.delete(id);
        return "Product deleted";
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
