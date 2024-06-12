package org.example.repository;

import org.example.entity.Clients;
import org.example.entity.Products;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "7117";

    public Optional<Products> findById(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Products products = new Products();
                products.setId(resultSet.getInt("id"));
                products.setProduct_name(resultSet.getString("product_name"));
                products.setCountry_of_manufacture(resultSet.getString("country_of_manufacture"));
                products.setPrice(resultSet.getDouble("price"));
                return Optional.of(products);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Products> findAll() {
        List<Products> productsList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM products");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Products products = new Products();
                products.setId(resultSet.getInt("id"));
                products.setProduct_name(resultSet.getString("product_name"));
                products.setCountry_of_manufacture(resultSet.getString("country_of_manufacture"));
                products.setPrice(resultSet.getDouble("price"));
                productsList.add(products);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsList;
    }

    public void save(Products products) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO products (product_name, country_of_manufacture, price) VALUES (?, ?, ?)");
            statement.setString(1, products.getProduct_name());
            statement.setString(2, products.getCountry_of_manufacture());
            statement.setDouble(3, products.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(Products products) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE products SET product_name = ?, country_of_manufacture = ?, price = ? WHERE id = ?");
            statement.setString(1, products.getProduct_name());
            statement.setString(2,products.getCountry_of_manufacture());
            statement.setDouble(3,products.getPrice());
            statement.setInt(4,products.getId());
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM products WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}