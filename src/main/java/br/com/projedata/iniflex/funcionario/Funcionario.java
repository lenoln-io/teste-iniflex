package br.com.projedata.iniflex.funcionario;

import br.com.projedata.iniflex.pessoa.Pessoa;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;

@Getter
public class Funcionario extends Pessoa {

    private BigDecimal salario;
    private String funcao;

    public Funcionario(String nome, LocalDate dataDeNascimento, BigDecimal salario, String funcao) {
        super(nome, dataDeNascimento);

        validarDadosFuncionario(salario, funcao);

        this.salario = salario;
        this.funcao = funcao;
    }

    public void setSalario(BigDecimal salario) {
        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salário deve ser maior que zero");
        }
        this.salario = salario;
    }

    public static String getSalarioFormatado(BigDecimal salario) {
        BigDecimal salarioArredondado = salario.setScale(2, RoundingMode.HALF_UP);
        DecimalFormat salarioFormatado = new DecimalFormat("#,##0.00");
        return salarioFormatado.format(salarioArredondado);
    }

    public int getIdade() {
        return Period.between(dataDeNascimento, LocalDate.now()).getYears();
    }

    public void validarDadosFuncionario(BigDecimal salario, String funcao){
        if (salario == null || salario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salário deve ser maior que zero");
        }
        if (funcao == null || funcao.trim().isEmpty()) {
            throw new IllegalArgumentException("Função não pode ser vazia");
        }
    }

    @Override
    public String toString() {
        return String.format("Nome: %s, Data de Nascimento: %s, Salário: R$ %s, Função: %s",
                nome, getDataFormatada(), getSalarioFormatado(salario), funcao);
    }

}
