package ru.Golov_Denis.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.Golov_Denis.NauJava.entity.NoteEntity;
import ru.Golov_Denis.NauJava.entity.NoteTagEntity;
import ru.Golov_Denis.NauJava.repository.NoteTagRepository;
import ru.Golov_Denis.NauJava.repository.NotesRepository;

import java.util.List;

@Service
public class NoteTransactionalServiceImpl implements NoteTransactionalService {

    private final NotesRepository notesRepository;
    private final NoteTagRepository noteTagRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public NoteTransactionalServiceImpl(
            NotesRepository notesRepository,
            NoteTagRepository noteTagRepository,
            PlatformTransactionManager transactionManager) {
        this.notesRepository = notesRepository;
        this.noteTagRepository = noteTagRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void deleteNoteWithTags(Long noteId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            NoteEntity note = notesRepository.findById(noteId)
                    .orElseThrow(() -> new RuntimeException("Note not found"));

            List<NoteTagEntity> noteTags = noteTagRepository.findByNote_Id(noteId);
            for (NoteTagEntity nt : noteTags) {
                noteTagRepository.delete(nt);
            }

            notesRepository.delete(note);

            transactionManager.commit(status);
        } catch (RuntimeException ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
