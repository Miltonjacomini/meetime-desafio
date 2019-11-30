package br.com.meetime.desafio.model.entity;

import br.com.meetime.desafio.model.Deal;
import br.com.meetime.desafio.model.dto.LeadDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "lead")
public class Lead implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true)
    private String email;
    private String empresa;
    private String site;
    private String telefones;
    @Enumerated(EnumType.STRING)
    private LeadStatus status;
    private String anotacoes;

    @Column(name = "id_usuario_responsavel")
    private Long idUsuarioResponsavel;

    public LeadDTO toDTO() {
        return LeadDTO.builder().id(this.getId())
                .nome(this.nome)
                .email(this.email)
                .empresa(this.empresa)
                .site(this.site)
                .telefones(this.telefones)
                .status(this.status)
                .anotacoes(this.anotacoes)
                .idUsuarioResponsavel(this.idUsuarioResponsavel).build();
    }

    public void updateFromView(LeadDTO leadDTO) {

        if (Objects.nonNull(leadDTO.getNome()) && StringUtils.hasText(leadDTO.getNome()))
            this.nome = leadDTO.getNome();

        if (Objects.nonNull(leadDTO.getEmail()) && StringUtils.hasText(leadDTO.getEmail()))
            this.email = leadDTO.getEmail();

        if (Objects.nonNull(leadDTO.getEmpresa()) && StringUtils.hasText(leadDTO.getEmpresa()))
            this.empresa = leadDTO.getEmpresa();

        if (Objects.nonNull(leadDTO.getSite()) && StringUtils.hasText(leadDTO.getSite()))
            this.site = leadDTO.getSite();

        if (Objects.nonNull(leadDTO.getTelefones()) && StringUtils.hasText(leadDTO.getTelefones()))
            this.telefones = leadDTO.getTelefones();

        if (Objects.nonNull(leadDTO.getStatus()))
            this.status = leadDTO.getStatus();

        if (Objects.nonNull(leadDTO.getAnotacoes()) && StringUtils.hasText(leadDTO.getAnotacoes()))
            this.anotacoes = leadDTO.getAnotacoes();

        if (Objects.nonNull(leadDTO.getIdUsuarioResponsavel()))
            this.idUsuarioResponsavel = leadDTO.getIdUsuarioResponsavel();
    }

    public boolean isValidForReuse() {
        return (this.getStatus().equals(LeadStatus.LOST) || this.getStatus().equals(LeadStatus.WON));
    }

    public Deal createDeal() {
        return Deal.builder().nome(this.empresa)
                .anotacoes(this.anotacoes)
                .pessoaNome(this.nome)
                .pessoaEmail(this.email)
                .organizacaoNome(this.empresa)
                .organizacaoSite(this.site)
                .build();
    }
}
