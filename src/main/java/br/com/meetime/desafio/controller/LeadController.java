package br.com.meetime.desafio.controller;

import br.com.meetime.desafio.model.dto.LeadDTO;
import br.com.meetime.desafio.model.entity.LeadStatus;
import br.com.meetime.desafio.service.LeadService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LeadController {

    private static final Logger logger = LoggerFactory.getLogger(LeadController.class);

    protected static final String PATH_LEADS = "/leads";
    protected static final String PATH_LEADS_WITH_ID = "/leads/{id}";
    protected static final String PATH_FINALIZE_LEAD = "/leads/{id}/finalize";

    private final LeadService leadService;

    @Autowired
    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping(PATH_LEADS)
    @ApiOperation(value = "Cadastrar Lead", response = LeadDTO.class)
    public ResponseEntity<LeadDTO> save(@Valid @RequestBody LeadDTO lead) {

        logger.info("Salvando o Lead : {}", lead);
        LeadDTO saved = leadService.save(lead);

        return ResponseEntity.ok(saved);
    }

    @GetMapping(PATH_LEADS_WITH_ID)
    @ApiOperation(value = "Buscar um Lead específico", response = LeadDTO.class)
    public ResponseEntity<LeadDTO> findById(@Valid @PathVariable long id) {

        logger.info("Buscando um Lead específico : {}", id);
        return ResponseEntity.ok(leadService.findById(id));
    }

    @GetMapping(PATH_LEADS)
    @ApiOperation(value = "Buscar todos os Lead", response = LeadDTO.class)
    public ResponseEntity<List<LeadDTO>> findAll() {

        logger.info("Buscando todos os Leads");
        return ResponseEntity.ok(leadService.findAll());
    }

    @PutMapping(PATH_LEADS_WITH_ID)
    public ResponseEntity<LeadDTO> update(@Valid @PathVariable long id, @RequestBody LeadDTO leadDTO) {

        logger.info("Atualizando Lead by Id: {}, DTO {}", id, leadDTO);
        LeadDTO updated = leadService.update(id, leadDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(PATH_LEADS_WITH_ID)
    public ResponseEntity<LeadDTO> delete(@Valid @PathVariable long id) {

        logger.info("Deletando Lead by Id: {}", id);
        leadService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(PATH_FINALIZE_LEAD)
    public ResponseEntity<LeadDTO> finalize(@Valid @PathVariable long id, @RequestBody LeadStatus status) {

        logger.info("Finalizando Lead by Id: {}", id);
        LeadDTO finalized = leadService.finalize(id, status);

        if (finalized == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(finalized);
    }
}
