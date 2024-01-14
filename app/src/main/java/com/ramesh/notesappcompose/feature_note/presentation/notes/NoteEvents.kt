package com.ramesh.notesappcompose.feature_note.presentation.notes

import com.ramesh.notesappcompose.feature_note.domain.model.Note
import com.ramesh.notesappcompose.feature_note.domain.util.NoteOrder

sealed class NoteEvents {
    data class OrderEvent(val noteOrder: NoteOrder): NoteEvents()
    data class DeleteNote(val note: Note): NoteEvents()
    object RestoreEvent: NoteEvents()
    object ToggleOrderSection: NoteEvents()
}