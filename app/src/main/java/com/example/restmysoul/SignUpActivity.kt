package com.example.restmysoul

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restmysoul.database.DatabaseHandler
import com.example.restmysoul.database.UserModelClass

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signUpButton: Button = findViewById(R.id.btn_signUp)
        signUpButton.setOnClickListener { view -> signUpUser(view) }
    }

    private fun signUpUser (view: View) {
        val etFullName: EditText = findViewById(R.id.signup_fullName)
        val etEmail: EditText = findViewById(R.id.signup_email)
        val etReligion: EditText = findViewById(R.id.signup_religion)
        val etChurch: EditText = findViewById(R.id.signup_church)
        val etPass: EditText = findViewById(R.id.signup_password)
        val etConfirmPassword: EditText = findViewById(R.id.signup_confirmPassword)

        val fullName = etFullName.text.toString()
        val email = etEmail.text.toString()
        val religion = etReligion.text.toString()
        val church = etChurch.text.toString()
        val password = etPass.text.toString()
        val confPass = etConfirmPassword.text.toString()
        val databaseHandler = DatabaseHandler(this)



        if (fullName.isEmpty() || email.isEmpty() || religion.isEmpty() || church.isEmpty() || password.isEmpty() || confPass.isEmpty()){
            Toast.makeText(this,"Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
        // this checks the database if the email address is already used.
        /************ NOT WORKING! ******************/
        else if(databaseHandler.checkIfEmailExists(email)) {
            Toast.makeText(this,"This email address is already used.", Toast.LENGTH_LONG).show()
        } else if (password == confPass) {
            databaseHandler.addUser(
                UserModelClass(
                    fullName = fullName,
                    email = email,
                    religion = religion,
                    church = church,
                    password = password
                )
            )
            val intentLoginActivity = Intent(this, LoginActivity::class.java)
            startActivity(intentLoginActivity)

            this.finish()

        } else Toast.makeText(this,"Password does not match. Try again.", Toast.LENGTH_LONG).show()
    }
}

/***************
 * if (etPassword.text.toString() == etConfirmPassword.text.toString()) {
databaseHandler.addUser(EmpModelClass(name = editTextUsername.text.toString(),
password = editTextPassword.text.toString()))
val intentLoginActivity = Intent(this, LoginActivity::class.java)
startActivity(intentLoginActivity)

this.finish()

} else Toast.makeText(this,"Your password doesn't match", Toast.LENGTH_LONG).show()

 */