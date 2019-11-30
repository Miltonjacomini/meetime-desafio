package br.com.meetime.desafio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Deal {

    private String nome;
    private String anotacoes;

    private String pessoaNome;
    private String pessoaEmail;

    private String organizacaoNome;
    private String organizacaoSite;

}
