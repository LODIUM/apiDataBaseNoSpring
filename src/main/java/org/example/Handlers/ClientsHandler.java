package org.example.Handlers;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.entity.Clients;
import org.example.repository.ClientsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class ClientsHandler implements HttpHandler {
    private ClientsRepository clientsRepository = new ClientsRepository();
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
                    Optional<Clients> client = clientsRepository.findById(id);
                    if (client.isPresent()) {
                        response = gson.toJson(client.get());
                    } else {
                        statusCode = 404;
                        response = "Client not found";
                    }
                }
                else {
                    List<Clients> clientsList = clientsRepository.findAll();
                    response = gson.toJson(clientsList);
                }
            } else if ("POST".equals(method)) {
                Clients client = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Clients.class);
                clientsRepository.save(client);
                response = "Client saved";
            } else if ("PUT".equals(method)) {
                Clients client = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Clients.class);
                clientsRepository.update(client);
                response = "Client updated";
            } else if ("DELETE".equals(method)) {
                int id = Integer.parseInt(getQueryParam(exchange.getRequestURI().getQuery(), "id"));
                clientsRepository.delete(id);
                response = "Client deleted";
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
