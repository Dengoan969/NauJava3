package ru.Golov_Denis.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Golov_Denis.NauJava.entity.ReportEntity;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {
}
