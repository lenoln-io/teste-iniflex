package br.com.projedata.iniflex.factory;

import br.com.projedata.iniflex.funcionario.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioFactory {
    public static List<Funcionario> criarFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(criarFuncionario("Maria", "2000-10-18", "2009.44", "Operador"));
        funcionarios.add(criarFuncionario("João", "1990-05-12", "2284.38", "Operador"));
        funcionarios.add(criarFuncionario("Caio", "1961-05-02", "9836.14", "Coordenador"));
        funcionarios.add(criarFuncionario("Miguel", "1988-10-14", "19119.88", "Diretor"));
        funcionarios.add(criarFuncionario("Alice", "1995-01-05", "2234.68", "Recepcionista"));
        funcionarios.add(criarFuncionario("Heitor", "1999-11-19", "1582.72", "Operador"));
        funcionarios.add(criarFuncionario("Arthur", "1993-03-31", "4071.84", "Contador"));
        funcionarios.add(criarFuncionario("Laura", "1994-07-08", "3017.45", "Gerente"));
        funcionarios.add(criarFuncionario("Heloísa", "2003-05-24", "1606.85", "Eletricista"));
        funcionarios.add(criarFuncionario("Helena", "1996-09-02", "2799.93", "Gerente"));
        funcionarios.add(criarFuncionario("Lenoln", "1997-06-03", "3000.00", "Desenvolvedor"));

        return funcionarios;
    }

    private static Funcionario criarFuncionario(String nome, String dataDeNascimento, String salario, String funcao) {
        return new Funcionario(
                nome,
                LocalDate.parse(dataDeNascimento),
                new BigDecimal(salario),
                funcao
        );
    }
}
