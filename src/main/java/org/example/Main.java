package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.Handlers.ClientsHandler;
import org.example.Handlers.ProductsHandler;
import org.example.Handlers.PurchasesHandler;
import org.example.entity.Clients;

import java.io.IOException;
import java.net.InetSocketAddress;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/clients", new ClientsHandler());
        server.createContext("/api/products", new ProductsHandler());
        server.createContext("/api/purchases", new PurchasesHandler()); //put and post not working
        server.setExecutor(null);
        server.start();
        System.out.println("Server started, server port is 8080");
    }
}