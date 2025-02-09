package br.com.projedata.iniflex.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL_DATABASE = "jdbc:sqlite:iniflex.db";

    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite não encontrado", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_DATABASE);
    }

    public static void criarTabelaFuncionarios() {
        String sql = """
            CREATE TABLE IF NOT EXISTS funcionarios (
                id_funcionario INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                dt_nasc DATE NOT NULL,
                salario DECIMAL(10,2) NOT NULL,
                funcao TEXT NOT NULL
            )
        """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao criar tabela", e);
        }
    }

    public static void deletarDados() {
        String sql = "DELETE FROM funcionarios";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar dados", e);
        }
    }
}
