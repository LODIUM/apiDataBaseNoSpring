package org.example.repository;

import org.example.entity.Clients;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientsRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "7117";

    public Optional<Clients> findById(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM clients WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Clients clients = new Clients();
                clients.setId(resultSet.getInt("id"));
                clients.setFirstName(resultSet.getString("first_name"));
                clients.setLastName(resultSet.getString("last_name"));
                clients.setEmail(resultSet.getString("email"));
                clients.setNumberPhone(resultSet.getString("number_phone"));
                return Optional.of(clients);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Clients> findAll(){
        List<Clients> clientsList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM clients");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Clients clients = new Clients();
                clients.setId(resultSet.getInt("id"));
                clients.setFirstName(resultSet.getString("first_name"));
                clients.setLastName(resultSet.getString("last_name"));
                clients.setEmail(resultSet.getString("email"));
                clients.setNumberPhone(resultSet.getString("number_phone"));
                clientsList.add(clients);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsList;
    }

    public void save(Clients clients) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO clients (first_name, last_name, email, number_phone) VALUES (?, ?, ?, ?)");
            statement.setString(1, clients.getFirst_name());
            statement.setString(2, clients.getLast_name());
            statement.setString(3, clients.getEmail());
            statement.setString(4, clients.getNumber_phone());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Clients clients) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE clients SET first_name = ?, last_name = ?, email = ?, number_phone = ? WHERE id = ?");
            statement.setString(1, clients.getFirst_name());
            statement.setString(2, clients.getLast_name());
            statement.setString(3, clients.getEmail());
            statement.setString(4, clients.getNumber_phone());
            statement.setInt(5, clients.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM clients WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
