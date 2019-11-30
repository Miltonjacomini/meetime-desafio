package br.com.meetime.desafio.service;

import br.com.meetime.desafio.model.entity.Lead;
import br.com.meetime.desafio.model.entity.LeadStatus;
import br.com.meetime.desafio.model.entity.User;
import br.com.meetime.desafio.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
public class DistributorServiceTest {

    protected final Faker faker = Faker.instance();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DistributorService distributorService;

    @Test
    @Sql(scripts = "/sql/distributionInsert.sql", config = @SqlConfig(transactionMode = ISOLATED))
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldDistributeLead() {

        Long userLead = distributorService.getUserForLead();

        assertEquals(2, userLead.intValue());
    }

    @Test
    @Sql(scripts = "/sql/distributionInsert2.sql", config = @SqlConfig(transactionMode = ISOLATED))
    @Sql(scripts = "/sql/clearDB.sql", executionPhase = AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
    public void shouldDistributeLeadsCase2() {

        Long userLead = distributorService.getUserForLead();

        assertTrue(userLead.intValue() == 2 || userLead.intValue() == 3);
    }

}
