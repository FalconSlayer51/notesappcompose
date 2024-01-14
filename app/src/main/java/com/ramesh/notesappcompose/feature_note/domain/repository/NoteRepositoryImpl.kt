package com.ramesh.notesappcompose.feature_note.domain.repository

import com.ramesh.notesappcompose.feature_note.data.data_source.NoteDao
import com.ramesh.notesappcompose.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao
): NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}