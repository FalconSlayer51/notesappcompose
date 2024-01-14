package com.ramesh.notesappcompose.feature_note.presentation.add_editNote

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class EnteredTitle(val newTitle: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val newContent: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class ChangeColor(val newColor: Int): AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()
}