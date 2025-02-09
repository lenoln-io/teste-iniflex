package br.com.projedata.iniflex.funcionario;

import br.com.projedata.iniflex.factory.FuncionarioFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

public class FuncionarioService {
    private final FuncionarioDAO funcionarioDAO;
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public FuncionarioService(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }

    public void inicializarDados() {
        List<Funcionario> funcionarios = FuncionarioFactory.criarFuncionarios();
        funcionarios.forEach(funcionarioDAO::inserir);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarioDAO.listarTodos();
    }

    public void removerFuncionario(String nome) {
        funcionarioDAO.remover(nome);
    }

    public void aplicarAumento(double percentual) {
        if (percentual <= 0) {
            throw new IllegalArgumentException("Percentual de aumento deve ser positivo");
        }

        funcionarioDAO.atualizarSalarios(percentual);
    }

    public Map<String, List<Funcionario>> agruparPorFuncao() {
        return listarFuncionarios().stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    public List<Funcionario> buscarAniversariantes(int... meses) {
        return funcionarioDAO.buscarPorMesDeAniversario(meses);
    }

    public String buscarFuncionarioMaisVelho() {
        var funcionarioMaisVelho = listarFuncionarios().stream()
                .max(Comparator.comparing(Funcionario::getIdade));
        return String.format("Nome: %s, Idade: %s",
                funcionarioMaisVelho.get().getNome(),
                funcionarioMaisVelho.get().getIdade()
        );
    }

    public List<Funcionario> listarPorOrdemAlfabetica() {
        return listarFuncionarios().stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .collect(Collectors.toList());
    }

    public BigDecimal calcularTotalSalarios() {
        return listarFuncionarios().stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> calcularSalariosMinimos() {
        return listarFuncionarios().stream()
                .collect(Collectors.toMap(
                        Funcionario::getNome,
                        funcionario -> funcionario.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP)
                ));
    }
}
