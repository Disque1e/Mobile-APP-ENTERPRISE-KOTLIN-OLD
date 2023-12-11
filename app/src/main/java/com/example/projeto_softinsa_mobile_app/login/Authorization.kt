package com.example.projeto_softinsa_mobile_app.login


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class Authorization(private val context: Context, private val editor: SharedPreferences.Editor?) {
    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private var isServerPrimeiroLogin: Boolean = false

    interface LoginCallback {
        fun onSuccess(token: String)
        fun onFailure(errorMessage: String)
    }

    interface ChangePasswordCallback {
        fun onSuccess()
        fun onFailure(errorMessage: String)
    }

    fun login(email: String, password: String, isPrimeiroLogin: Boolean, callback: LoginCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/auth/login" // Substitua pela sua URL de API para login

        val body = JSONObject()
        try {
            body.put("email", email)
            body.put("password", password)
            body.put("isPrimeiroLogin", isPrimeiroLogin)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(Request.Method.POST, url, body,
            Response.Listener { response ->
                val token = response.optString("accessToken")
                val userId = response.optInt("userId")
                isServerPrimeiroLogin = response.optBoolean("isPrimeiroLogin")
                // Imprimir a resposta JSON para solução de problemas
                Log.d("Authorization", "Response JSON: $response")
                Log.d("Authorization", "isServerPrimeiroLogin: $isServerPrimeiroLogin")

                if (token.isNotEmpty()) {
                    editor?.putString("token", token)
                    editor?.putInt("userId", userId)
                    editor?.putBoolean("isPrimeiroLogin", isPrimeiroLogin)
                    Log.d("tag", userId.toString())
                    editor?.apply()

                    callback.onSuccess(token)
                } else {
                    val errorMessage = "Failed to login. Token not found."
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val errorMessage = "Failed to login. Please check your credentials."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun getUserId(): Int {
        // Retrieve the userId from SharedPreferences
        return context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
            .getInt("userId", 0) // 0 is the default value if userId is not found
    }
    fun getIsServerPrimeiroLogin(): Boolean {
        return isServerPrimeiroLogin
    }
    fun registar(
        primeiroNome: String,
        ultimoNome: String,
        telemovel: String,
        email: String,
        password: String,
        isPrimeiroLogin: Boolean,
        callback: LoginCallback
    ) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/auth/signup" // Substitua pela URL da sua API para registro

        val body = JSONObject()
        try {
            body.put("primeiroNome", primeiroNome)
            body.put("ultimoNome", ultimoNome)
            body.put("telemovel", telemovel)
            body.put("email", email)
            body.put("password", password)
            body.put("isPrimeiroLogin", isPrimeiroLogin)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(Request.Method.POST, url, body,
            Response.Listener { response ->
                // Print the response JSON for troubleshooting
                Log.d("Authorization", "Response JSON: $response")

                // Verifique se o registro foi bem-sucedido com base na resposta do servidor
                val success = response.optBoolean("success")
                if (success) {
                    callback.onSuccess("Registration successful")
                } else {
                    val errorMessage = response.optString("message", "Failed to sign up.")
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Lida com o erro de registro e chama o onFailure callback com a mensagem de erro
                val errorMessage = "Failed to sign up."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    // Função getUserDetail() removida, pois não parece estar relacionada às colunas desejadas.

    fun ChangePassword(userId: Int, email: String, newPassword: String, isPrimeiroLogin: Boolean, callback: ChangePasswordCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/auth/primeiroLogin/$userId"

        val body = JSONObject()
        try {
            body.put("email", email)
            body.put("password", newPassword)
            body.put("isPrimeiroLogin", isPrimeiroLogin)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.POST, // Altere para POST
            url,
            body,
            Response.Listener { response ->
                Log.d("ChangePassword", "Response JSON: $response")
                // Atualize o valor de isPrimeiroLogin nas SharedPreferences
                val sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isPrimeiroLogin", isPrimeiroLogin)
                editor.apply()
                Log.d("Debug", "isPrimeiroLogin atualizado para: " + isPrimeiroLogin);
                callback.onSuccess()
                Log.d("Debug", "isPrimeiroLogin atualizado para1: " + isPrimeiroLogin);
                editor.apply()
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val errorMessage = "Erro de conexão"
                callback.onFailure(errorMessage)
            }
        )

        requestQueue.add(request)
    }
}