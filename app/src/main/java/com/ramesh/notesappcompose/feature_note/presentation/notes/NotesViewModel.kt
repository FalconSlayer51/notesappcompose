package com.ramesh.notesappcompose.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramesh.notesappcompose.feature_note.domain.model.Note
import com.ramesh.notesappcompose.feature_note.domain.use_cases.NoteUseCases
import com.ramesh.notesappcompose.feature_note.domain.util.NoteOrder
import com.ramesh.notesappcompose.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state
    private var recentlyDeleteNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(events: NoteEvents) {
        when(events) {
            is NoteEvents.DeleteNote -> {
                viewModelScope.launch {
                    recentlyDeleteNote = events.note
                    noteUseCases.deleteNote(events.note)
                }
            }
            is NoteEvents.OrderEvent -> {
                if (state.value.noteOrder::class == events.noteOrder::class &&
                    state.value.noteOrder.orderType == events.noteOrder.orderType){
                    return
                }
                getNotes(events.noteOrder)

            }
            is NoteEvents.RestoreEvent -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeleteNote ?: return@launch)
                    recentlyDeleteNote = null
                }
            }
            is NoteEvents.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesUsecase(noteOrder).onEach { notes: List<Note> ->
            _state.value = _state.value.copy(notes = notes,noteOrder = noteOrder)
        }.launchIn(viewModelScope)
    }
}