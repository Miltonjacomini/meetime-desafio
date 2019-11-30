package br.com.meetime.desafio.repository;

import br.com.meetime.desafio.model.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    @Query("SELECT l FROM Lead l WHERE l.email = :email ")
    Optional<Lead> findByEmailAndReusable(String email);
}
