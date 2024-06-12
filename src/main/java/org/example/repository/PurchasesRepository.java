package org.example.repository;

import org.example.entity.Purchases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchasesRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "7117";

    public Optional<Purchases> findById(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM purchases WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Purchases purchases = new Purchases();
                purchases.setId(resultSet.getInt("id"));
                purchases.setClients_id(resultSet.getInt("clients_id"));
                purchases.setProducts_id(resultSet.getInt("product_id"));
                purchases.setCost(resultSet.getDouble("cost"));
                return Optional.of(purchases);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Purchases> findAll() {
        List<Purchases> purchasesList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM purchases");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Purchases purchases = new Purchases();
                purchases.setId(resultSet.getInt("id"));
                purchases.setClients_id(resultSet.getInt("clients_id"));
                purchases.setProducts_id(resultSet.getInt("product_id"));
                purchases.setCost(resultSet.getDouble("cost"));
                purchasesList.add(purchases);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchasesList;
    }

    public void save(Purchases purchases) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO purchases (clients_id, product_id, cost) VALUES (?, ?, ?)");
            statement.setInt(1, purchases.getClients_id());
            statement.setInt(2, purchases.getProducts_id());
            statement.setDouble(3, purchases.getCost());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Purchases purchases) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE purchases SET clients_id = ?, product_id = ?, cost = ? WHERE id = ?");
            statement.setInt(1, purchases.getClients_id());
            statement.setInt(2, purchases.getProducts_id());
            statement.setDouble(3, purchases.getCost());
            statement.setInt(4, purchases.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM purchases WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
