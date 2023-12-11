package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_lista_user
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_projeto
import org.json.JSONException
import org.json.JSONObject

class Perfil(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    data class User(
        val userId: Int,
        val primeiroNome: String,
        val ultimoNome: String,
        val numeroFuncionario: String?,
        val email: String,
        val password: String,
        val telemovel: String,
        val morada: String?,
        val salario: String?,
        val dataRegisto: String,
        val ultimoLogin: String,
        val dataContratacao: String?,
        val isPrimeiroLogin: Boolean,
        val isAtivo: Boolean,
        val isColaborador: Boolean,
        val isCandidato: Boolean,
        val verificationToken: String?,
        val recoverToken: String?,
        val cargoId: Int,
        val departamentoId: String?,
        val filialId: String?,
        val cargo: Cargo
    )

    data class Cargo(
        val cargoId: Int,
        val cargoNome: String
    )

    fun getStoredCargoId(): Int {
        val sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("cargoId", 0)
    }

    fun getStoredIsColaborador(): Boolean {
        val sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isColaborador", false)
    }
    fun changePassword(id: Int, password: String, callback: GetUpdateCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/user/update/$id"

        val body = JSONObject()
        try {
            //body.put("userId", id)
            body.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, body,
            Response.Listener { response ->
                if (response.has("data")) {
                    val sucesso = true
                    callback.onSuccess(sucesso)
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        requestQueue.add(request)
    }

    fun listUser(callback: GetUserListaCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/user/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_users= ArrayList<imagem_lista_user>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val userId = item.getInt("userId")
                        val primeiroNome = item.getString("primeiroNome")
                        val ultimoNome = item.getString("ultimoNome")
                        val numeroFuncionario = item.optInt("numeroFuncionario")
                        val cargoId = item.getInt("cargoId")
                        val email = item.getString("email")
                        val telemovel = item.getString("telemovel")
                        val dataRegisto = item.getString("dataRegisto")
                        val morada = item.optString("morada")
                        val isColaborador = item.getBoolean("isColaborador")
                        val isCandidato = item.getBoolean("isCandidato")
                        val isAtivo = item.getBoolean("isAtivo")
                        val departamentoId = item.optInt("departamentoId")
                        val filialId = item.optInt("filialId")

                        val imgItem = imagem_lista_user()
                        imgItem.atr_Id_img_lista_user(userId)
                        imgItem.atr_Nome_lista_user(primeiroNome)
                        imgItem.atr_Sobrenome_lista_user(ultimoNome)
                        imgItem.atr_Num_Func_lista_user(numeroFuncionario)
                        imgItem.atr_Cargo_lista_user(cargoId)
                        imgItem.atr_Email_lista_user(email)
                        imgItem.atr_Telemovel_lista_user(telemovel)
                        imgItem.atr_DataRegisto_lista_user(dataRegisto)
                        imgItem.atr_Morada_lista_user(morada)
                        imgItem.atr_isColaborador_lista_user(isColaborador)
                        imgItem.atr_isCandidato_lista_user(isCandidato)
                        imgItem.atr_isAtivo_lista_user(isAtivo)
                        imgItem.atr_Departamento_lista_user(departamentoId)
                        imgItem.atr_Filial_lista_user(filialId)


                        ar_img_list_users.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_users)
                } catch (e: JSONException) {
                    val errorMessage = "User - Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }



    fun getUser(id: Int, callback: GetUserCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/user/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val user = User(
                        userId = dataObject.optInt("userId"),
                        primeiroNome = dataObject.optString("primeiroNome"),
                        ultimoNome = dataObject.optString("ultimoNome"),
                        numeroFuncionario = dataObject.optString("numeroFuncionario"),
                        email = dataObject.optString("email"),
                        password = dataObject.optString("password"),
                        telemovel = dataObject.optString("telemovel"),
                        morada = dataObject.optString("morada"),
                        salario = dataObject.optString("salario"),
                        dataRegisto = dataObject.optString("dataRegisto"),
                        ultimoLogin = dataObject.optString("ultimoLogin"),
                        dataContratacao = dataObject.optString("dataContratacao"),
                        isPrimeiroLogin = dataObject.optBoolean("isPrimeiroLogin"),
                        isAtivo = dataObject.optBoolean("isAtivo"),
                        isColaborador = dataObject.optBoolean("isColaborador"),
                        isCandidato = dataObject.optBoolean("isCandidato"),
                        verificationToken = dataObject.optString("verificationToken"),
                        recoverToken = dataObject.optString("recoverToken"),
                        cargoId = dataObject.optInt("cargoId"),
                        departamentoId = dataObject.optString("departamentoId"),
                        filialId = dataObject.optString("filialId"),
                        cargo = Cargo(
                            cargoId = dataObject.optJSONObject("cargo").optInt("cargoId"),
                            cargoNome = dataObject.optJSONObject("cargo").optString("cargoNome")
                        )
                    )
                    callback.onSuccess(user)
                } else {
                    // Lidar com a resposta JSON inválida ou ausência do campo "data"
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            { error ->
                error.printStackTrace()
                // Lidar com o erro de requisição e chamar o callback onFailure com a mensagem de erro
                val errorMessage = "O utilizador não existe!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun updateUser(user: User, callback: GetUserCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/user/update/${user.userId}"

        val body = JSONObject()
        try {
            body.put("primeiroNome", user.primeiroNome)
            body.put("ultimoNome", user.ultimoNome)
            body.put("numeroFuncionario", user.numeroFuncionario)
            body.put("email", user.email)
            body.put("password", user.password)
            body.put("telemovel", user.telemovel)
            body.put("morada", user.morada)
            body.put("salario", user.salario)
            body.put("dataRegisto", user.dataRegisto)
            body.put("ultimoLogin", user.ultimoLogin)
            body.put("dataContratacao", user.dataContratacao)
            body.put("isPrimeiroLogin", user.isPrimeiroLogin)
            body.put("isAtivo", user.isAtivo)
            body.put("isColaborador", user.isColaborador)
            body.put("isCandidato", user.isCandidato)
            body.put("verificationToken", user.verificationToken)
            body.put("recoverToken", user.recoverToken)
            body.put("cargoId", user.cargoId)
            body.put("departamentoId", user.departamentoId)
            body.put("filialId", user.filialId)

            val cargoObject = JSONObject()
            cargoObject.put("cargoId", user.cargo?.cargoId )
            cargoObject.put("cargoNome", user.cargo?.cargoNome)
            body.put("cargo", cargoObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, body,
            Response.Listener { response ->
                // Check if the response JSON contains the field "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val user = User(
                        userId = dataObject.optInt("userId"),
                        primeiroNome = dataObject.optString("primeiroNome"),
                        ultimoNome = dataObject.optString("ultimoNome"),
                        numeroFuncionario = dataObject.optString("numeroFuncionario"),
                        email = dataObject.optString("email"),
                        password = dataObject.optString("password"),
                        telemovel = dataObject.optString("telemovel"),
                        morada = dataObject.optString("morada"),
                        salario = dataObject.optString("salario"),
                        dataRegisto = dataObject.optString("dataRegisto"),
                        ultimoLogin = dataObject.optString("ultimoLogin"),
                        dataContratacao = dataObject.optString("dataContratacao"),
                        isPrimeiroLogin = dataObject.optBoolean("isPrimeiroLogin"),
                        isAtivo = dataObject.optBoolean("isAtivo"),
                        isColaborador = dataObject.optBoolean("isColaborador"),
                        isCandidato = dataObject.optBoolean("isCandidato"),
                        verificationToken = dataObject.optString("verificationToken"),
                        recoverToken = dataObject.optString("recoverToken"),
                        cargoId = dataObject.optInt("cargoId"),
                        departamentoId = dataObject.optString("departamentoId"),
                        filialId = dataObject.optString("filialId"),
                        cargo = Cargo(
                            cargoId = dataObject.optJSONObject("cargo").optInt("cargoId"),
                            cargoNome = dataObject.optJSONObject("cargo").optString("cargoNome")
                        )
                    )
                    callback.onSuccess(user)
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "Erro a atualizar utilizador!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun updateUserPerfil(userId: Int, email: String?, telemovel: String?, morada: String?, primeiroNome: String?, ultimoNome: String?, callback: GetUpdateCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/user/update/${userId}"

        val body = JSONObject()
        try {
            body.put("primeiroNome", primeiroNome)
            body.put("ultimoNome", ultimoNome)
            body.put("email", email)
            body.put("telemovel", telemovel)
            body.put("morada", morada)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, body,
            Response.Listener { response ->
                // Check if the response JSON contains the field "data"
                if (response.has("data")) {
                    val sucesso = true
                    callback.onSuccess(sucesso)
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "Erro a atualizar utilizador!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun deleteUser(userId: Int, callback: GetUpdateCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/user/delete"

        val body = JSONObject()
        try {
            body.put("userId", userId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            Response.Listener { response ->
                // Check if the response JSON contains the field "data"
                if (response.has("data")) {
                    val sucesso = true
                    callback.onSuccess(sucesso)
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "Erro a atualizar utilizador!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    interface GetUserCallback {
        fun onSuccess(user: User)
        fun onFailure(errorMessage: String)
    }

    interface GetUserListaCallback {
        fun onSuccess(users: List<imagem_lista_user>)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}