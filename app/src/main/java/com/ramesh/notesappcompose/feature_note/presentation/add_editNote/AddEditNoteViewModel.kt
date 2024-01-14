package com.ramesh.notesappcompose.feature_note.presentation.add_editNote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramesh.notesappcompose.feature_note.domain.model.Note
import com.ramesh.notesappcompose.feature_note.domain.use_cases.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title"
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventflow = _eventFlow.asSharedFlow()

    private var currentNotedId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also {
                        currentNotedId = it.id
                        _noteTitle.value = _noteTitle.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = it.content,
                            isHintVisible = false
                        )
                        _noteColor.intValue = it.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.intValue = event.newColor
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.newContent
                )
            }
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = _noteTitle.value.copy(
                    text = event.newTitle
                )
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNotedId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch (e: Exception) {
                        _eventFlow.emit(UiEvent.ShowSnackBar(e.message ?: "couldn't save note"))
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}