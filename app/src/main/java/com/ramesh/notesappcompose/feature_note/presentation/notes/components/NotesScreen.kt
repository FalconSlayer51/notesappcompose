package com.ramesh.notesappcompose.feature_note.presentation.notes.components

import android.icu.text.UnicodeSet.SpanCondition
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramesh.notesappcompose.feature_note.domain.model.Note
import com.ramesh.notesappcompose.feature_note.presentation.notes.NoteEvents
import com.ramesh.notesappcompose.feature_note.presentation.notes.NotesViewModel
import com.ramesh.notesappcompose.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Your Note", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = {
                        viewModel.onEvent(NoteEvents.ToggleOrderSection)
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Sort Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddEditNoteScreen.route) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChanged = {
                        viewModel.onEvent(NoteEvents.OrderEvent(it))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) {note: Note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(NoteEvents.DeleteNote(note))
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(message = "note deleted",actionLabel = "undo")
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NoteEvents.RestoreEvent)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}