package br.com.meetime.desafio.service;

import br.com.meetime.desafio.model.entity.User;
import br.com.meetime.desafio.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class DistributorService {

    private static final Logger logger = LoggerFactory.getLogger(LeadService.class);

    private final LeadService leadService;
    private final UserRepository userRepository;

    @Autowired
    public DistributorService(LeadService leadService, UserRepository userRepository) {
        this.leadService = leadService;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Long getUserForLead() {

        List<User> users = userRepository.findAll();

        Map<Long, Long> leadsByUsers = leadService.findAll()
                                                  .stream()
                                                  .collect(groupingBy(lead -> lead.getIdUsuarioResponsavel(), counting()));

        List<User> notHaveLeads = new ArrayList<>();
        users.stream().forEach(user -> {
            if (!leadsByUsers.keySet().contains(user.getId())) {
                notHaveLeads.add(user);
            }
        });

        if (notHaveLeads.isEmpty()) {
            Map.Entry<Long, Long> entry = leadsByUsers.entrySet().stream().sorted(Map.Entry.comparingByValue()).iterator().next();
            return entry.getKey();
        } else {
            Collections.shuffle(notHaveLeads);
            return notHaveLeads.get(0).getId();
        }
    }
}
