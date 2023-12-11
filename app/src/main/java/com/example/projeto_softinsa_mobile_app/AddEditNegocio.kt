package com.example.projeto_softinsa_mobile_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_area_negocio
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_negocio
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Area_Negocio
import com.example.projeto_softinsa_mobile_app.API.Negocio
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


class AddEditNegocio : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var areaNegociosList: List<imagem_area_negocio> = emptyList()
    private var selectedAreaId: Int = 0

    //BASE DE DADOS EXEMPLO:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_negocio)
        val negocioJson = intent.getStringExtra("negocioJson")
        val isEditing = intent.getBooleanExtra("isEditing", false)

        val authorization = Authorization(this, null)
        var userId = authorization.getUserId()
        val bt_adicionar = findViewById<Button>(R.id.bt_negocio_add)
        val email = findViewById<EditText>(R.id.negocio_email)
        val telemovel = findViewById<EditText>(R.id.negocio_telemovel)
        val orcamento = findViewById<EditText>(R.id.negocio_orcamento)
        val desc = findViewById<EditText>(R.id.negocio_desc)
        val loadingText = findViewById<TextView>(R.id.loading_text)
        val areaSpinner = findViewById<Spinner>(R.id.areas_spinner)

        areaSpinner.visibility = View.GONE // Hide the spinner initially
        loadingText.visibility = View.VISIBLE // Show the loading text

        val Negocio= Negocio(this, getSharedPreferences("MyPrefs", MODE_PRIVATE).edit())
        val areaNegocio = Area_Negocio(this, getSharedPreferences("MyPrefs", MODE_PRIVATE).edit())
        areaNegocio.listArea_Negocios(object : Area_Negocio.GetArea_NegocioCallback {
            override fun onSuccess(areaNegocios: List<imagem_area_negocio>) {
                areaNegociosList = areaNegocios
                Log.d("tag", "Area Negocios List: $areaNegociosList")
                val adapter = ArrayAdapter(
                    this@AddEditNegocio,
                    android.R.layout.simple_spinner_item,
                    areaNegociosList.map { it.da_areaNegocioNome_area_negocio() })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                areaSpinner.adapter = adapter

                areaSpinner.visibility = View.VISIBLE
                loadingText.visibility = View.GONE

                if (isEditing) {
                    if (negocioJson != null) {
                        // Parse the negocioJson string to get the selectedNegocio object
                        val gson = Gson()
                        val selectedNegocio = gson.fromJson(negocioJson, imagem_negocio::class.java)

                        // Populate the fields with the selectedNegocio data

                        email.setText(selectedNegocio.email_img_negocio)
                        telemovel.setText(selectedNegocio.telemovel_img_negocio)
                        orcamento.setText(selectedNegocio.orcamento_img_negocio)
                        desc.setText(selectedNegocio.descricao_img_negocio)



                        bt_adicionar.text = "Alterar Negócio"
                        bt_adicionar.setOnClickListener{
                            val selectedNegocioAreaId = selectedNegocio.areaNegocioId_img_negocio
                            Log.d("tag", selectedNegocioAreaId.toString())
                            Log.d("tag", areaNegociosList.toString())

                            val selectedAreaIndex = areaNegociosList.indexOfFirst { it.da_areaNegocioId_area_negocio() == selectedNegocioAreaId }
                            if (selectedAreaIndex != -1) {
                                areaSpinner.setSelection(selectedAreaIndex)
                            }
                            Log.d("tag", selectedAreaId.toString())
                            selectedNegocio.negocioId_img_negocio?.let { it1 ->
                                if (selectedNegocioAreaId != null) {
                                    Negocio.updateNegocio(it1, email.text.toString(),telemovel.text.toString(),orcamento.text.toString(), desc.text.toString(), selectedAreaId , object : Negocio.CreateNegocioCallback {
                                        override fun onSuccess(sucesso: String) {
                                            Log.d("tag", selectedAreaId.toString())
                                            Log.d("tag", sucesso)
                                            Toast.makeText(this@AddEditNegocio, "Negócio alterado com sucesso!", Toast.LENGTH_SHORT).show()

                                        }

                                        override fun onFailure(errorMessage: String) {
                                            Toast.makeText(this@AddEditNegocio, errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }
                            }

                        }

                    }
                } else {
                    bt_adicionar.setOnClickListener{
                        Log.d("tag", selectedAreaId.toString())
                        Negocio.createNegocio(userId, email.text.toString(),telemovel.text.toString(),orcamento.text.toString(), desc.text.toString(), selectedAreaId , object : Negocio.CreateNegocioCallback {
                            override fun onSuccess(sucesso: String) {
                                Log.d("tag", sucesso)
                                Toast.makeText(this@AddEditNegocio, "Negócio inserido com sucesso!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainOportunidades::class.java)
                                startActivity(intent)
                                finish()
                            }

                            override fun onFailure(errorMessage: String) {
                                Toast.makeText(this@AddEditNegocio, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        })

                    }
                }

            }

            override fun onFailure(errorMessage: String) {
                Toast.makeText(this@AddEditNegocio, errorMessage, Toast.LENGTH_SHORT).show()
                loadingText.visibility = View.GONE
            }
        })

        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedArea = areaNegociosList[position] // Assuming area_negocios is accessible here
                selectedAreaId = selectedArea.da_areaNegocioId_area_negocio()!!
                val selectedAreaName = selectedArea.da_areaNegocioNome_area_negocio()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
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