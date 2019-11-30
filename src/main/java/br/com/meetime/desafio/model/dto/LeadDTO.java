package br.com.meetime.desafio.model.dto;

import br.com.meetime.desafio.model.entity.Lead;
import br.com.meetime.desafio.model.entity.LeadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LeadDTO implements Serializable {

    private Long id;
    private String nome;
    private String email;
    private String empresa;
    private String site;
    private String telefones;
    private LeadStatus status;
    private String anotacoes;

    private Long idUsuarioResponsavel;

    public Lead toEntity() {
        return Lead.builder().nome(this.nome)
                .email(this.email)
                .empresa(this.empresa)
                .site(this.site)
                .telefones(this.telefones)
                .status(this.status)
                .anotacoes(this.anotacoes)
                .idUsuarioResponsavel(this.idUsuarioResponsavel).build();
    }

    public void isValidEmail() {

        String emailTest = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailTest);

        if (!pattern.matcher(this.email).matches()) {
            throw new InvalidParameterException("Email inválido");
        }
    }
}
