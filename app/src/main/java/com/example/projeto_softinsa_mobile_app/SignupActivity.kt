package com.example.projeto_softinsa_mobile_app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projeto_softinsa_mobile_app.login.Authorization


class SignupActivity : AppCompatActivity() {
    private lateinit var signupButton: Button
    private lateinit var signinLink: TextView
    private lateinit var authorization: Authorization
    private lateinit var passwordEditText: EditText
    private lateinit var passwordEditText_confirm: EditText
    private lateinit var show_1: ImageView
    private lateinit var show_2: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupButton = findViewById(R.id.signupbtn)
        signinLink = findViewById(R.id.signinLink)
        authorization = Authorization(this, getSharedPreferences("YourPrefs", MODE_PRIVATE).edit())
        passwordEditText = findViewById(R.id.password)
        passwordEditText_confirm = findViewById(R.id.confirmpassword)
        show_1 = findViewById(R.id.show_pass_1)
        show_2 = findViewById(R.id.show_pass_2)

        signupButton.setOnClickListener {
            internetConnection()
        }

        signinLink.setOnClickListener{
            val signinIntent = Intent(this@SignupActivity, MainActivity::class.java)
            startActivity(signinIntent)
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
    // Função para exibir um Toast
    private fun showToast(message: String) {
        Toast.makeText(this@SignupActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    private fun internetConnection() {
        if (!checkForInternet(this)) {
            val positiveButtonClick = { dialog: DialogInterface, _: Int ->
                internetConnection()
                dialog.dismiss()
            }

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Erro de conexão à Internet")
            alertDialogBuilder.setMessage("Não está conectado à internet!\nTente novamente quando estiver conectado")
            alertDialogBuilder.setPositiveButton(
                "Ok",
                DialogInterface.OnClickListener(positiveButtonClick)
            )
            alertDialogBuilder.show()
        } else {
            val firstName = findViewById<EditText>(R.id.fname).text.toString()
            val lastName = findViewById<EditText>(R.id.lname).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val phone = findViewById<EditText>(R.id.telefone).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confpassword= findViewById<EditText>(R.id.confirmpassword).text.toString()
            val isPrimeiroLogin = true

            // Check if all fields are non-empty
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Check if password meets the required conditions
                if (password.matches(Regex(".*[A-Z].*")) && password.matches(Regex(".*\\d.*")) && password.matches(Regex(".*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"))) {
                    // Check if password and confpassword are equal
                    if (password == confpassword) {
                        authorization.registar(firstName, lastName, phone, email, password, isPrimeiroLogin, object : Authorization.LoginCallback {
                            override fun onSuccess(token: String) {
                                showToast("Registo efetuado com sucesso, por favor verifique o email.")
                                // Aqui você pode redirecionar o usuário para a próxima tela, por exemplo.
                            }

                            override fun onFailure(errorMessage: String) {
                                showToast("Registration failed: $errorMessage")
                            }
                        })
                    } else {
                        Toast.makeText(this, "As senhas não correspondem. Por favor, insira senhas iguais nos campos 'Password' e 'Confirm Password'.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "A senha deve conter pelo menos um caractere maiúsculo e um número.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, insira todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}