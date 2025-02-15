package com.example.fitnesstrackingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONException


class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnRegister = findViewById(R.id.btn_register)
        tvLogin = findViewById(R.id.tv_login)

        // Handle register button click
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(username, email, password)
            }
        }

        // Handle login text click
        tvLogin.setOnClickListener {
            // Redirect to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(username: String, email: String, password: String) {

        val url = "http://10.0.2.2/fitness_app/register.php"

        // Create a StringRequest using Volley
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    // Parse the response from the server
                    val jsonResponse = JSONObject(response)

                    if (jsonResponse.getBoolean("success")) {
                        // Registration successful
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        // Redirect to login or home screen
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Registration failed
                        val errorMessage = jsonResponse.getString("message")
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Handle the error
                Toast.makeText(this, "Volley Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            // Pass the data to the PHP script in the form of POST parameters
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(stringRequest)
    }
}
