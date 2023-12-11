package com.example.projeto_softinsa_mobile_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projeto_softinsa_mobile_app.login.Authorization

class ResetPassword : AppCompatActivity() {
    private lateinit var resetbtn: Button
    private lateinit var signinLink: Button
    private lateinit var signupLink: Button
    private lateinit var passwordEditText: EditText
    private lateinit var passwordEditText_confirm: EditText
    private lateinit var show_1: ImageView
    private lateinit var show_2: ImageView
    private lateinit var email: EditText
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        resetbtn = findViewById(R.id.resetbtn)
        signinLink = findViewById(R.id.signinLink)
        signupLink = findViewById(R.id.signupLink)
        passwordEditText = findViewById(R.id.password_change)
        passwordEditText_confirm = findViewById(R.id.confirmpassword_change)
        show_1 = findViewById(R.id.show_pass_1)
        show_2 = findViewById(R.id.show_pass_2)
        email = findViewById<EditText>(R.id.email_change)

        userId = intent.getIntExtra("userId", 0) // Obter o userId do Intent

        resetbtn.setOnClickListener {
            showConfirmationDialog()
        }

        signinLink.setOnClickListener {
            val signinIntent = Intent(this@ResetPassword, MainActivity::class.java)
            startActivity(signinIntent)
        }

        signupLink.setOnClickListener {
            val signupIntent = Intent(this@ResetPassword, SignupActivity::class.java)
            startActivity(signupIntent)
        }
        show_1.setOnClickListener{
            val currentInputType = passwordEditText.inputType

            if (currentInputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // se a pass estiver visivel, muda para o modo normal
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                show_1.setImageResource(R.drawable.baseline_see_pass)
            } else {
                // If the password is currently hidden, switch to visible password mode
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                show_1.setImageResource(R.drawable.baseline_see_pass)
            }

            // Move the cursor to the end of the text after changing input type
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        show_2.setOnClickListener{
            val currentInputType = passwordEditText_confirm.inputType

            if (currentInputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // se a pass estiver visivel, muda para o modo normal
                passwordEditText_confirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                show_2.setImageResource(R.drawable.baseline_see_pass)
            } else {
                // If the password is currently hidden, switch to visible password mode
                passwordEditText_confirm.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                show_2.setImageResource(R.drawable.baseline_see_pass)
            }

            // Move the cursor to the end of the text after changing input type
            passwordEditText_confirm.setSelection(passwordEditText_confirm.text.length)
        }
    }
    private fun showConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val authorization = Authorization(this, editor)

        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("Are you sure you want to change your password? This action cannot be undone!")

        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            val userEmail = email.text.toString() // Get the user's email
            val newPassword = passwordEditText.text.toString()
            val newPasswordConfirm = passwordEditText_confirm.text.toString()

            if (userEmail.isNotEmpty() && newPassword == newPasswordConfirm) {
                val isPrimeiroLogin = false

                authorization.ChangePassword(userId, userEmail, newPassword, isPrimeiroLogin, object :
                    Authorization.ChangePasswordCallback {
                    override fun onSuccess() {
                        // Password reset successfully, and the value of isPrimeiroLogin has been updated in ChangePassword

                        // Update the value of isPrimeiroLogin in SharedPreferences
                        editor.putBoolean("isPrimeiroLogin", isPrimeiroLogin)
                        editor.apply()

                        Toast.makeText(this@ResetPassword, "Password changed successfully!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(applicationContext, WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        Log.d("Debug", "isPrimeiroLogin atualizado para2: " + isPrimeiroLogin);

                        startActivity(intent)
                        Log.d("Debug", "isPrimeiroLogin atualizado para3: " + isPrimeiroLogin);

                    }

                    override fun onFailure(errorMessage: String) {
                        Toast.makeText(this@ResetPassword, "Error changing password: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@ResetPassword, "Please enter the user's email and ensure password confirmation matches.", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }

        alertDialogBuilder.show()
    }
}