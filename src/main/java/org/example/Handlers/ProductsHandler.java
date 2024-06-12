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

        try{
            if  ("GET".equals(method)) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("id=")){
                    int id = Integer.parseInt(getQueryParam(query, "id"));
                    Optional<Products> products = productsRepository.findById(id);
                    if (products.isPresent()) {
                    response = gson.toJson(products.get());
                    } else {
                        statusCode = 404;
                        response = "Product not found";
                }
            }
            else {
                List<Products> productsList = productsRepository.findAll();
                response =gson.toJson(productsList);
            }
        } else if ("POST".equals(method)) {
                  Products products =gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Products.class);
                  productsRepository.save(products);
                  response = "Product saved";
              } else if ("PUT".equals(method)) {
                  Products products =gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Products.class);
                  productsRepository.update(products);
                  response = "Product update";
              } else if ("DELETE".equals(method)){
                  int id =Integer.parseInt(getQueryParam(exchange.getRequestURI().getQuery(), "id"));
                  productsRepository.delete(id);
                  response = "Client deleted";
              } else{
                  statusCode = 405;
                  response = "Method not allowed";
              }
        }catch (Exception e) {
            statusCode = 500;
            response = "Internal Server Error: " + e.getMessage();
        }
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private String getQueryParam(String query, String param) {
        for (String pair : query. split("&")){
            String[] keyValue = pair.split("=");
            if (keyValue[0].equals(param)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
