package com.ramesh.notesappcompose.feature_note.presentation.add_editNote

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)