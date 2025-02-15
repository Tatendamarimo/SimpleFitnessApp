package com.example.fitnesstrackingapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONException
import org.json.JSONObject
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TrackingActivity : AppCompatActivity() {

    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var exerciseList: MutableList<Exercise>
    private lateinit var fabAddExercise: FloatingActionButton
    private lateinit var btnAddExercise: Button
    private lateinit var editTextExerciseType: EditText
    private lateinit var editTextDuration: EditText
    private lateinit var editTextDistance: EditText
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        // Initialize views
        exerciseRecyclerView = findViewById(R.id.recyclerViewExercises)
        fabAddExercise = findViewById(R.id.fabAddExercise)
        btnAddExercise = findViewById(R.id.btnAddExercise)
        editTextExerciseType = findViewById(R.id.editTextExerciseType)
        editTextDuration = findViewById(R.id.editTextDuration)
        editTextDistance = findViewById(R.id.editTextDistance)
        locationText = findViewById(R.id.textLocation)

        // Initialize exercise list and adapter
        exerciseList = mutableListOf()
        exerciseAdapter = ExerciseAdapter(exerciseList)

        // Set up RecyclerView
        exerciseRecyclerView.adapter = exerciseAdapter
        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the FloatingActionButton and Button click listeners
        fabAddExercise.setOnClickListener {
            addExercise()
        }

        btnAddExercise.setOnClickListener {
            addExercise()
        }

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getUserLocation()
    }

    private fun addExercise() {
        val exerciseType = editTextExerciseType.text.toString()
        val duration = editTextDuration.text.toString()
        val distance = editTextDistance.text.toString()

        // Validate input
        if (exerciseType.isBlank() || duration.isBlank() || distance.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve userID from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userID = sharedPreferences.getString("userID", null)

        // Check if userID exists
        if (userID == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Create new Exercise object
        val newExercise = Exercise(exerciseType, duration, distance)

        // Add the exercise to the list and notify the adapter
        exerciseList.add(newExercise)
        exerciseAdapter.notifyItemInserted(exerciseList.size - 1)

        // Clear input fields
        editTextExerciseType.text.clear()
        editTextDuration.text.clear()
        editTextDistance.text.clear()

        // Send data to PHP server
        sendExerciseToServer(newExercise, userID)
    }

    private fun sendExerciseToServer(exercise: Exercise, userID: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2/fitness_app/log_exercise.php"

        // Create a string request
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                // Handle response from the server
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getString("status") == "success") {
                        Toast.makeText(this, "Exercise logged successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error logging exercise: ${jsonResponse.getString("message")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Response error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Volley error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                // Passing parameters to the PHP script
                val params = HashMap<String, String>()
                params["exerciseType"] = exercise.exerciseType
                params["duration"] = exercise.duration
                params["distance"] = exercise.distance
                params["userID"] = userID
                return params
            }
        }

        // Add the request to the RequestQueue
        queue.add(stringRequest)
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // Get last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    locationText.text = "Location: $lat, $lon"
                } else {
                    locationText.text = "Location not available"
                }
            }
        }
    }
}
