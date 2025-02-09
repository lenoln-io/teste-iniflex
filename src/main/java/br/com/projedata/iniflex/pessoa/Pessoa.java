package br.com.projedata.iniflex.pessoa;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class Pessoa {

    protected String nome;
    protected LocalDate dataDeNascimento;

    public Pessoa(String nome, LocalDate dataDeNascimento) {

        validarDados(nome, dataDeNascimento);

        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
    }

    public void validarDados(String nome, LocalDate dataDeNascimento){
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (dataDeNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        }
        if (dataDeNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não ultrapassar a data de hoje.");
        }
    }

    public String getDataFormatada() {
        return dataDeNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}
