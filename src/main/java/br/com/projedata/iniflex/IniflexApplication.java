package br.com.projedata.iniflex;

import br.com.projedata.iniflex.database.DatabaseManager;
import br.com.projedata.iniflex.funcionario.Funcionario;
import br.com.projedata.iniflex.funcionario.FuncionarioRepository;
import br.com.projedata.iniflex.funcionario.FuncionarioService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IniflexApplication {

    public static void main(String[] args) {
        try {

            //Inicialização das dependências
            FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
            FuncionarioService funcionarioService = new FuncionarioService(funcionarioRepository);

            //Deleta a tabela funcionários caso exista e a cria novamente
            DatabaseManager.resetarTabelaFuncionarios();

            //Inserção de dados dos funcionários usando a Factory
            System.out.println("Inicializando dados...");
            funcionarioService.inicializarDados();

            System.out.println("\n3.1 - Lista de Funcionários");
            funcionarioService.listarFuncionarios().forEach(System.out::println);

            System.out.println("\n3.2 - Removendo funcionário João da lista");
            funcionarioService.removerFuncionario("João");

            System.out.println("\n3.3 - Listagem atualizada de funcionários");
            funcionarioService.listarFuncionarios().forEach(System.out::println);

            System.out.println("\n3.4 - Listagem com aumento salarial atualizada");
            funcionarioService.aplicarAumento(10.0);
            funcionarioService.listarFuncionarios().forEach(System.out::println);

            System.out.println("\n3.5-6 - Funcionários por função");
            funcionarioService.agruparPorFuncao()
                    .forEach((funcao, lista) -> {
                        System.out.println("\nFunção: " + funcao);
                        lista.forEach(System.out::println);
                    });

            System.out.println("\n3.8 - Aniversariantes dos meses 10 e 12");
            funcionarioService.buscarAniversariantes(10, 12).forEach(System.out::println);

            System.out.println("\n3.9 - Funcionário de maior idade");
            System.out.println(funcionarioService.buscarFuncionarioMaisVelho());

            System.out.println("\n3.10 - Lista de funcionários por ordem alfabética");
            funcionarioService.listaFuncionariosPorOrdemAlfabetica().forEach(System.out::println);

            System.out.println("\n3.11 - Soma total dos salários dos funcionários");
            System.out.println("R$ " + Funcionario.getSalarioFormatado(funcionarioService.calcularTotalSalarios()));

            System.out.println("\n3.12 - Salários mínimos dos funcionários");
            funcionarioService.calcularSalariosMinimos()
                    .forEach((nome, salariosMinimos) ->
                            System.out.println(nome + ": " + salariosMinimos + " salários mínimos")
                    );

        } catch (Exception e) {
            System.err.println("Erro na execução do programa: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
