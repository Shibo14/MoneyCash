package uz.abboskhan.cashuc

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
import uz.abboskhan.cashuc.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var pb: Dialog
    lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnSignIn.setOnClickListener {
            userLogin()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

    }
    private fun userLogin() {
        val email = binding.etSinInEmail.text.toString()
        val password = binding.etSinInPassword.text.toString()
        if (validateForm(email, password)) {
            showProgressBar()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        binding.btnSignIn.text = "Login"
                        Toast.makeText(this, "Oops! Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                    hideProgressBar()
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Enter valid email address"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding.tilPassword.error = "Enter password"
                binding.tilEmail.error = null
                false
            }

            else -> {
                binding.tilEmail.error = null
                binding.tilPassword.error = null
                true
            }
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

    override fun onStart() {
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onStart()
    }

}