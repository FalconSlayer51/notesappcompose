package com.ramesh.notesappcompose.feature_note.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}