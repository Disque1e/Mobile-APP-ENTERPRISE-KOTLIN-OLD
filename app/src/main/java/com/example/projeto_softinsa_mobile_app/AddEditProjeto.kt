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
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_projeto
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_tipoProjeto
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Projeto
import com.example.projeto_softinsa_mobile_app.API.Tipo_Projeto
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


class AddEditProjeto : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var tipoProjetosList: List<imagem_tipoProjeto> = emptyList()
    data class PrioridadeItem(val displayText: String, val numericValue: String)
    private val prioridadeValues = arrayOf(
        PrioridadeItem("Urgente", "1"),
        PrioridadeItem("Normal", "2"),
        PrioridadeItem("Pouco Urgente", "3"))
    private var selecteTipoProjetoId: Int = 0
    private var selectedPrioridade: Int = 0

    //BASE DE DADOS EXEMPLO:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_projeto)
        val projetoJson = intent.getStringExtra("projetoJson")
        val isEditing = intent.getBooleanExtra("isEditing", false)


        val authorization = Authorization(this, null)
        var userId = authorization.getUserId()
        val bt_adicionar = findViewById<Button>(R.id.bt_projeto_add)
        val nome = findViewById<EditText>(R.id.projeto_nome)
        val desc = findViewById<EditText>(R.id.projeto_desc)
        val orcamento = findViewById<EditText>(R.id.projeto_orcamento)
        val loadingText = findViewById<TextView>(R.id.loading_text)
        val tipoSpinner = findViewById<Spinner>(R.id.tipoProjeto_spinner)
        val prioridadeSpinner = findViewById<Spinner>(R.id.prioridadeProjeto_spinner)

        tipoSpinner.visibility = View.GONE // Hide the spinner initially
        loadingText.visibility = View.VISIBLE // Show the loading text

        val Projeto= Projeto(this, getSharedPreferences("MyPrefs", MODE_PRIVATE).edit())
        val tipoProjeto = Tipo_Projeto(this, getSharedPreferences("MyPrefs", MODE_PRIVATE).edit())
        tipoProjeto.listTipo_Projetos(object : Tipo_Projeto.GetTipo_ProjetoCallback {
            override fun onSuccess(tipoProjetos: List<imagem_tipoProjeto>) {
                tipoProjetosList = tipoProjetos
                Log.d("tag", "Area Projetos List: $tipoProjetosList")
                val adapter = ArrayAdapter(
                    this@AddEditProjeto,
                    android.R.layout.simple_spinner_item,
                    tipoProjetosList.map { it.da_tipoProjetoNome_tipoProjeto() })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tipoSpinner.adapter = adapter

                val prioridadeAdapter = ArrayAdapter(this@AddEditProjeto, android.R.layout.simple_spinner_item, prioridadeValues.map { it.displayText })
                prioridadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                prioridadeSpinner.adapter = prioridadeAdapter

                tipoSpinner.visibility = View.VISIBLE
                loadingText.visibility = View.GONE

                if (isEditing) {
                    if (projetoJson != null) {
                        // Parse the projetoJson string to get the selectedProjeto object
                        val gson = Gson()
                        val selectedProjeto = gson.fromJson(projetoJson, imagem_projeto::class.java)

                        // Populate the fields with the selectedProjeto data

                        nome.setText(selectedProjeto.projetoNome_img_projeto)
                        desc.setText(selectedProjeto.descricao_img_projeto)
                        orcamento.setText(selectedProjeto.orcamento_img_projeto)

                        val selectedTipoProjetoId = selectedProjeto.projetoId_img_projeto
                        Log.d("tag", selectedTipoProjetoId.toString())
                        Log.d("tag", tipoProjetosList.toString())
                        val selectedAreaIndex = tipoProjetosList.indexOfFirst { it.da_tipoProjetoId_tipoProjeto() == selectedTipoProjetoId }
                        if (selectedAreaIndex != -1) {
                            tipoSpinner.setSelection(selectedAreaIndex)
                        }

                        val selectedPrioridade = selectedProjeto.prioridade_img_projeto
                        val selectedPrioridadeIndex = prioridadeValues.indexOfFirst { it.numericValue == selectedPrioridade }
                        if (selectedPrioridadeIndex != -1) {
                            prioridadeSpinner.setSelection(selectedPrioridadeIndex)
                        }


                        bt_adicionar.text = "Alterar Negócio"
                        bt_adicionar.setOnClickListener{

                            Log.d("tag", selectedPrioridade.toString())
                            selectedProjeto.projetoId_img_projeto?.let { it1 ->
                                if (selectedTipoProjetoId != null) {
                                    if (selectedPrioridade != null) {
                                        Projeto.updateProjeto(it1, nome.text.toString(), desc.text.toString(), orcamento.text.toString(), selectedPrioridade, selectedTipoProjetoId , object : Projeto.CreateProjetoCallback {
                                            override fun onSuccess(sucesso: String) {
                                                Log.d("tag", sucesso)
                                                Toast.makeText(this@AddEditProjeto, "Projeto alterado com sucesso!", Toast.LENGTH_SHORT).show()

                                            }

                                            override fun onFailure(errorMessage: String) {
                                                Toast.makeText(this@AddEditProjeto, errorMessage, Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }
                                }
                            }

                        }

                    }
                } else {
                    bt_adicionar.setOnClickListener{
                        Log.d("tag", selecteTipoProjetoId.toString())
                        Projeto.createProjeto(userId,nome.text.toString(), desc.text.toString(), orcamento.text.toString(), selectedPrioridade, selecteTipoProjetoId , object : Projeto.CreateProjetoCallback {
                            override fun onSuccess(sucesso: String) {
                                Log.d("tag", sucesso)
                                Toast.makeText(this@AddEditProjeto, "Projeto inserido com sucesso!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainOportunidades::class.java)
                                startActivity(intent)
                                finish()
                            }

                            override fun onFailure(errorMessage: String) {
                                Toast.makeText(this@AddEditProjeto, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        })

                    }
                }

            }

            override fun onFailure(errorMessage: String) {
                Toast.makeText(this@AddEditProjeto, errorMessage, Toast.LENGTH_SHORT).show()
                loadingText.visibility = View.GONE
            }
        })

        tipoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedArea = tipoProjetosList[position] // Assuming area_projetos is accessible here
                selecteTipoProjetoId = selectedArea.da_tipoProjetoId_tipoProjeto()!!
                val selectedAreaName = selectedArea.da_tipoProjetoNome_tipoProjeto()

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