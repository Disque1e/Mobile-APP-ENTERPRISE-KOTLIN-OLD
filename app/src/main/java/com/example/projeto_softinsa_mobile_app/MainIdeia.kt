package com.example.projeto_softinsa_mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_ideia
import com.example.projeto_softinsa_mobile_app.Detailed.Detailed_ideia
import com.example.projeto_softinsa_mobile_app.ListUser.MainListUsers
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_ideia
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainIdeia : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    // BASE DE DADOS EXEMPLO:
    private var lv_ideia: ListView? = null
    private var ada: Lvadapador_ideia? = null
    private var ar_img_list_ideia: ArrayList<imagem_ideia>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ideia)

        lv_ideia = findViewById(R.id.lv_ideia) as ListView

        fetchDataFromServer()

        lv_ideia!!.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, Detailed_ideia::class.java)
            intent.putExtra("Titulo_ideia", ar_img_list_ideia!![position].titulo_img_ideia)
            intent.putExtra("Tipo_ideia", ar_img_list_ideia!![position].tipo_img_tipo_ideia)
            intent.putExtra("User_ideia", ar_img_list_ideia!![position].tipo_img_User_ideia)
            intent.putExtra("Descricao_ideia", ar_img_list_ideia!![position].tipo_img_Desc_ideia)
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
        val url = "https://softinsa-web-app-carreiras01.onrender.com/ideia/list"

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

                    ar_img_list_ideia = ArrayList()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val Titulo = item.getString("titulo")
                        val Tipo = item.getString("tipo")
                        val NomeId = if (item.isNull("userId")) 0 else item.getInt("userId")
                        val Descricao = item.getString("descricao")

                        val imgItem = imagem_ideia()
                        imgItem.titulo_img_ideia = Titulo
                        imgItem.tipo_img_tipo_ideia = Tipo
                        imgItem.tipo_img_User_ideia = NomeId
                        imgItem.tipo_img_Desc_ideia = Descricao

                        ar_img_list_ideia!!.add(imgItem)
                    }

                    runOnUiThread {
                        ada = Lvadapador_ideia(applicationContext, ar_img_list_ideia!!)
                        lv_ideia!!.adapter = ada
                        ada?.notifyDataSetChanged()
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