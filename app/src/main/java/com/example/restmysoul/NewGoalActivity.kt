package com.example.restmysoul

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.restmysoul.database.DatabaseHandler
import com.example.restmysoul.database.ReadDetailsModelClass
import com.example.restmysoul.ui.goals.GoalsViewModel

class NewGoalActivity : AppCompatActivity() {
    lateinit var selectedGoal: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_goal)

        val addGoalbtn: Button = findViewById(R.id.addGoalbtn)
        addGoalbtn.setOnClickListener { view -> addGoal(view)}

    }

    private fun addGoal(view: View) {
        val etNumDays: EditText = findViewById(R.id.editText_numDays)
        val etNumChapters: EditText = findViewById(R.id.editText_numChaptersADay)
        val databaseHandler = DatabaseHandler(this)
        val numdays : String = etNumDays.text.toString()
        val numchapters : String = etNumChapters.text.toString()

        val userId = databaseHandler.getUserId() /** WE HAVE A PROBLEM HERE! **/


        if(selectedGoal.isEmpty() || etNumDays.text.toString().isEmpty() || etNumChapters.text.toString().isEmpty()) {
            Toast.makeText(this,"Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
        else {
            if(selectedGoal == "Bible Reading") {
                val status = databaseHandler.createReadingGoal(
                    ReadDetailsModelClass(
                        0,
                        u_id = userId,
                        numchapters = numchapters.toInt(),
                        numdays = numdays.toInt()
                    )
                )

                if(status < 0)
                    Toast.makeText(this,"An error occured. Contact support.", Toast.LENGTH_LONG).show()
                else {
                    Toast.makeText(this,"Goal added successfully.", Toast.LENGTH_LONG).show()
                    /***NOTE: Should go back to MainActivity->GoalsFragment instead sa Home lang. ***/
                    val intentMainActivity = Intent(this, MainActivity::class.java)
                    startActivity(intentMainActivity)
                    this.finish()
                }

            } else {
                Toast.makeText(this, "This feature is coming soon.", Toast.LENGTH_LONG).show()
            }


        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_biblereading ->
                    if (checked) {
                        selectedGoal = "Bible Reading"
                    }
                R.id.radio_devotion ->
                    if (checked) {
                        selectedGoal = "Devotion"
                    }
                R.id.radio_prayer ->
                    if (checked) {
                        selectedGoal = "Prayer"
                    }
                R.id.radio_fasting ->
                    if (checked) {
                        selectedGoal = "Fasting "
                    }
            }
        }
    }
}