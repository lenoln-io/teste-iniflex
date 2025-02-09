package br.com.projedata.iniflex.funcionario;

import br.com.projedata.iniflex.database.DatabaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FuncionarioRepository {

    public void inserir(Funcionario funcionario) {
        String sql = "INSERT INTO funcionarios (nome, dt_nasc, salario, funcao) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, funcionario.getNome());
            preparedStatement.setDate(2, Date.valueOf(funcionario.getDataDeNascimento()));
            preparedStatement.setDouble(3, funcionario.getSalario().doubleValue());
            preparedStatement.setString(4, funcionario.getFuncao());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir funcionário: " + e.getMessage(), e);
        }
    }

    public void remover(String nome) {
        String sql = "DELETE FROM funcionarios WHERE nome = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nome);
            int linhas = preparedStatement.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("Funcionário "+ nome + " não encontrado." );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover funcionário: " + e.getMessage(), e);
        }
    }

    public void atualizarSalarios(double aumentoSalario) {
        String sql = "UPDATE funcionarios SET salario = salario * ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, 1 + (aumentoSalario/100));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar salários: " + e.getMessage(), e);
        }
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios";

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                funcionarios.add(extrairFuncionarioDoResultado(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários: " + e.getMessage(), e);
        }

        return funcionarios;
    }

    public List<Funcionario> buscarPorMesDeAniversario(int... meses) {
        List<Funcionario> funcionarios = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM funcionarios WHERE ");

        for (int i = 0; i < meses.length; i++) {
            if (i > 0) sql.append(" OR ");
            sql.append("strftime('%m', datetime(dt_nasc / 1000, 'unixepoch')) = ?");
        }

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < meses.length; i++) {
                preparedStatement.setString(i + 1, String.format("%02d", meses[i]));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                funcionarios.add(extrairFuncionarioDoResultado(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aniversariantes: " + e.getMessage(), e);
        }

        return funcionarios;
    }

    private Funcionario extrairFuncionarioDoResultado(ResultSet resultSet) throws SQLException {
        return new Funcionario(
                resultSet.getString("nome"),
                resultSet.getDate("dt_nasc").toLocalDate(),
                BigDecimal.valueOf(resultSet.getDouble("salario")).setScale(2, RoundingMode.HALF_UP),
                resultSet.getString("funcao")
        );
    }
}
