package com.ramesh.notesappcompose.feature_note.domain.use_cases

import com.ramesh.notesappcompose.feature_note.domain.model.InvalidNoteException
import com.ramesh.notesappcompose.feature_note.domain.model.Note
import com.ramesh.notesappcompose.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("title shouldn't be empty")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("content shouldn't be empty")
        }
        repository.insertNote(note)
    }
}