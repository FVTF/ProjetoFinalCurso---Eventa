package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Eventa;encrypt=true;trustServerCertificate=true";
    private static final String USER = "LoginTest";
    private static final String PASSWORD = "12345qwert";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Carrega o driver JDBC manualmente
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexão com o SQL Server estabelecida com sucesso!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC do SQL Server não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return connection;
    }
}
