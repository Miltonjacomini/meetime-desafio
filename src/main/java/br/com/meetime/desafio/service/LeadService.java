package br.com.meetime.desafio.service;

import br.com.meetime.desafio.controller.ResourceNotFoundException;
import br.com.meetime.desafio.model.dto.LeadDTO;
import br.com.meetime.desafio.model.entity.Lead;
import br.com.meetime.desafio.model.entity.LeadStatus;
import br.com.meetime.desafio.repository.LeadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class LeadService {

    private static final Logger logger = LoggerFactory.getLogger(LeadService.class);
    protected static final String LEAD_NOT_FOUND = "Lead não encontrado";

    private final LeadRepository leadRepository;
    private PipedriveService pipedriveService;

    @Autowired
    public LeadService(LeadRepository leadRepository, PipedriveService pipedriveService) {
        this.pipedriveService = pipedriveService;
        this.leadRepository = leadRepository;
    }

    @Transactional
    public LeadDTO save(LeadDTO leadDTO) {

        leadDTO.isValidEmail();

        Optional<Lead> leadFound = leadRepository.findByEmailAndReusable(leadDTO.getEmail());
        if (leadFound.isPresent()) {
            return reuseIfPossible(leadDTO, leadFound);
        }

        Lead entity = leadDTO.toEntity();
        return leadRepository.save(entity).toDTO();
    }

    private LeadDTO reuseIfPossible(LeadDTO leadDTO, Optional<Lead> leadFound) {
        Lead lead = leadFound.get();
        if (lead.isValidForReuse()) {
            logger.info("Reutilizando Lead id: {}", lead.getId());
            lead.updateFromView(leadDTO);
            return leadRepository.save(lead).toDTO();
        } else {
            logger.info("Lead já existente, sem possível reutilização. Id: {}", lead.getId());
            return null;
        }
    }

    @Transactional(readOnly = true)
    public LeadDTO findById(long id) {
        Lead lead = leadRepository.findById(id).orElse(new Lead());
        return lead.toDTO();
    }

    @Transactional(readOnly = true)
    public List<LeadDTO> findAll() {
        return leadRepository.findAll().stream().map(Lead::toDTO).collect(toList());
    }

    @Transactional
    public void delete(long id) {
        leadRepository.deleteById(id);
    }

    @Transactional
    public LeadDTO update(long id, LeadDTO leadDTO) {

        leadDTO.isValidEmail();

        Lead leadFound = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LEAD_NOT_FOUND));

        leadFound.updateFromView(leadDTO);

        return leadRepository.save(leadFound).toDTO();
    }

    @Transactional
    public LeadDTO finalize(long id, LeadStatus status) {

        if (LeadStatus.OPEN.equals(status))
            return null;

        Lead lead = leadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(LEAD_NOT_FOUND));
        lead.setStatus(status);

        if (LeadStatus.WON.equals(status)) {
            pipedriveService.send(lead);
        }
        
        return leadRepository.save(lead).toDTO();
    }
}
