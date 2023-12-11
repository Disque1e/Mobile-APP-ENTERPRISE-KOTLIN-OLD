package com.example.projeto_softinsa_mobile_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_area_negocio
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_investimento
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Investimento
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


class AddEditInvestimento : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var areaNegociosList: List<imagem_area_negocio> = emptyList()

    //BASE DE DADOS EXEMPLO:
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_investimento)
        val investimentoJson = intent.getStringExtra("investimentoJson")
        val isEditing = intent.getBooleanExtra("isEditing", false)

        val authorization = Authorization(this, null)
        var userId = authorization.getUserId()
        val bt_adicionar = findViewById<Button>(R.id.bt_investimento_add)
        val orcamento = findViewById<EditText>(R.id.investimento_montante)
        val desc = findViewById<EditText>(R.id.investimento_desc)
        val Investimento= Investimento(this, getSharedPreferences("MyPrefs", MODE_PRIVATE).edit())

                if (isEditing) {
                    if (investimentoJson != null) {
                        // Parse the negocioJson string to get the selectedNegocio object
                        val gson = Gson()
                        val selectedInvestimento = gson.fromJson(investimentoJson, imagem_investimento::class.java)

                        // Populate the fields with the selectedNegocio data

                        orcamento.setText(selectedInvestimento.montante_img_investimento)
                        desc.setText(selectedInvestimento.descricao_img_investimento)


                        bt_adicionar.text = "Alterar Investimento"
                        bt_adicionar.setOnClickListener{
                            selectedInvestimento.investimentoId_img_investimento?.let { it1 ->
                                   Investimento.updateInvestimento(it1, orcamento.text.toString(), desc.text.toString() , object : Investimento.CreateInvestimentoCallback {
                                        override fun onSuccess(sucesso: String) {
                                            Log.d("tag", sucesso)
                                            Toast.makeText(this@AddEditInvestimento, "Investimento alterado com sucesso!", Toast.LENGTH_SHORT).show()

                                        }

                                        override fun onFailure(errorMessage: String) {
                                            Toast.makeText(this@AddEditInvestimento, errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    })

                            }

                        }

                    }
                } else {
                    bt_adicionar.setOnClickListener{
                        Investimento.createInvestimento(userId, orcamento.text.toString(), desc.text.toString() , object : Investimento.CreateInvestimentoCallback  {
                            override fun onSuccess(sucesso: String) {
                                Log.d("tag", sucesso)
                                Toast.makeText(this@AddEditInvestimento, "Investimento inserido com sucesso!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainOportunidades::class.java)
                                startActivity(intent)
                                finish()
                            }

                            override fun onFailure(errorMessage: String) {
                                Toast.makeText(this@AddEditInvestimento, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        })

                    }
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

}