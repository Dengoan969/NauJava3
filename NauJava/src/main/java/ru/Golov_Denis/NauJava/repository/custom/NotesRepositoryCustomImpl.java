package ru.Golov_Denis.NauJava.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;
import ru.Golov_Denis.NauJava.entity.NoteEntity;

import java.util.List;

@Repository
public class NotesRepositoryCustomImpl implements NotesRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NoteEntity> findByUserUsernameCriteria(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NoteEntity> cq = cb.createQuery(NoteEntity.class);
        Root<NoteEntity> note = cq.from(NoteEntity.class);

        cq.select(note)
                .where(cb.equal(note.get("user").get("username"), username));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<NoteEntity> findByTitleOrContentFragmentCriteria(String fragment) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NoteEntity> cq = cb.createQuery(NoteEntity.class);
        Root<NoteEntity> note = cq.from(NoteEntity.class);

        String pattern = "%" + fragment.toLowerCase() + "%";

        Predicate titleLike = cb.like(cb.lower(note.get("title")), pattern);
        Predicate contentLike = cb.like(cb.lower(note.get("content")), pattern);

        cq.select(note)
                .where(cb.or(titleLike, contentLike));

        return entityManager.createQuery(cq).getResultList();
    }
}
