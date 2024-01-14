package com.ramesh.notesappcompose

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ramesh.notesappcompose.feature_note.presentation.add_editNote.AddEditNoteScreen
import com.ramesh.notesappcompose.feature_note.presentation.add_editNote.AddEditNoteViewModel
import com.ramesh.notesappcompose.feature_note.presentation.notes.NotesViewModel
import com.ramesh.notesappcompose.feature_note.presentation.notes.components.NotesScreen
import com.ramesh.notesappcompose.feature_note.presentation.util.Screen
import com.ramesh.notesappcompose.ui.theme.NotesappcomposeTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import androidx.lifecycle.viewmodel.compose.viewModel as viewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesappcomposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.NoteScreen.route) {
                        composable(route = Screen.NoteScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(name = "noteId"){
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name = "noteColor"){
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("noteColor")?: -1
                            AddEditNoteScreen(navController = navController, noteColor = color)
                        }
                    }
                }
            }
        }
    }
}