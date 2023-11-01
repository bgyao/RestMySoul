package com.example.restmysoul.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.core.content.edit
import com.example.restmysoul.ui.goals.GoalsViewModel

class DatabaseHandler(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "RestMySoulDatabase"

        /* users table. for logging in, creating of accounts. deleted users table will follow */
        private const val TABLE_USERS = "users"
        private const val KEY_UID = "u_id"
        private const val KEY_FULL_NAME  = "full_name"
        private const val KEY_EMAIL  = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_RELIGION  = "religion"
        private const val KEY_CHURCH  = "church"

        /* bible reading details table. Users will add a goal and commit how many chapters per day,
        and number of days they will read */
        private const val TABLE_READING_DETAILS = "reading_details"
        private const val KEY_READINGDETAILSID = "readingdetails_id"
        private const val KEY_NUMCHAPTERS   = "num_chapters"
        private const val KEY_NUMDAYS  = "num_days"

        /* users will manually input their bible reading activity. */
        private const val TABLE_READING_LOG = "reading_log"
        private const val KEY_READINGLOGID = "readinglog_id"
        private const val KEY_CHAPTERS_COUNT   = "chapters_count"

        /* devo_details table */
        private const val TABLE_DEVO_DETAILS = "devo_details"
        private const val KEY_DEVODETAILS_ID = "devodetails_id"

        /* devo_log table */
        private const val TABLE_DEVO_LOG = "devo_log"
        private const val KEY_DEVOLOG_ID = "devolog_id"

        /* prayer_details table */
        private const val TABLE_PRAYER_DETAILS = "prayer_details"
        private const val KEY_PRAYERDETAILS_ID = "prayerdetails_id"

        /* prayer_log table */
        private const val TABLE_PRAYER_LOG = "prayer_log"
        private const val KEY_PRAYERLOG_ID = "prayerlog_id"

        /* prayer_meeting_details */
        private const val TABLE_PM_DETAILS = "prayer_meeting_details"
        private const val KEY_PM_ID = "pm_id"
        private const val KEY_PM_TITLE = "pm_title"
        private const val KEY_PM_LOC = "pm_location"

        /* fasting_details table */
        private const val TABLE_FASTING_DETAILS = "fasting_details"
        private const val KEY_FASTINGDETAILS_ID = "fastingdetails_id"

        /* fasting_meals table */
        private const val TABLE_FASTING_MEALS = "fasting_meals"
        private const val KEY_FM_ID = "fm_id"
        private const val KEY_FM_NAME = "fm_name" //Breakfast, Lunch, Dinner, Others
        private const val KEY_FASTING_TYPE = "fast_type" //manual input. App will suggest as a tooltip which fasting types are recommended

        /*REUSABLES*/
        private const val KEY_START_BIBLE_BOOK = "start_bible_book"
        private const val KEY_START_BOOK_CHAPTER = "start_book_chapter"
        private const val KEY_START_CHAPTER_VERSE = "start_chapter_verse"
        private const val KEY_END_BIBLE_BOOK = "end_bible_book"
        private const val KEY_END_BOOK_CHAPTER = "end_book_chapter"
        private const val KEY_END_CHAPTER_VERSE = "end_chapter_verse"
        private const val KEY_FREQUENCY   = "frequency" //daily? weekly? monthly?
        private const val KEY_FREQ_COUNT   = "frequency_count" //1x a day/week/month,2x,3x,...
        private const val KEY_DATETIME = "datetime" //in TEXT datatype for SQLite
        private const val KEY_FROMDATETIME = "from_datetime" //in TEXT datatype for SQLite
        private const val KEY_TODATETIME = "to_datetime" //in TEXT datatype for SQLite
        //public static final SimpleDateFormat MONTH_DAY_YEAR_TIME = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
        private const val KEY_THEME = "theme" //a title or general description for the prayer or devotional
        private const val KEY_NOTES = "notes" //long text. for taking down notes
        private const val KEY_IS_DONE = "is_done" //marks a goal if done.

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = ("CREATE TABLE " + TABLE_USERS + " ("
                + KEY_UID + " INTEGER PRIMARY KEY, " //primary key of users table
                + KEY_FULL_NAME + " TEXT, " // full name of user
                + KEY_EMAIL + " TEXT, " //email address, to be used for logging in
                + KEY_PASSWORD + " TEXT, " //password, to be encrypted before encoding to db
                + KEY_RELIGION + " TEXT, " //Roman Catholic or Christian Denomination
                + KEY_CHURCH + " TEXT" //church name
                + ")")
        db?.execSQL(CREATE_USERS_TABLE)

        val CREATE_READING_DETAILS_TABLE = ("CREATE TABLE " + TABLE_READING_DETAILS + " ("
                + KEY_READINGDETAILSID + " INTEGER PRIMARY KEY, " //bible reading primary key
                + KEY_UID + " INTEGER, " //links this table to which user this bible reading plan belongs
                + KEY_NUMCHAPTERS + " INTEGER, " //target number of bible chapters to read per day
                + KEY_NUMDAYS + " INTEGER, " //number of days this goal will be available
                + KEY_IS_DONE + " INTEGER" //marks the goal as done/finished/closed
                + ")")
        db?.execSQL(CREATE_READING_DETAILS_TABLE)
        /* NOTE: Make sure to add error checking: "You have an ongoing reading goal" of some sort. */

        val CREATE_READING_LOG_TABLE = ("CREATE TABLE " + TABLE_READING_LOG + " ("
                + KEY_READINGLOGID + " INTEGER PRIMARY KEY, " //primary key of reading log entry
                + KEY_UID + " INTEGER, " //links reading log to user
                + KEY_READINGDETAILSID + " INTEGER, " //links reading log to the goal created
                + KEY_DATETIME + " TEXT, " //the date and time the entry was created.
                /* user inputs the Bible Book, Chapter, and verse started */
                + KEY_START_BIBLE_BOOK + " TEXT, " //Genesis, Exodus, Matthew, John, etc
                + KEY_START_BOOK_CHAPTER + " INTEGER, " //bible chapter
                + KEY_START_CHAPTER_VERSE + " INTEGER, "//bible verse

                /* user inputs the Bible Book, Chapter, and verse ended */
                + KEY_END_BIBLE_BOOK + " TEXT, " //Malachi, Revelation, etc.
                + KEY_END_BOOK_CHAPTER + " INTEGER, " //bible chapter
                + KEY_END_CHAPTER_VERSE + " INTEGER, " //bible verse
                + KEY_CHAPTERS_COUNT + " INTEGER" // user inputs how many chapters lapsed. feature of autocompute not yet available
                + ")")
        db?.execSQL(CREATE_READING_LOG_TABLE)

        val CREATE_DEVODETAILS_TABLE = ("CREATE TABLE " + TABLE_DEVO_DETAILS + " ("
                + KEY_DEVODETAILS_ID + " INTEGER PRIMARY KEY, " //devotional goal details PK
                + KEY_UID + " INTEGER, " //links the devo goal to the user
                + KEY_FREQUENCY + " TEXT, " //daily? weekly? monthly?
                + KEY_FREQ_COUNT + " INTEGER, "//1x a day/wk/mo, 2x a day/wk/mo, etc...
                + KEY_IS_DONE + " INTEGER" //marks the goal as done/finished/closed
                + ")")
        db?.execSQL(CREATE_DEVODETAILS_TABLE)

        val CREATE_DEVO_LOG_TABLE = ("CREATE TABLE " + TABLE_DEVO_LOG + " ("
                + KEY_DEVOLOG_ID + " INTEGER PRIMARY KEY, "
                + KEY_UID + " INTEGER, "
                + KEY_DEVODETAILS_ID + " INTEGER, "
                + KEY_DATETIME + " TEXT, "
                + KEY_START_BIBLE_BOOK + " TEXT, "
                + KEY_START_BOOK_CHAPTER + " INTEGER, "
                + KEY_START_CHAPTER_VERSE + " INTEGER, "
                + KEY_END_BIBLE_BOOK + " TEXT, "
                + KEY_END_BOOK_CHAPTER + " INTEGER, "
                + KEY_END_CHAPTER_VERSE + " INTEGER, "
                + KEY_THEME + " TEXT, "
                + KEY_NOTES + " TEXT"
                + ")")
        db?.execSQL(CREATE_DEVO_LOG_TABLE)

        val CREATE_PRAYER_DETAILS_TABLE = ("CREATE TABLE " + TABLE_PRAYER_DETAILS + " ("
                + KEY_PRAYERDETAILS_ID + " INTEGER PRIMARY KEY, "
                + KEY_UID + " INTEGER, "
                + KEY_FREQUENCY + " TEXT, "
                + KEY_FREQ_COUNT + " INTEGER, "
                + KEY_PM_ID + " INTEGER, " //can be null. Null indicates not attending a prayer meeting
                + KEY_FASTINGDETAILS_ID + " INTEGER, "  //can be null. Null indicates not fasting.
                + KEY_IS_DONE + " INTEGER" //marks the goal as done/finished/closed
                + ")")
        db?.execSQL(CREATE_PRAYER_DETAILS_TABLE)

        val CREATE_PRAYER_LOG_TABLE = ("CREATE TABLE " + TABLE_PRAYER_LOG + " ("
                + KEY_PRAYERLOG_ID + " INTEGER PRIMARY KEY, "
                + KEY_UID + " INTEGER, "
                + KEY_PRAYERDETAILS_ID + " INTEGER, "
                + KEY_DATETIME + " TEXT, "
                + KEY_THEME + " TEXT, "
                + KEY_NOTES + " TEXT"
                + ")")
        db?.execSQL(CREATE_PRAYER_LOG_TABLE)

        val CREATE_PM_DETAILS_TABLE = ("CREATE TABLE " + TABLE_PM_DETAILS + " ("
                + KEY_PM_ID + " INTEGER PRIMARY KEY, "
                + KEY_UID + " INTEGER, "
                + KEY_PM_TITLE + " TEXT, "
                + KEY_FASTINGDETAILS_ID + " INTEGER, "
                + KEY_PRAYERDETAILS_ID + " INTEGER, "
                + KEY_FROMDATETIME + " TEXT, "
                + KEY_TODATETIME + " TEXT, "
                + KEY_PM_LOC + " TEXT, "
                + KEY_IS_DONE + " INTEGER" //marks the goal as done/finished/closed
                + ")")
        db?.execSQL(CREATE_PM_DETAILS_TABLE)

        val CREATE_FASTING_DETAILS_TABLE = ("CREATE TABLE " + TABLE_FASTING_DETAILS + " ("
                + KEY_FASTINGDETAILS_ID + " INTEGER PRIMARY KEY, "
                + KEY_UID + " INTEGER, "
                + KEY_PM_ID + " INTEGER, "
                + KEY_FREQUENCY + " TEXT, "
                + KEY_FREQ_COUNT + " INTEGER, "
                + KEY_FROMDATETIME + " TEXT, "
                + KEY_TODATETIME + " TEXT, "
                + KEY_IS_DONE + " INTEGER" //marks the goal as done/finished/closed
                + ")")
        db?.execSQL(CREATE_FASTING_DETAILS_TABLE)

        val CREATE_FASTING_MEALS_TABLE = ("CREATE TABLE " + TABLE_FASTING_MEALS + " ("
                + KEY_FM_ID + " INTEGER PRIMARY KEY, "
                + KEY_UID + " INTEGER, "
                + KEY_FASTINGDETAILS_ID + " INTEGER, "
                + KEY_FM_NAME + " TEXT, " //Breakfast, Lunch, Dinner, Other
                + KEY_FASTING_TYPE + " TEXT, " //manual input of user; app will display recommended
                + KEY_DATETIME + " TEXT, "
                + KEY_IS_DONE + " INTEGER" //marks the goal as done/finished/closed
                + ")")
        db?.execSQL(CREATE_FASTING_MEALS_TABLE)


        /* connect prayer meetings table with prayer table and fasting table.
        * Note: integrate prayer and fasting, and prayer meetings in a form.
        * Example: "Is this a prayer meeting? [Y/N]" then
        * "Do you intend to fast? [Y/N]" <-- this will appear regardless if it is a PM or not
        * Sources: https://www.geeksforgeeks.org/how-to-view-and-locate-sqlite-database-in-android-studio/
        * https://www.youtube.com/watch?v=CzGNaiSoh7E
        *  */
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVO_DETAILS)
        onCreate(db = db)
    }

    fun addUser(usr: UserModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_FULL_NAME, usr.fullName)
        contentValues.put(KEY_EMAIL, usr.email)
        contentValues.put(KEY_PASSWORD, usr.password)
        contentValues.put(KEY_RELIGION, usr.religion)
        contentValues.put(KEY_CHURCH, usr.church)


        val success = db.insert(TABLE_USERS, null, contentValues)
        usr.u_id?.let { setUserId(it) }

        db.close()

        return success
    }
    private val sharedPreferences : SharedPreferences? = null
    fun setUserId(user_id: Int) {
        sharedPreferences?.edit {
            putInt(KEY_UID,user_id)
        }
    }

    fun getUserId() : Int? = sharedPreferences?.getInt(KEY_UID, 0)

    @SuppressLint("Range")
    fun loginThisUser(login_email:String, login_pass:String) : Boolean {

        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $KEY_EMAIL = \"$login_email\" AND $KEY_PASSWORD =\"$login_pass\"", null)

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val u_id = cursor.getInt(cursor.getColumnIndex(KEY_UID))
            setUserId(u_id)
            db.close()
            return true
        }
        db.close()
        return false
    }

    @SuppressLint("Range")
    fun checkIfEmailExists(signup_email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $KEY_EMAIL = \"$signup_email\"", null)

        if (cursor.count > 0) {
            db.close()
            return true
        }
        db.close()
        return false
    }

    fun createReadingGoal(rdetails: ReadDetailsModelClass): Long {
        val db = this.writableDatabase

        val user_id = getUserId()

        val contentValues = ContentValues()
        rdetails.u_id = user_id
        contentValues.put(KEY_NUMCHAPTERS, rdetails.numchapters)
        contentValues.put(KEY_NUMDAYS, rdetails.numdays)
        contentValues.put(KEY_IS_DONE, rdetails.is_done)

        val success = db.insert(TABLE_READING_DETAILS, null, contentValues)

        db.close()

        return success
    }

    /**************************************************************************************
     * FEATURES COMING SOON:
     * Devotional Goals
     * Prayer Goals with Prayer meetings and/or fasting goals

    fun createDevoGoal(devdetails: DevoDetailsModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_UID, devdetails.u_id)
        contentValues.put(KEY_FREQUENCY, devdetails.frequency)
        contentValues.put(KEY_FREQ_COUNT, devdetails.freqcount)
        contentValues.put(KEY_IS_DONE, devdetails.is_done)

        val success = db.insert(TABLE_DEVO_DETAILS, null, contentValues)

        db.close()

        return success
    }

    fun createPrayerGoal(praydetails: PrayerDetailsModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_UID, praydetails.u_id)
        contentValues.put(KEY_FREQUENCY, praydetails.frequency)
        contentValues.put(KEY_FREQ_COUNT, praydetails.freqcount)
        contentValues.put(KEY_PM_ID, praydetails.pm_id)
        contentValues.put(KEY_FASTINGDETAILS_ID, praydetails.fastingdetails_id)
        contentValues.put(KEY_IS_DONE, praydetails.is_done)

        val success = db.insert(TABLE_PRAYER_DETAILS, null, contentValues)

        db.close()

        return success
    }

    fun createFastingGoal(fastdetails: FastingDetailsModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_UID, fastdetails.u_id)
        contentValues.put(KEY_PM_ID, fastdetails.pm_id)
        contentValues.put(KEY_FREQUENCY, fastdetails.frequency)
        contentValues.put(KEY_FREQ_COUNT, fastdetails.freqcount)
        contentValues.put(KEY_FROMDATETIME, fastdetails.fromdatetime)
        contentValues.put(KEY_TODATETIME, fastdetails.todatetime)
        contentValues.put(KEY_PM_ID, fastdetails.pm_id)
        contentValues.put(KEY_FASTINGDETAILS_ID, fastdetails.fastingdetails_id)
        contentValues.put(KEY_IS_DONE, fastdetails.is_done)

        val success = db.insert(TABLE_FASTING_DETAILS, null, contentValues)

        db.close()

        return success
    }

    fun createFastingMeal(fastmealdetails: FastMealDetailsModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_UID, fastmealdetails.u_id)
        contentValues.put(KEY_FASTINGDETAILS_ID, fastmealdetails.fastingdetails_id)
        contentValues.put(KEY_FM_NAME, fastmealdetails.fm_name)
        contentValues.put(KEY_FASTING_TYPE, fastmealdetails.fasting_type)
        contentValues.put(KEY_DATETIME, fastmealdetails.datetime)
        contentValues.put(KEY_IS_DONE, fastmealdetails.is_done)

        val success = db.insert(TABLE_FASTING_MEALS, null, contentValues)

        db.close()

        return success
    }

    fun createPrayerMeeting(praymeetdetails: PrayerMeetingDetailsModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_UID, praymeetdetails.u_id)
        contentValues.put(KEY_PM_TITLE, praymeetdetails.pm_title)
        contentValues.put(KEY_FREQ_COUNT, praymeetdetails.freqcount)
        contentValues.put(KEY_FROMDATETIME, praymeetdetails.fromdatetime)
        contentValues.put(KEY_TODATETIME, praymeetdetails.todatetime)
        contentValues.put(KEY_PM_LOC, praymeetdetails.pm_loc)
        contentValues.put(KEY_FASTINGDETAILS_ID, praymeetdetails.fastingdetails_id)
        contentValues.put(KEY_PRAYERDETAILS_ID, praymeetdetails.prayerdetails_id)
        contentValues.put(KEY_IS_DONE, praymeetdetails.is_done)

        val success = db.insert(TABLE_PM_DETAILS, null, contentValues)

        db.close()

        return success
    }

    fun createReadingLog(readlog: ReadingLogModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_UID, readlog.u_id)
        contentValues.put(KEY_READINGDETAILSID, readlog.readingdetails_id)
        contentValues.put(KEY_START_BIBLE_BOOK, readlog.start_book)
        contentValues.put(KEY_START_BOOK_CHAPTER, readlog.start_chapter)
        contentValues.put(KEY_START_CHAPTER_VERSE, readlog.start_verse)
        contentValues.put(KEY_END_BIBLE_BOOK, readlog.end_book)
        contentValues.put(KEY_END_BOOK_CHAPTER, readlog.end_chapter)
        contentValues.put(KEY_END_CHAPTER_VERSE, readlog.end_verse)
        contentValues.put(KEY_CHAPTERS_COUNT, readlog.chapters_count)
        contentValues.put(KEY_DATETIME, readlog.datetime)

        val success = db.insert(TABLE_READING_LOG, null, contentValues)

        db.close()

        return success
    }

    fun createDevoLog(devolog: DevoLogModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_UID, devolog.u_id)
        contentValues.put(KEY_DEVODETAILS_ID, devolog.devodetails_id)
        contentValues.put(KEY_DATETIME, devolog.datetime)
        contentValues.put(KEY_START_BIBLE_BOOK, devolog.start_book)
        contentValues.put(KEY_START_BOOK_CHAPTER, devolog.start_chapter)
        contentValues.put(KEY_START_CHAPTER_VERSE, devolog.start_verse)
        contentValues.put(KEY_END_BIBLE_BOOK, devolog.end_book)
        contentValues.put(KEY_END_BOOK_CHAPTER, devolog.end_chapter)
        contentValues.put(KEY_END_CHAPTER_VERSE, devolog.end_verse)
        contentValues.put(KEY_THEME, devolog.theme)
        contentValues.put(KEY_NOTES, devolog.notes)

        val success = db.insert(TABLE_DEVO_LOG, null, contentValues)

        db.close()

        return success
    }

    fun createPrayerLog(prayerlog: PrayerLogModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_UID, prayerlog.u_id)
        contentValues.put(KEY_PRAYERDETAILS_ID, prayerlog.prayerdetails_id)
        contentValues.put(KEY_DATETIME, prayerlog.datetime)
        contentValues.put(KEY_THEME, prayerlog.theme)
        contentValues.put(KEY_NOTES, prayerlog.notes)

        val success = db.insert(TABLE_PRAYER_LOG, null, contentValues)

        db.close()

        return success
    } */

    /*** ALL RETRIEVAL/READ FUNCTIONS GO HERE ***/

    @SuppressLint("Range")
    fun viewUserDetails (u_email:String, u_pass:String): ArrayList<UserModelClass> {
        val userDetails: ArrayList<UserModelClass> = ArrayList<UserModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE $KEY_EMAIL = $u_email AND $KEY_PASSWORD = $u_pass"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var u_id: Int
        var fullName: String
        var email: String
        var password: String
        var religion: String
        var church: String

        //this logic parses all user data in the table. This is not how we intend this to work.
        //what we need is for the table to retrieve data based on login credentials: email and pass
        // this whole logic will work, just adjust the SELECT query!
        if (cursor.moveToFirst()) {
            do {
                u_id = cursor.getInt(cursor.getColumnIndex(KEY_UID))
                fullName = cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
                religion = cursor.getString(cursor.getColumnIndex(KEY_RELIGION))
                church = cursor.getString(cursor.getColumnIndex(KEY_CHURCH))

                val user = UserModelClass(u_id = u_id, fullName = fullName, email = email, password = password, religion = religion, church = church)
                userDetails.add(user)
            } while(cursor.moveToNext())
        }
        db.close()
        return userDetails
    }


    /**************** FEATURE COMING SOON! *************************
    @SuppressLint("Range")
    fun viewDevoPlans (): ArrayList<DevoDetailsModelClass> {
        /* Parse all devotional plans of the current user defined by KEY_UID */
        val devoDetails: ArrayList<DevoDetailsModelClass> = ArrayList<DevoDetailsModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_DEVO_DETAILS" // WHERE $KEY_UID = '?????'"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var devodetails_id: Int
        var u_id: Int
        var frequency: String
        var freqcount: Int
        var is_done: Int?=null


        if (cursor.moveToFirst()) {
            do {
                devodetails_id = cursor.getInt(cursor.getColumnIndex(KEY_DEVODETAILS_ID))
                u_id = cursor.getInt(cursor.getColumnIndex(KEY_UID))
                frequency = cursor.getString(cursor.getColumnIndex(KEY_FREQUENCY))
                freqcount = cursor.getInt(cursor.getColumnIndex(KEY_FREQ_COUNT))
                is_done = cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE))

                val devodetails = DevoDetailsModelClass(devodetails_id = devodetails_id, u_id = u_id, frequency = frequency, freqcount = freqcount, is_done = is_done)
                devoDetails.add(devodetails)
            } while(cursor.moveToNext())
        }

        return devoDetails
    }

    @SuppressLint("Range")
    fun viewDevoLogs (user_id: Int, devoPlan_id: Int?=null): ArrayList<DevoLogModelClass> {
        val devoLog: ArrayList<DevoLogModelClass> = ArrayList<DevoLogModelClass>()
        val selectQuery: String

        selectQuery = if(devoPlan_id==null) {
            "SELECT * FROM $TABLE_DEVO_LOG WHERE $KEY_UID = $user_id"
        } else {
            "SELECT * FROM $TABLE_DEVO_LOG WHERE $KEY_UID = $user_id AND $KEY_DEVODETAILS_ID = $devoPlan_id"
        }


        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var devolog_id: Int
        var u_id: Int
        var devodetails_id: Int
        var datetime: String
        var start_book: String
        var start_chapter: Int
        var start_verse: Int
        var end_book: String
        var end_chapter: Int
        var end_verse: Int
        var theme: String
        var notes: String

        if (cursor.moveToFirst()) {
            do {
                devolog_id = cursor.getInt(cursor.getColumnIndex(KEY_DEVOLOG_ID))
                u_id = cursor.getInt(cursor.getColumnIndex(KEY_UID))
                devodetails_id = cursor.getInt(cursor.getColumnIndex(KEY_DEVODETAILS_ID))
                datetime = cursor.getString(cursor.getColumnIndex(KEY_DATETIME))
                start_book = cursor.getString(cursor.getColumnIndex(KEY_START_BIBLE_BOOK))
                start_chapter = cursor.getInt(cursor.getColumnIndex(KEY_START_BOOK_CHAPTER))
                start_verse = cursor.getInt(cursor.getColumnIndex(KEY_START_CHAPTER_VERSE))
                end_book = cursor.getString(cursor.getColumnIndex(KEY_END_BIBLE_BOOK))
                end_chapter = cursor.getInt(cursor.getColumnIndex(KEY_END_BOOK_CHAPTER))
                end_verse = cursor.getInt(cursor.getColumnIndex(KEY_END_CHAPTER_VERSE))
                theme = cursor.getString(cursor.getColumnIndex(KEY_THEME))
                notes = cursor.getString(cursor.getColumnIndex(KEY_NOTES))

                val devoLogs = DevoLogModelClass(
                    devolog_id = devolog_id,
                    u_id = u_id,
                    devodetails_id = devodetails_id,
                    datetime = datetime,
                    start_book = start_book,
                    start_chapter = start_chapter,
                    start_verse = start_verse,
                    end_book = end_book,
                    end_chapter = end_chapter,
                    end_verse = end_verse,
                    theme = theme,
                    notes = notes)
                devoLog.add(devoLogs)

            } while(cursor.moveToNext())
        }

        return devoLog
    }
 *******************************************************************************************/

    @SuppressLint("Range")
    fun viewReadingPlans (user_id: Int): ArrayList<ReadDetailsModelClass> {
        val readDetails = ArrayList<ReadDetailsModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_READING_DETAILS WHERE $KEY_UID = $user_id"
     /* val selectQuery = if(readingdetails_id!=null) "SELECT * FROM $TABLE_READING_DETAILS WHERE $KEY_UID = $user_id AND $KEY_READINGDETAILSID = $readingdetails_id"
                        else "SELECT * FROM $TABLE_READING_DETAILS WHERE $KEY_UID = $user_id" */

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(selectQuery, null)

        var readingPlandetails_id: Int
        var numchapters: Int
        var numdays: Int
        var is_done: Int?=null

        if (cursor.moveToFirst()) {
            do {
                readingPlandetails_id = cursor.getInt(cursor.getColumnIndex(KEY_READINGDETAILSID))

                numchapters = cursor.getInt(cursor.getColumnIndex(KEY_NUMCHAPTERS))
                numdays = cursor.getInt(cursor.getColumnIndex(KEY_NUMDAYS))
                is_done = cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE))

                val readingPlans = ReadDetailsModelClass(
                    readingdetails_id = readingPlandetails_id,
                    u_id = user_id,
                    numchapters = numchapters,
                    numdays = numdays,
                    is_done = is_done)
                readDetails.add(readingPlans)

            } while(cursor.moveToNext())
        }

        return readDetails
    }

    @SuppressLint("Range")
    fun viewReadingLogs (user_id: Int, readingPlan_id:Int?=null, thisReadingLog_id:Int?=null): ArrayList<ReadingLogModelClass> {
        val readLogs: ArrayList<ReadingLogModelClass> = ArrayList<ReadingLogModelClass>()


        val selectQuery = when {
            readingPlan_id != null && thisReadingLog_id == null -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = $user_id AND $KEY_READINGDETAILSID = $readingPlan_id"
            readingPlan_id == null && thisReadingLog_id != null -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = $user_id AND $KEY_READINGLOGID = $thisReadingLog_id"
            readingPlan_id != null && thisReadingLog_id != null -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = $user_id AND $KEY_READINGDETAILSID = $readingPlan_id AND $KEY_READINGLOGID = $thisReadingLog_id"
            else -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = $user_id"
        }

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(selectQuery, null)

        var readinglog_id: Int
        var u_id: Int
        var readingdetails_id: Int
        var start_book: String
        var start_chapter: Int
        var start_verse: Int
        var end_book: String
        var end_chapter: Int
        var end_verse: Int
        var chapters_count: Int
        var datetime: String

        if (cursor.moveToFirst()) {
            do {
                readinglog_id = cursor.getInt(cursor.getColumnIndex(KEY_READINGLOGID))
                u_id = cursor.getInt(cursor.getColumnIndex(KEY_UID))
                readingdetails_id = cursor.getInt(cursor.getColumnIndex(KEY_READINGDETAILSID))
                datetime = cursor.getString(cursor.getColumnIndex(KEY_DATETIME))
                start_book = cursor.getString(cursor.getColumnIndex(KEY_START_BIBLE_BOOK))
                start_chapter = cursor.getInt(cursor.getColumnIndex(KEY_START_BOOK_CHAPTER))
                start_verse = cursor.getInt(cursor.getColumnIndex(KEY_START_CHAPTER_VERSE))
                end_book = cursor.getString(cursor.getColumnIndex(KEY_END_BIBLE_BOOK))
                end_chapter = cursor.getInt(cursor.getColumnIndex(KEY_END_BOOK_CHAPTER))
                end_verse = cursor.getInt(cursor.getColumnIndex(KEY_END_CHAPTER_VERSE))
                chapters_count = cursor.getInt(cursor.getColumnIndex(KEY_CHAPTERS_COUNT))

                val readingLog = ReadingLogModelClass(
                    readinglog_id = readinglog_id,
                    u_id = u_id,
                    readingdetails_id = readingdetails_id,
                    datetime = datetime,
                    start_book = start_book,
                    start_chapter = start_chapter,
                    start_verse = start_verse,
                    end_book = end_book,
                    end_chapter = end_chapter,
                    end_verse = end_verse,
                    chapters_count = chapters_count)
                readLogs.add(readingLog) //add the readingLog data to readLogs array.

            } while(cursor.moveToNext())
        }

        return readLogs
    }

    @SuppressLint("Range")
    fun readingLogCount (/*user_id: Int?, readingPlan_id:Int?=null, thisReadingLog_id:Int?=null*/): Cursor? {

        /** error here!!!!!! **/
        val selectQuery = "SELECT * FROM $TABLE_READING_DETAILS" /*when {
            readingPlan_id != null && thisReadingLog_id == null -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = $user_id AND $KEY_READINGDETAILSID = $readingPlan_id"
            readingPlan_id == null && thisReadingLog_id != null -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = $user_id AND $KEY_READINGLOGID = $thisReadingLog_id"
            readingPlan_id != null && thisReadingLog_id != null -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = $user_id AND $KEY_READINGDETAILSID = $readingPlan_id AND $KEY_READINGLOGID = $thisReadingLog_id"
            else -> "SELECT * FROM $TABLE_READING_LOG WHERE $KEY_UID = 1"
        }*/

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(selectQuery, null)

        return cursor
    }
 /*********************** Features Coming Soon! *********************************************
    @SuppressLint("Range")
    fun viewPrayerPlans (user_id: Int): ArrayList<PrayerDetailsModelClass> {
        val prayerPlans: ArrayList<PrayerDetailsModelClass> = ArrayList<PrayerDetailsModelClass>()
        val prayerMeetings: ArrayList<PrayerMeetingDetailsModelClass> = ArrayList<PrayerMeetingDetailsModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_PRAYER_DETAILS " +
                "LEFT JOIN $TABLE_PM_DETAILS ON $TABLE_PRAYER_DETAILS.$KEY_PM_ID = $TABLE_PM_DETAILS.$KEY_PM_ID " +
                "LEFT JOIN $TABLE_FASTING_DETAILS ON $TABLE_PRAYER_DETAILS.$KEY_FASTINGDETAILS_ID = $TABLE_FASTING_DETAILS.$KEY_FASTINGDETAILS_ID " +
                "WHERE $KEY_UID = $user_id"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        /* from table prayer goal details */
        var t1_prayerdetails_id: Int?=null
        var u_id: Int
        var t1_frequency: String
        var t1_freqcount: Int
        var t1_pm_id: Int?=null
        var t1_fastingdetails_id: Int?=null
        var t1_is_done: Int?=null

        /* from prayer meeting details table */
        var t2_pm_title: String?=null
        var t2_freqcount: Int?=null
        var t2_fromdatetime: String?=null
        var t2_todatetime: String?=null
        var t2_pm_loc: String?=null
        var t2_is_done: Int?=null

        /* from fasting details table */
        var t3_fastingdetails_id: Int?=null
        var t3_u_id: Int?=null
        var t3_frequency: String?=null
        var t3_freqcount: Int?=null
        var t3_fromdatetime: String?=null
        var t3_todatetime: String?=null
        var t3_pm_id: Int?=null
        var t3_is_done: Int?=null


        if (cursor.moveToFirst()) {
            do {
                /* getting details from the prayer goal details table */
                t1_prayerdetails_id = cursor.getInt(cursor.getColumnIndex(KEY_PRAYERDETAILS_ID))
                u_id = cursor.getInt(cursor.getColumnIndex(KEY_UID))
                t1_frequency = cursor.getString(cursor.getColumnIndex(KEY_FREQUENCY))
                t1_freqcount = cursor.getInt(cursor.getColumnIndex(KEY_FREQ_COUNT))
                t1_pm_id = cursor.getInt(cursor.getColumnIndex(KEY_PM_ID))
                t1_fastingdetails_id = cursor.getInt(cursor.getColumnIndex(KEY_FASTINGDETAILS_ID))
                t1_is_done = cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE))

                /* getting details from the prayer meeting details table if prayer goal is connected to one */
                t2_pm_title = cursor.getString(cursor.getColumnIndex(KEY_PM_TITLE))
                t2_freqcount = cursor.getInt(cursor.getColumnIndex(KEY_FREQ_COUNT))
                t2_fromdatetime = cursor.getString(cursor.getColumnIndex(KEY_FROMDATETIME))
                t2_todatetime = cursor.getString(cursor.getColumnIndex(KEY_TODATETIME))
                t2_pm_loc = cursor.getString(cursor.getColumnIndex(KEY_PM_LOC))
                t2_is_done = cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE))

                /* getting details from the fasting details table if prayer goal is connected to one */
                var t3_fastingdetails_id: Int?=null
                var t3_u_id: Int?=null
                var t3_frequency: String?=null
                var t3_freqcount: Int?=null
                var t3_fromdatetime: String?=null
                var t3_todatetime: String?=null
                var t3_pm_id: Int?=null
                var t3_is_done: Int?=null

                val prayerGoal = PrayerDetailsModelClass(
                    prayerdetails_id = t1_prayerdetails_id,
                    u_id = u_id,
                    frequency = t1_frequency,
                    freqcount = t1_freqcount,
                    pm_id = t1_pm_id,
                    fastingdetails_id = t1_fastingdetails_id,
                    is_done = t1_is_done)
                prayerPlans.add(prayerGoal) //add the readingLog data to readLogs array.

                val pmDetail = PrayerMeetingDetailsModelClass(
                    pm_id = t1_pm_id,
                    u_id = u_id,
                    pm_title = t2_pm_title,
                    freqcount = t2_freqcount,
                    fromdatetime = t2_fromdatetime,
                    todatetime = t2_todatetime,
                    pm_loc = t2_pm_loc,
                    fastingdetails_id = t1_fastingdetails_id,
                    prayerdetails_id = t1_prayerdetails_id,
                    is_done = t2_is_done)
                prayerMeetings.add(pmDetail)

            } while(cursor.moveToNext())
        }

        return prayerPlans
    }*/

    /* all update functions go here */

    fun updateUserDetails (thisUser: UserModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues ()
        contentValues.put(KEY_FULL_NAME, thisUser.fullName)
        contentValues.put(KEY_RELIGION, thisUser.religion)
        contentValues.put(KEY_CHURCH, thisUser.church)

        val success = db.update(TABLE_USERS, contentValues, KEY_UID + "=" + thisUser.u_id, null)
        db.close()
        return success
    }

    fun changeUserPass (thisUser: UserModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_EMAIL, thisUser.email)
        contentValues.put(KEY_PASSWORD, thisUser.password)

        val success = db.update(TABLE_USERS, contentValues, KEY_UID + "=" + thisUser.u_id, null)
        db.close()
        return success
    }

    fun updateReadingPlan(thisReadingPlan: ReadDetailsModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_UID, thisReadingPlan.u_id)
        contentValues.put(KEY_NUMCHAPTERS, thisReadingPlan.numchapters)
        contentValues.put(KEY_NUMDAYS, thisReadingPlan.numdays)
        contentValues.put(KEY_IS_DONE, thisReadingPlan.is_done)

        val success = db.update(TABLE_READING_DETAILS, contentValues, KEY_READINGDETAILSID + "=" + thisReadingPlan.readingdetails_id, null)

        db.close()
        return success
    }

    fun updateReadingLog(thisReadingLog: ReadingLogModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_START_BIBLE_BOOK, thisReadingLog.start_book)
        contentValues.put(KEY_START_BOOK_CHAPTER, thisReadingLog.start_chapter)
        contentValues.put(KEY_START_CHAPTER_VERSE, thisReadingLog.start_verse)
        contentValues.put(KEY_END_BIBLE_BOOK, thisReadingLog.end_book)
        contentValues.put(KEY_END_BOOK_CHAPTER, thisReadingLog.end_chapter)
        contentValues.put(KEY_END_CHAPTER_VERSE, thisReadingLog.end_verse)
        contentValues.put(KEY_NUMCHAPTERS, thisReadingLog.chapters_count)
        contentValues.put(KEY_DATETIME, thisReadingLog.datetime)

        val success = db.update(TABLE_READING_LOG, contentValues, "WHERE" + KEY_READINGLOGID + "=" + thisReadingLog.readinglog_id, null)

        db.close()

        return success
    }

    /************ Features coming soon! **********************
     *  fun updateDevoLog
     *  fun updateDevoPlan
     *  fun updatePrayerLog
     *  fun updatePrayerPlan
     *  fun updatePrayerMeeting
     *  fun updateFastingMeals
     *  fun updateFastingDetails
     */

    /* All delete functions go here. These functions should technically be "UPDATE" since it is
    * a practice to only soft delete things in the database, but for this activity's sake,
    * we will hard delete entries.
    *
    * Before calling the delete functions, there is a need to make sure that the UI is replete with
    * warnings that their details will be completely lost. */

    fun deleteUser(thisUser: UserModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_UID, thisUser.u_id)

        val success = db.delete(TABLE_USERS, KEY_UID + "=" + thisUser.u_id, null)
        db.close()
        return success
    }

    fun deleteReadingPlan(thisReadingPlan: ReadDetailsModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_READINGDETAILSID, thisReadingPlan.readingdetails_id)
        contentValues.put(KEY_UID, thisReadingPlan.u_id)

        val whereClause = "$KEY_READINGDETAILSID = ${thisReadingPlan.readingdetails_id} AND $KEY_UID = ${thisReadingPlan.u_id}"

        db.delete(TABLE_READING_LOG, whereClause, null)
        val success = db.delete(TABLE_READING_DETAILS, whereClause, null)
        db.close()
        return success
    }

    fun deleteReadingLog(thisReadingLog: ReadingLogModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_READINGLOGID, thisReadingLog.readinglog_id)
        contentValues.put(KEY_UID, thisReadingLog.u_id)

        val success = db.delete(TABLE_READING_LOG, KEY_READINGLOGID + "=" + thisReadingLog.readinglog_id + " AND " + KEY_UID + "=" + thisReadingLog.u_id, null)
        db.close()
        return success
    }

}
