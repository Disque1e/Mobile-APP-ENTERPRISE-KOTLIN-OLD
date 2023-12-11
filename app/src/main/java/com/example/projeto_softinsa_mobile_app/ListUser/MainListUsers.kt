package com.example.projeto_softinsa_mobile_app.ListUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_lista_user
import com.example.projeto_softinsa_mobile_app.Detailed.Detailed_list_users
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_lista_user
import com.google.android.material.navigation.NavigationView
import okhttp3.*

import org.json.JSONException
import java.io.IOException

import org.json.JSONObject

class MainListUsers : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private var lv_Lista_user: ListView? = null
    private var ada: Lvadapador_lista_user? = null
    private var ar_img_list_User_list: ArrayList<imagem_lista_user>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list_users)

        lv_Lista_user = findViewById(R.id.lv_Lista_user)

        fetchDataFromServer()

        lv_Lista_user!!.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, Detailed_list_users::class.java)
            intent.putExtra("userId", ar_img_list_User_list!![position].Id_img_lista_user)
            intent.putExtra("Lista_user_Nome", ar_img_list_User_list!![position].Nome_img_lista_user)
            intent.putExtra("Lista_user_Sobrenome", ar_img_list_User_list!![position].Sobrenome_img_lista_user)
            intent.putExtra("Lista_user_Email", ar_img_list_User_list!![position].Email_img_lista_user)
            intent.putExtra("Lista_user_Telemovel", ar_img_list_User_list!![position].Telemovel_img_lista_user)
            intent.putExtra("Lista_user_Num_Func", ar_img_list_User_list!![position].Num_Func_img_lista_user.toString())
            intent.putExtra("Lista_user_Estado", ar_img_list_User_list!![position].Estado_img_lista_user)
            intent.putExtra("Lista_user_Cargo", ar_img_list_User_list!![position].Cargo_img_lista_user)
            intent.putExtra("Lista_user_Departamento", ar_img_list_User_list!![position].Departamento_img_lista_user.toString())
            intent.putExtra("Lista_user_Filial", ar_img_list_User_list!![position].Filial_img_lista_user.toString())
            intent.putExtra("Lista_user_Morada", ar_img_list_User_list!![position].Morada_img_lista_user)
            startActivity(intent)
        }
        /*-----------------------------NAVGATION MENU BAR-----------------------------*/
        var drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val menuBtn = findViewById<ImageButton>(R.id.menu_btn)
        menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        NavigationViewHelper.setupNavigationView(this, drawerLayout, navigationView)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchDataFromServer() {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/user/list"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Erro ao obter dados do servidor: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                try {
                    val jsonObject = JSONObject(responseBody)
                    val jsonArray = jsonObject.getJSONArray("data")

                    ar_img_list_User_list = ArrayList()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val userId = item.getInt("userId")
                        val nome = item.getString("primeiroNome")
                        val sobrenome = item.getString("ultimoNome")
                        val email = item.getString("email")
                        val telemovel = item.getString("telemovel")
                        val numFuncionario = if (item.isNull("numeroFuncionario")) 0 else item.getInt("numeroFuncionario")
                        val cargoId = if (item.isNull("cargoId")) 0 else item.getInt("cargoId")
                        val estado = if (item.getBoolean("isAtivo")) "Ativo" else "Inativo"
                        val morada = item.getString("morada")
                        val departamentoId = if (item.isNull("departamentoId")) 0 else item.getInt("departamentoId")
                        val filialId = if (item.isNull("filialId")) 0 else item.getInt("filialId")

                        val imgItem = imagem_lista_user()
                        imgItem.Id_img_lista_user = userId
                        imgItem.Nome_img_lista_user = nome
                        imgItem.Sobrenome_img_lista_user = sobrenome
                        imgItem.Email_img_lista_user = email
                        imgItem.Telemovel_img_lista_user = telemovel
                        imgItem.Num_Func_img_lista_user = numFuncionario
                        imgItem.Cargo_img_lista_user = cargoId
                        imgItem.Estado_img_lista_user = estado
                        imgItem.Morada_img_lista_user = morada
                        imgItem.Departamento_img_lista_user = departamentoId
                        imgItem.Filial_img_lista_user = filialId
                        ar_img_list_User_list!!.add(imgItem)
                    }

                    runOnUiThread {
                        ada = Lvadapador_lista_user(applicationContext, ar_img_list_User_list!!)
                        lv_Lista_user!!.adapter = ada
                    }

                } catch (e: JSONException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Erro ao analisar a resposta do servidor: $e", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}