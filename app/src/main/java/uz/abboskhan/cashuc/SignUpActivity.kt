package uz.abboskhan.cashuc

import android.annotation.SuppressLint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import uz.abboskhan.cashuc.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("Users")
    var name = ""
    var email = ""
    var password = ""
    var referCode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = binding.etSinUpName.text.toString()
        email = binding.etSinUpEmail.text.toString()
        password = binding.etSinUpPassword.text.toString()
        referCode = binding.edtReferCode.text.toString()

        auth = Firebase.auth

        binding.tvLoginPage.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.btnSignUp.setOnClickListener { registerUser() }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }



    private fun checkNameAvailability() {


        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Check for the child node with the entered name
                    val userExists = dataSnapshot.child(name).exists()

                    if (userExists) {
                        // Name is already registered, notify the user
                        Toast.makeText(
                            this@SignUpActivity,
                            "Name already registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Name is not registered, proceed with registration
                        registerUserData()
                    }
                } else {
                    // No users registered yet, proceed with registration
                    registerUserData()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Log.w("TAG", "Failed to read value")
            }
        })
    }

    private fun registerUserData() {

        val timestamp = System.currentTimeMillis()
        val id = "$timestamp"
        checkNameAvailability()
        val referEmail = email.substring(0, email.lastIndexOf("@"))
        val userReferCode = referEmail.replace(".", "")

        // Create a Firebase authentication object
        val auth = FirebaseAuth.getInstance()

        // Create the Firebase authentication task
        auth.createUserWithEmailAndPassword(email, password)

            // Add a listener to the Firebase authentication task
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Show a success message
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Create a Firebase Realtime Database reference
                    val usersRef = FirebaseDatabase.getInstance().getReference("users")


                    // Create a UserData object
                    val user = UserData(id, timestamp, name, email, password, referCode, 100, 5)

                    // Save the user data to the Firebase Realtime Database
                    usersRef.child(name).setValue(user)

                    // Navigate to the main activity
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))

                    // Finish the current activity
                    finish()
                } else {
                    // Show an error message
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()

                    // Dismiss the progress bar
                    // pb.dismissProgressBar()
                }
            }

            // Add a listener to handle failures
            .addOnFailureListener {
                // Show an error message
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()

                // Dismiss the progress bar
                //  pb.dismissProgressBar()
            }

    }


    private fun registerUser() {
        // Create a progress bar object
      //  pb.startDialog()

        // Get the user input
        val name = binding.etSinUpName.text.toString()
        val email = binding.etSinUpEmail.text.toString()
        val password = binding.etSinUpPassword.text.toString()
        referCode = binding.edtReferCode.text.toString()

        // Validate the user input
        if (validateForm(name, email, password)) {
            // Check if the name is already registered
            val usersRef = FirebaseDatabase.getInstance().getReference("Users")

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Check for the child node with the entered name
                        val userExists = dataSnapshot.child(name).exists()

                        if (userExists) {
                            // Name is already registered, notify the user
                            Toast.makeText(
                                this@SignUpActivity,
                                "Name already registered",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Name is not registered, proceed with registration
                            registerUserData()
                        }
                    } else {
                        // No users registered yet, proceed with registration
                        registerUserData()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Log.w("TAG", "Failed to read value")
                }
            })

        } else {
            // Display error messages
            // ...
        }
    }


    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                binding.tilName.error = "Enter name"
                false
            }

            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Enter valid email address"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding.tilPassword.error = "Enter password"
                false
            }

            else -> {
                true
            }
        }
    }


}