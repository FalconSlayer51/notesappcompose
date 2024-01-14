package com.ramesh.notesappcompose.feature_note.domain.use_cases

import com.ramesh.notesappcompose.feature_note.domain.model.Note
import com.ramesh.notesappcompose.feature_note.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}