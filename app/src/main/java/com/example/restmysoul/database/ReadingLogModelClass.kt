package com.example.restmysoul.database

class ReadingLogModelClass (val readinglog_id: Int?=null,
                            val u_id: Int,
                            val readingdetails_id: Int,
                            val start_book: String,
                            val start_chapter: Int,
                            val start_verse: Int,
                            val end_book: String,
                            val end_chapter: Int,
                            val end_verse: Int,
                            val chapters_count: Int,
                            val datetime: String)