package com.example.projeto_softinsa_mobile_app.Detailed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_candidatura
import com.example.projeto_softinsa_mobile_app.R
import com.example.projeto_softinsa_mobile_app.API.Candidatura
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.example.projeto_softinsa_mobile_app.API.Vaga
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Detailed_candidatura : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_candidatura)
        val candidaturaJson = intent.getStringExtra("candidaturaJson") // Retrieve the vaga JSON string
        if (candidaturaJson != null) {
            Log.d("JSON", candidaturaJson)
        }
        val gson = Gson()
        val selectedCandidatura = gson.fromJson(candidaturaJson, imagem_candidatura::class.java)
        val numero = findViewById<TextView>(R.id.detailed_Candidatura_Numero)
        val nome = findViewById<TextView>(R.id.detailed_Candidatura_Nome)
        val email = findViewById<TextView>(R.id.detailed_Candidatura_Email)
        val telefone = findViewById<TextView>(R.id.detailed_Candidatura_Telefone)
        val data = findViewById<TextView>(R.id.detailed_Candidatura_Data)
        val desc = findViewById<TextView>(R.id.detailed_Candidatura_Desc)
        val decodedData: imagem_candidatura.CV? = selectedCandidatura.cv_img_candidatura
        val intListData: List<Int>? = decodedData?.data
        val fileType: String? = decodedData?.type
        val abrirCandidatura = findViewById<Button>(R.id.bt_detailed_Candidatura_Curriculo)
        val user = Perfil(this, null)


        if (selectedCandidatura.userId_img_candidatura != null) {
            user.getUser(selectedCandidatura.userId_img_candidatura!! , object : Perfil.GetUserCallback {
                override fun onSuccess(user: Perfil.User) {
                    Log.d("tag", "${user.primeiroNome}")
                    email.text = user.email
                    nome.text = user.primeiroNome + " " + user.ultimoNome
                    telefone.text = user.telemovel
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = dateFormat.parse(user.dataRegisto)
                    val formattedDate = dateFormat.format(date)
                    data.text = formattedDate
                }

                override fun onFailure(errorMessage: String) {
                    numero.text = "Error: $errorMessage"
                }
            })
        } else {
            numero.text = "O utilizador foi eliminado!"
        }

        val vaga = Vaga(this, null)
        if (selectedCandidatura.vagaId_img_candidatura != null) {
            vaga.getVaga(selectedCandidatura.vagaId_img_candidatura!! , object : Vaga.GetVagaSingleCallback {
                override fun onSuccess(vaga: Vaga.Vaga) {
                    Log.d("tag", "${vaga.titulo}")
                    numero.text = vaga.titulo
                }

                override fun onFailure(errorMessage: String) {
                    numero.text = "Error: $errorMessage"
                }
            })
        } else {
            numero.text = "A vaga não existe!"
        }

        abrirCandidatura.setOnClickListener {
            Log.d("tag", intListData.toString())
            if (intListData != null) {
                val byteArrayData = intListData.map { it.toByte() }.toByteArray()
                Log.d("tag", byteArrayData.toString())
                val fileName = "curriculo.pdf"
                val file = File(getExternalFilesDir(null), "curriculo.pdf")

                try {
                    FileOutputStream(file).use { outputStream ->
                        outputStream.write(byteArrayData)
                    }
                    Toast.makeText(this, "PDF file saved successfully.", Toast.LENGTH_SHORT).show()

                    // Open the PDF file using an intent
                    val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/pdf")
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "No PDF viewer application found.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to save PDF file.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val eliminarCandidatura = findViewById<ImageButton>(R.id.bt_detailed_Eliminar_Candidatura)
        eliminarCandidatura.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirmação")
            alertDialogBuilder.setMessage("Tem certeza de que deseja eliminar este candidadutra? Essa ação não pode ser desfeita!")
            val candidatura = Candidatura(this, null)
            alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
                selectedCandidatura.da_candidaturaId_candidatura()?.let { it1 ->
                    candidatura.deleteCandidatura(it1,  object : Candidatura.CreateCandidaturaCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(this@Detailed_candidatura, "Candidatura eliminada com sucesso!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(applicationContext, MainOportunidades::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(errorMessage: String) {
                            Toast.makeText(this@Detailed_candidatura, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Não") { dialog, _ ->
                dialog.cancel()
            }
            alertDialogBuilder.show()
        }

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
