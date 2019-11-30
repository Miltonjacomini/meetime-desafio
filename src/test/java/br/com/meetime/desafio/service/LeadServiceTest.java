package br.com.meetime.desafio.service;

import br.com.meetime.desafio.model.dto.LeadDTO;
import br.com.meetime.desafio.model.entity.LeadStatus;
import br.com.meetime.desafio.repository.LeadRepository;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
public class LeadServiceTest {

    protected final Faker faker = Faker.instance();

    @Autowired
    private LeadService leadService;

    @Autowired
    private LeadRepository leadRepository;

    @Test
    @Sql(scripts = "/sql/insert.sql", config = @SqlConfig(transactionMode = ISOLATED))
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldReuseLeadIfExistAndLostOrWon() {

        long idPreInserted = 34L;

        LeadDTO leadFound = leadService.findById(idPreInserted);
        assertNotNull(leadFound);

        LeadDTO update = LeadDTO.builder()
                .nome(faker.lorem().characters(20))
                .email("milton@meetime.com")
                .empresa(faker.company().name())
                .site(faker.internet().domainName()).status(LeadStatus.OPEN).build();

        LeadDTO updated = leadService.save(update);

        assertNotNull(updated);
        assertEquals(Long.valueOf(updated.getId()), Long.valueOf(idPreInserted));
        assertEquals(LeadStatus.OPEN, updated.getStatus());

    }

    @Test
    @Sql(scripts = "/sql/insert.sql", config = @SqlConfig(transactionMode = ISOLATED))
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldNotReuseLeadOpenExistAndLostOrWon() {

        long idPreInserted = 36L;

        LeadDTO leadFound = leadService.findById(idPreInserted);
        assertNotNull(leadFound);

        LeadDTO update = LeadDTO.builder()
                .nome(faker.lorem().characters(20))
                .email("diego@meetime.com")
                .empresa(faker.company().name())
                .site(faker.internet().domainName()).status(LeadStatus.OPEN).build();

        LeadDTO updated = leadService.save(update);

        assertNull(updated);

    }

    @Test(expected = InvalidParameterException.class)
    public void shouldNotSaveInvalidEmail() {

        String emailInvalido = "algumemailnaovalido.com";

        LeadDTO leadWithInvalidEmail = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10))
                .email(emailInvalido).empresa(faker.company().name()).site(faker.internet().domainName())
                .telefones("+551149091999;+554890097888").status(LeadStatus.OPEN)
                .anotacoes(faker.lorem().characters(150))
                .idUsuarioResponsavel(3L).build();

        leadService.save(leadWithInvalidEmail);

    }

}
