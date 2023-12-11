package com.example.projeto_softinsa_mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ForgotPass : AppCompatActivity() {

    private lateinit var emailChangeEditText: EditText
    private lateinit var resetButton: Button
    private lateinit var signinLink: Button
    private lateinit var signupLink: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        emailChangeEditText = findViewById(R.id.email_change)
        resetButton = findViewById(R.id.resetbtn)
        signinLink = findViewById(R.id.signinLink)
        signupLink = findViewById(R.id.signupLink)
        resetButton.setOnClickListener {
            val email = emailChangeEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                performResetRequest(email)
            } else {
                Toast.makeText(this, "Por favor, insira o email", Toast.LENGTH_SHORT).show()
            }
        }

        signinLink.setOnClickListener {
            val intentSignIn = Intent(this, MainActivity::class.java)
            startActivity(intentSignIn)
        }

        signupLink.setOnClickListener {
            val intentSignUp = Intent(this, SignupActivity::class.java)
            startActivity(intentSignUp)
        }

    }

    private fun performResetRequest(email: String) {
        val urlString = "https://softinsa-web-app-carreiras01.onrender.com/auth/forgot-password" // Substitua pela URL da sua API

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val requestBody = "{\"email\": \"$email\"}"

                val outputStream = OutputStreamWriter(connection.outputStream)
                outputStream.write(requestBody)
                outputStream.flush()
                outputStream.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leitura da resposta da API, se necessário
                    val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    bufferedReader.close()

                    runOnUiThread {
                        Toast.makeText(this@ForgotPass, "Token de recuperação criado com sucesso", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ForgotPass, "Erro na requisição: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@ForgotPass, "Erro ao efetuar o HTTP request", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}