package com.example.restmysoul
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restmysoul.database.DatabaseHandler
import com.example.restmysoul.database.UserModelClass

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.btn_userLogin)
        val createAccount: TextView = findViewById(R.id.link_create_account)

        loginButton.setOnClickListener { view -> loginUser(view) }
        createAccount.setOnClickListener { signUp() }
    }

    private fun loginUser(view: View) {
        val userDetails = ArrayList<UserModelClass>()
        val etemail: EditText = findViewById(R.id.login_email)
        val etpasswd: EditText = findViewById(R.id.login_password)

        val email = etemail.text.toString()
        val password = etpasswd.text.toString()
        val databaseHandler = DatabaseHandler(this)

        //Error check: see if email and password exists
        /**** NOT WORKING!!! ****/
        if (!email.isEmpty() && !password.isEmpty()) {


            val status = databaseHandler.loginThisUser(email, password)

            if(!status){
                Toast.makeText(this, "Email and password does not exist. Create an account!", Toast.LENGTH_LONG)
                    .show()
            } else {
                val intentMainActivity = Intent(this, MainActivity::class.java)
                startActivity(intentMainActivity)
                this.finish()
            }

        } else {
            Toast.makeText(this, "Enter your registered email and password.", Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun signUp() {
        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
        startActivity(intent)
    }
}






