package uz.abboskhan.cashuc

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.abboskhan.cashuc.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var pb: Dialog
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth

        binding.tvLoginPage.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        binding.btnSignUp.setOnClickListener { registerUser() }

    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

        startActivity(Intent(this,SignInActivity::class.java))
        finish()
    }

    private fun registerUser() {
        val name = binding.etSinUpName.text.toString()
        val email = binding.etSinUpEmail.text.toString()
        val password = binding.etSinUpPassword.text.toString()
        if (validateForm(name, email, password)) {
            showProgressBar()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this,"Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else {
                        Toast.makeText(this,"Oops! Something went wrong", Toast.LENGTH_SHORT).show()
                    }

                    hideProgressBar()
                }
        }
    }
    private fun validateForm(name:String, email:String,password:String):Boolean {
        return when {
            TextUtils.isEmpty(name)->{
                binding.tilName.error = "Enter name"
                false
            }
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding.tilEmail.error = "Enter valid email address"
                false
            }
            TextUtils.isEmpty(password)->{
                binding.tilPassword.error = "Enter password"
                false
            }
            else -> { true }
        }
    }
    private fun showProgressBar() {
        pb = Dialog(this)
        pb.setContentView(R.layout.loading_prgbar)
        pb.setCancelable(false)
        pb.show()
    }

    private fun hideProgressBar() {
        pb.dismiss()
    }

}