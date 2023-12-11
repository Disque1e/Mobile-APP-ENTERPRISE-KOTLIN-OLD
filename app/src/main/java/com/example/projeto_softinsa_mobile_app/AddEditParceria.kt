package com.example.projeto_softinsa_mobile_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_parceria
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Parceria
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


class AddEditParceria : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    //BASE DE DADOS EXEMPLO:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_parceria)
        val parceriaJson = intent.getStringExtra("parceriaJson")
        val isEditing = intent.getBooleanExtra("isEditing", false)

        val authorization = Authorization(this, null)
        var userId = authorization.getUserId()
        val bt_adicionar = findViewById<Button>(R.id.bt_parceria_add)
        val email = findViewById<EditText>(R.id.parceria_email)
        val telemovel = findViewById<EditText>(R.id.parceria_telemovel)
        val nome = findViewById<EditText>(R.id.parceria_nome)
        val Parceria= Parceria(this, getSharedPreferences("MyPrefs", MODE_PRIVATE).edit())


                if (isEditing) {
                    if (parceriaJson != null) {
                        // Parse the parceriaJson string to get the selectedParceria object
                        val gson = Gson()
                        val selectedParceria = gson.fromJson(parceriaJson, imagem_parceria::class.java)

                        // Populate the fields with the selectedParceria data

                        email.setText(selectedParceria.email_img_parceria)
                        telemovel.setText(selectedParceria.telemovel_img_parceria)
                        nome.setText(selectedParceria.nomeParceiro_img_parceria)


                        bt_adicionar.text = "Alterar Parceria"
                        bt_adicionar.setOnClickListener {
                            selectedParceria.parceriaId_img_parceria?.let { it1 ->
                                Parceria.updateParceria(
                                    it1,
                                    nome.text.toString(),
                                    email.text.toString(),
                                    telemovel.text.toString(),
                                    object : Parceria.CreateParceriaCallback {
                                        override fun onSuccess(sucesso: String) {
                                            Log.d("tag", sucesso)
                                            Toast.makeText(
                                                this@AddEditParceria,
                                                "Parceria alterada com sucesso!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }

                                        override fun onFailure(errorMessage: String) {
                                            Toast.makeText(
                                                this@AddEditParceria,
                                                errorMessage,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            }


                        }

                    }
                } else {
                    bt_adicionar.setOnClickListener{
                        Parceria.createParceria(userId, nome.text.toString(), email.text.toString(), telemovel.text.toString(), object : Parceria.CreateParceriaCallback {
                            override fun onSuccess(sucesso: String) {
                                Log.d("tag", sucesso)
                                Toast.makeText(this@AddEditParceria, "Parceria inserida com sucesso!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainOportunidades::class.java)
                                startActivity(intent)
                                finish()
                            }

                            override fun onFailure(errorMessage: String) {
                                Toast.makeText(this@AddEditParceria, errorMessage, Toast.LENGTH_SHORT).show()
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