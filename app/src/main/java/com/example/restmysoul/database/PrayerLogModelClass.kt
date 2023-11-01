package com.example.restmysoul.database

class PrayerLogModelClass(val prayerlog_id: Int?=null,
                            val u_id: Int,
                          val prayerdetails_id: Int,
                          val datetime: String,
                          val theme: String,
                          val notes: String)