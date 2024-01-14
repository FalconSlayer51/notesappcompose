package com.ramesh.notesappcompose.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramesh.notesappcompose.ui.theme.BabyBlue
import com.ramesh.notesappcompose.ui.theme.LightGreen
import com.ramesh.notesappcompose.ui.theme.RedOrange
import com.ramesh.notesappcompose.ui.theme.RedPink
import com.ramesh.notesappcompose.ui.theme.Violet

@Entity
data class Note(
    val title: String,
    val content: String,
    val timeStamp: Long,
    val color: Int,
    @PrimaryKey
    val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, BabyBlue, Violet, RedPink)
    }
}

class InvalidNoteException(message: String):Exception(message)