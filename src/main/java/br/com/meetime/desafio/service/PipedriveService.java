package br.com.meetime.desafio.service;

import br.com.meetime.desafio.model.entity.Lead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PipedriveService {

    private static final Logger logger = LoggerFactory.getLogger(PipedriveService.class);

    private static final String URL_PIPE_DEAL = "https://teste47.pipedrive.com/v1/deals?api_token=x";
    private static final String URL_PIPE_PERSONS = "https://teste47.pipedrive.com/v1/persons?api_token=x";
    private static final String URL_PIPE_ORGANIZATIONS = "https://teste47.pipedrive.com/v1/organizations?api_token=x";

    private RestTemplate restTemplate;

    @Autowired
    public PipedriveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void send(Lead lead) {

        Long org_id = saveOrganization(lead.getEmpresa(), lead.getSite());

        ResponseEntity<String> responsePerson = savePessoa(lead.getNome(), lead.getEmail());
        System.out.println(responsePerson.getStatusCode());

        ResponseEntity<String> responseDeal = saveDeal(lead.getEmpresa(), lead.getAnotacoes(), org_id);
        System.out.println(responseDeal.getStatusCode());
    }

    private Long saveOrganization(String nome, String site) {

        logger.info("Salvando organization");
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap();

        body.add("name", nome);
        body.add("491065709208004c5ac7d3a596b2b16376edfd35", site);

        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(URL_PIPE_ORGANIZATIONS, HttpMethod.POST, httpEntity, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return Long.valueOf(0);
        }

        LinkedHashMap<String, String> data = (LinkedHashMap<String, String>) response.getBody().get("data");
        return Long.valueOf(data.get("company_id"));
    }

    private ResponseEntity<String> saveDeal(String nome, String anotacoes, Long org_id) {

        logger.info("Salvando Negócio");
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap();

        body.add("org_id", String.valueOf(org_id));
        body.add("title", nome);
        body.add("d47241067d4a4f95c58ef8a4b0a34a2165c23dbb", anotacoes);

        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL_PIPE_DEAL, HttpMethod.POST, httpEntity, String.class);

        return response;
    }

    private ResponseEntity<String> savePessoa(String nome, String email) {

        logger.info("Salvando Pessoa");
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap();

        body.add("name", nome);
        body.add("email", email);

        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL_PIPE_PERSONS, HttpMethod.POST, httpEntity, String.class);

        return response;
    }

}
