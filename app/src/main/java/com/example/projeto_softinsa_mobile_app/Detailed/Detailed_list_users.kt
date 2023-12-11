package com.example.projeto_softinsa_mobile_app.Detailed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.ListUser.Edit_ListUser
import com.example.projeto_softinsa_mobile_app.ListUser.MainListUsers
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject

data class Filial(
    val filialId: Int,
    val filialNome: String,
    val morada: String,
    val telemovel: String,
    val email: String,
)

data class Departamento(
    val departamentoId: Int,
    val departamentoNome: String,
    val dataCriacao: String,
    val descricao: String,
)

data class Cargo(
    val cargoId: Int,
    val cargoNome: String,
)
private lateinit var url: String

class Detailed_list_users : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_list_users)

        val texto1 = findViewById<TextView>(R.id.detailed_Lista_user_Nome)
        val texto2 = findViewById<TextView>(R.id.detailed_Lista_user_Sobrenome)
        val texto3 = findViewById<TextView>(R.id.detailed_Lista_user_Num_Func)
        val texto4 = findViewById<TextView>(R.id.detailed_Lista_user_Email)
        val texto5 = findViewById<TextView>(R.id.detailed_Lista_user_Telemovel)
        val texto6 = findViewById<TextView>(R.id.detailed_Lista_user_Estado)
        val texto7 = findViewById<TextView>(R.id.detailed_Lista_user_Cargo)
        val texto8 = findViewById<TextView>(R.id.detailed_Lista_user_Departamento)
        val texto9 = findViewById<TextView>(R.id.detailed_Lista_user_Filial)
        val texto10 = findViewById<TextView>(R.id.detailed_Lista_user_Morada)
        val texto11 = findViewById<TextView>(R.id.detailed_Lista_user_Id)

        val userId = intent.extras?.get("userId")
        val givenName = intent.extras?.get("Lista_user_Nome")
        val givenSubName = intent.extras?.get("Lista_user_Sobrenome")
        val givenNumFunctionario = intent.extras?.get("Lista_user_Num_Func")
        val givenEmail = intent.extras?.get("Lista_user_Email")
        val givenTelemovel = intent.extras?.get("Lista_user_Telemovel")
        val givenEstado = intent.extras?.get("Lista_user_Estado")
        val givenisCargo = intent.extras?.get("Lista_user_Cargo")
        val givenisDepartamento = intent.extras?.get("Lista_user_Departamento")
        val givenisFilial = intent.extras?.get("Lista_user_Filial")
        val givenisMorada = intent.extras?.get("Lista_user_Morada")

        texto1.text = givenName.toString()
        texto2.text = givenSubName.toString()
        texto3.text = givenNumFunctionario.toString()
        texto4.text = givenEmail.toString()
        texto5.text = givenTelemovel.toString()
        texto6.text = givenEstado.toString()
        //texto7.text = givenCargo.toString()
        texto10.text = givenisMorada.toString()
        texto11.text =userId.toString()
        requestQueue = Volley.newRequestQueue(this)

        val filialId = givenisFilial.toString()
        val DepartamentoId = givenisDepartamento.toString()
        val cargoId = givenisCargo.toString()

        Log.d("tag", "$filialId")
        if (filialId != null) {
            getFilial(object : GetFilialCallback {
                override fun onSuccess(filial: Filial) {
                    Log.d("tag", "${filial.filialNome}")
                    texto9?.text = filial.filialNome
                }

                override fun onFailure(errorMessage: String) {
                    //holder.tvFilialId?.text = "Error: $errorMessage"
                }
            }, filialId.toInt())
        }

        Log.d("tag", "$DepartamentoId")
        if (DepartamentoId != null) {
            getDepartamento(object : GetDepartamentoCallback {
                override fun onSuccess(departamento: Departamento) {
                    Log.d("tag", "${departamento.departamentoNome}")
                    texto8?.text = departamento.departamentoNome
                }

                override fun onFailure(errorMessage: String) {
                    //holder.tvFilialId?.text = "Error: $errorMessage"
                }
            }, DepartamentoId.toInt())
        }

        Log.d("tag", "$cargoId")
        if (cargoId != null) {
            listCargo(object : GetCargosCallback {
                override fun onSuccess(cargos: ArrayList<Cargo>) {
                    var cargoName: String? = null

                    for (cargo in cargos) {
                        if (cargo.cargoId == cargoId.toInt()) {
                            cargoName = cargo.cargoNome
                            break
                        }
                    }
                        texto7?.text = cargoName
                }

                override fun onFailure(errorMessage: String) {
                    //holder.tvFilialId?.text = "Error: $errorMessage"
                }
            })
        }

        // Initialize requestQueue after other variables have been initialized
        var drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)

        /*-----------------------------NAVGATION MENU BAR-----------------------------*/
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val menuBtn = findViewById<ImageButton>(R.id.menu_btn)
        menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.nav_home ->{
                    val intent1 = Intent(applicationContext, MainPage::class.java)
                    startActivity(intent1)
                }
                R.id.nav_oportunities ->{
                    val intent2 = Intent(applicationContext, MainOportunidades::class.java)
                    startActivity(intent2)
                }
                R.id.nav_ideas ->{
                    val intent3 = Intent(applicationContext, MainIdeia::class.java)
                    startActivity(intent3)
                }
                R.id.nav_job_offers ->{
                    val intent4 = Intent(applicationContext, MainOferta_Vaga::class.java)
                    startActivity(intent4)
                }
                R.id.nav_benefits ->{
                    val intent5 = Intent(applicationContext, MainBeneficio::class.java)
                    intent5.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent5)
                }
                R.id.nav_list_users ->{
                    val intent7 = Intent(applicationContext, MainListUsers::class.java)
                    startActivity(intent7)
                }
                R.id.nav_profile ->{
                    Toast.makeText(applicationContext, "Clicked profile", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_logout ->{
                    val intent6 = Intent(applicationContext, MainActivity::class.java)
                    //nao deixar voltar atras :3
                    intent6.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    Toast.makeText(applicationContext, "Login Out", Toast.LENGTH_SHORT).show()
                    startActivity(intent6)
                    finish() // Encerra a atividade atual
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val editarButton = findViewById<Button>(R.id.Editar)
        editarButton.setOnClickListener {
            val intent = Intent(this, Edit_ListUser::class.java)
            intent.putExtra("userId", userId.toString())
            intent.putExtra("Lista_user_Nome", givenName.toString())
            intent.putExtra("Lista_user_Sobrenome", givenSubName.toString())
            intent.putExtra("Lista_user_Num_Func", givenNumFunctionario.toString())
            intent.putExtra("Lista_user_Email", givenEmail.toString())
            intent.putExtra("Lista_user_Telemovel", givenTelemovel.toString())
            intent.putExtra("Lista_user_Estado", givenEstado.toString())
            intent.putExtra("Lista_user_Cargo", givenisCargo.toString())
            intent.putExtra("Lista_user_Departamento", givenisDepartamento.toString())
            intent.putExtra("Lista_user_Filial", givenisFilial.toString())
            intent.putExtra("Lista_user_Morada", givenisMorada.toString())
            startActivity(intent)
        }


    }

    fun getFilial(callback: GetFilialCallback, id: Int) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/filial/get/$id"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    if (response.has("data")) {
                        val dataObject = response.getJSONObject("data")
                        val filial = Filial(
                            filialId = dataObject.optInt("filialId"),
                            filialNome = dataObject.optString("filialNome"),
                            morada = dataObject.optString("morada"),
                            telemovel = dataObject.optString("telemovel"),
                            email = dataObject.optString("email")
                        )
                        callback.onSuccess(filial)
                    } else {
                        val errorMessage = "Invalid JSON response"
                        callback.onFailure(errorMessage)
                    }
                } catch (e: JSONException) {
                    val errorMessage = "Error parsing JSON response"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val errorMessage = "Error: " + error.message
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun getDepartamento(callback: GetDepartamentoCallback, id: Int) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/departamento/get/$id"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    if (response.has("data")) {
                        val dataObject = response.getJSONObject("data")
                        val departamento = Departamento(
                            departamentoId = dataObject.optInt("departamentoId"),
                            departamentoNome = dataObject.optString("departamentoNome"),
                            dataCriacao = dataObject.optString("dataCriacao"),
                            descricao = dataObject.optString("descricao")
                        )
                        callback.onSuccess(departamento)
                    } else {
                        val errorMessage = "Invalid JSON response"
                        callback.onFailure(errorMessage)
                    }
                } catch (e: JSONException) {
                    val errorMessage = "Error parsing JSON response"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val errorMessage = "Error: " + error.message
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

    fun listCargo(callback: GetCargosCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/cargo/list"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                if (response.has("data")) {
                    val dataArray = response.getJSONArray("data")
                    val cargos = ArrayList<Cargo>()

                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        val cargo = Cargo(
                            cargoId = dataObject.optInt("cargoId"),
                            cargoNome = dataObject.optString("cargoNome")
                        )
                        cargos.add(cargo)
                    }

                    callback.onSuccess(cargos)
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "Erro ao obter a lista de cargos"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    interface GetFilialCallback {
        fun onSuccess(user: Filial)
        fun onFailure(errorMessage: String)
    }

    interface GetDepartamentoCallback {
        fun onSuccess(user: Departamento)
        fun onFailure(errorMessage: String)
    }

    interface GetCargosCallback {
        fun onSuccess(user: ArrayList<Cargo>)
        fun onFailure(errorMessage: String)
    }
    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }
}