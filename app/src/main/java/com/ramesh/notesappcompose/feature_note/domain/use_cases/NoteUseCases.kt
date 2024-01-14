package com.ramesh.notesappcompose.feature_note.domain.use_cases

data class NoteUseCases(
    val getNotesUsecase: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote
)
