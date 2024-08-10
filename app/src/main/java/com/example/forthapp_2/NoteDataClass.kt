package com.example.forthapp_2

data class NoteDataClass(val title: String, val description: String, val noteId: String) {
    constructor() : this("", "", "")
}
