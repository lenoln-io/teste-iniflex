package br.com.projedata.iniflex.funcionario;

import br.com.projedata.iniflex.factory.FuncionarioFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public void inicializarDados() {
        List<Funcionario> funcionarios = FuncionarioFactory.criarFuncionarios();
        funcionarios.forEach(funcionarioRepository::inserir);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.listarTodos();
    }

    public void removerFuncionario(String nome) {
        funcionarioRepository.remover(nome);
    }

    public void aplicarAumento(double percentual) {
        if (percentual <= 0) {
            throw new IllegalArgumentException("Percentual de aumento deve ser positivo");
        }

        funcionarioRepository.atualizarSalarios(percentual);
    }

    public Map<String, List<Funcionario>> agruparPorFuncao() {
        return listarFuncionarios().stream().sorted(listarPorOrdemAlfabetica(Funcionario::getFuncao))
                .collect(Collectors.groupingBy(
                        Funcionario::getFuncao,
                        LinkedHashMap::new,
                        Collectors.toList()
                        )
                );
    }

    public List<Funcionario> buscarAniversariantes(int... meses) {
        return funcionarioRepository.buscarPorMesDeAniversario(meses);
    }

    public String buscarFuncionarioMaisVelho() {
        var funcionarioMaisVelho = listarFuncionarios().stream()
                .max(Comparator.comparing(Funcionario::getIdade));

        return funcionarioMaisVelho.map(funcionario -> String.format("Nome: %s, Idade: %s anos",
                funcionario.getNome(),
                funcionario.getIdade()
        )).orElse("Nenhum funcionário encontrado");

    }

   public List<Funcionario> listaFuncionariosPorOrdemAlfabetica() {
        return listarFuncionarios().stream()
                .sorted(listarPorOrdemAlfabetica(Funcionario::getNome))
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

    public Comparator<Funcionario> listarPorOrdemAlfabetica(Function<Funcionario, Comparable> campoParaOrdenar) {
        return Comparator.comparing(
                campoParaOrdenar,
                Comparator.nullsLast(Comparator.naturalOrder()));
    }
}
