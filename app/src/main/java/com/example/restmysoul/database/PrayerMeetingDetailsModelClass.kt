package com.example.restmysoul.database

class PrayerMeetingDetailsModelClass(val pm_id: Int?=null,
                                    val u_id: Int,
                                     val pm_title: String,
                                     val freqcount: Int,
                                     val fromdatetime: String,
                                     val todatetime: String,
                                     val pm_loc: String,
                                     val fastingdetails_id: Int?=null,
                                     val prayerdetails_id: Int?=null,
                                     val is_done: Int?=null)

