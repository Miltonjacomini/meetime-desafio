package br.com.meetime.desafio.controller;

import br.com.meetime.desafio.model.dto.LeadDTO;
import br.com.meetime.desafio.model.entity.LeadStatus;
import br.com.meetime.desafio.repository.LeadRepository;
import br.com.meetime.desafio.service.LeadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LeadControllerTest {

    protected final Faker faker = Faker.instance();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeadService leadService;

    @MockBean
    private LeadRepository leadRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldSaveLead() throws Exception {

        long idUsuarioResponsavel = faker.random().nextLong(5);

        LeadDTO leadCocacola = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10))
                .email(faker.internet().emailAddress()).empresa(faker.company().name()).site(faker.internet().domainName())
                .telefones("+551149091999;+554890097888").status(LeadStatus.OPEN)
                .anotacoes(faker.lorem().characters(150))
                .idUsuarioResponsavel(idUsuarioResponsavel).build();

        when(leadService.save(any(LeadDTO.class))).thenReturn(leadCocacola);

        mockMvc.perform(post(LeadController.PATH_LEADS)
                .content(objectMapper.writeValueAsString(leadCocacola))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(leadCocacola.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(leadCocacola.getNome())))
                .andExpect(jsonPath("$.email", is(leadCocacola.getEmail())));

        verify(leadService, times(1)).save(any(LeadDTO.class));
    }

    @Test
    public void shouldUpdateLead() throws Exception {

        long idUsuarioResponsavel = faker.random().nextLong(5);

        LeadDTO leadCocacola = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10))
                .email(faker.internet().emailAddress()).empresa(faker.company().name()).site(faker.internet().domainName())
                .telefones("+551149091999;+554890097888").status(LeadStatus.OPEN)
                .anotacoes(faker.lorem().characters(150))
                .idUsuarioResponsavel(idUsuarioResponsavel).build();

        when(leadService.findById(eq(leadCocacola.getId()))).thenReturn(leadCocacola);

        LeadDTO leadUpdated = leadCocacola;
        leadUpdated.setNome(faker.lorem().characters(20));
        leadUpdated.setEmail(faker.internet().emailAddress());
        leadUpdated.setEmpresa(faker.company().name());

        when(leadService.update(eq(leadCocacola.getId()), any(LeadDTO.class))).thenReturn(leadUpdated);

        mockMvc.perform(put(LeadController.PATH_LEADS_WITH_ID.replace("{id}", leadCocacola.getId().toString()))
                .content(objectMapper.writeValueAsString(leadUpdated))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(leadUpdated.getNome())))
                .andExpect(jsonPath("$.email", is(leadUpdated.getEmail())))
                .andExpect(jsonPath("$.empresa", is(leadUpdated.getEmpresa())));

        verify(leadService, times(1)).update(anyLong(), any(LeadDTO.class));

    }

    @Test
    public void shouldFindAllLeads() throws Exception {

        LeadDTO leadCocacola = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10)).build();
        LeadDTO leadCarrefour = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10)).build();
        LeadDTO leadWalmart = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10)).build();
        List<LeadDTO> allLeads = Arrays.asList(leadCocacola, leadCarrefour, leadWalmart);
        when(leadService.findAll()).thenReturn(allLeads);

        mockMvc.perform(get(LeadController.PATH_LEADS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(allLeads.size())));

        verify(leadService, times(1)).findAll();
    }

    @Test
    public void shouldViewOneSpecificLead() throws Exception {

        long idUsuarioResponsavel = faker.random().nextLong(5);

        LeadDTO leadCocacola = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10))
                .email(faker.internet().emailAddress()).empresa(faker.company().name()).site(faker.internet().domainName())
                .telefones("+551149091999;+554890097888").status(LeadStatus.OPEN)
                .anotacoes(faker.lorem().characters(150))
                .idUsuarioResponsavel(idUsuarioResponsavel).build();
        when(leadService.findById(eq(leadCocacola.getId()))).thenReturn(leadCocacola);

        mockMvc.perform(get(LeadController.PATH_LEADS_WITH_ID.replace("{id}", "/"+leadCocacola.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(leadCocacola.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(leadCocacola.getNome())))
                .andExpect(jsonPath("$.email", is(leadCocacola.getEmail())))
                .andExpect(jsonPath("$.empresa", is(leadCocacola.getEmpresa())));

        verify(leadService, times(1)).findById(leadCocacola.getId());

    }

    @Test
    public void shouldDeleteLeadById() throws Exception {

        doNothing().when(leadService).delete(1L);

        mockMvc.perform(delete(LeadController.PATH_LEADS_WITH_ID.replace("{id}", "/1")))
                .andExpect(status().isNoContent());

        verify(leadService, times(1)).delete(1L);

    }

    @Test
    public void shouldFinalizeLead() throws Exception {

        long idUsuarioResponsavel = faker.random().nextLong(5);

        LeadDTO leadCocacola = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10))
                .email(faker.internet().emailAddress()).empresa(faker.company().name()).site(faker.internet().domainName())
                .telefones("+551149091999;+554890097888").status(LeadStatus.OPEN)
                .anotacoes(faker.lorem().characters(150))
                .idUsuarioResponsavel(idUsuarioResponsavel).build();

        when(leadService.findById(leadCocacola.getId())).thenReturn(leadCocacola);

        mockMvc.perform(put(LeadController.PATH_FINALIZE_LEAD.replace("{id}", leadCocacola.getId().toString()))
                .content(objectMapper.writeValueAsString(LeadStatus.WON.name()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(leadCocacola.getNome())))
                .andExpect(jsonPath("$.email", is(leadCocacola.getEmail())))
                .andExpect(jsonPath("$.empresa", is(leadCocacola.getEmpresa())))
                .andExpect(jsonPath("$.status", is(LeadStatus.WON)));

        verify(leadService, times(1)).finalize(anyLong(), any(LeadStatus.class));

    }

    @Test
    public void shouldNotFinalizeLeadWON() throws Exception {

        long idUsuarioResponsavel = faker.random().nextLong(5);

        LeadDTO leadCocacola = LeadDTO.builder().id(faker.random().nextLong(5)).nome(faker.lorem().characters(10))
                .email(faker.internet().emailAddress()).empresa(faker.company().name()).site(faker.internet().domainName())
                .telefones("+551149091999;+554890097888").status(LeadStatus.WON)
                .anotacoes(faker.lorem().characters(150))
                .idUsuarioResponsavel(idUsuarioResponsavel).build();

        when(leadService.findById(leadCocacola.getId())).thenReturn(leadCocacola);

        mockMvc.perform(put(LeadController.PATH_FINALIZE_LEAD.replace("{id}", leadCocacola.getId().toString()))
                .content(objectMapper.writeValueAsString(LeadStatus.WON.name()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(leadService, times(1)).finalize(anyLong(), any(LeadStatus.class));

    }
}
