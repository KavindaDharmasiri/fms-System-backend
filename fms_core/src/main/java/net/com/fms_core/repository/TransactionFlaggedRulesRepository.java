package net.com.fms_core.repository;

import net.com.fms_core.entity.TransactionFlaggedRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionFlaggedRulesRepository extends JpaRepository<TransactionFlaggedRules, Integer> {
}
