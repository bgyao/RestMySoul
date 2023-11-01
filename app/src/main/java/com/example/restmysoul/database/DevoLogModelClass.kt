package com.example.restmysoul.database

class DevoLogModelClass (val devolog_id: Int?=null,
                         val u_id: Int,
                         val devodetails_id: Int,
                         val datetime: String,
                         val start_book: String,
                         val start_chapter: Int,
                         val start_verse: Int,
                         val end_book: String,
                         val end_chapter: Int,
                         val end_verse: Int,
                         val theme: String,
                         val notes: String)