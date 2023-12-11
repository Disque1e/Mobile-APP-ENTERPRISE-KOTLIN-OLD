package com.example.projeto_softinsa_mobile_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.google.android.material.navigation.NavigationView

class MainPage : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var perfil: Perfil
    var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        val nome = findViewById<TextView>(R.id.main_nome)
        val authorization = Authorization(this, null)
        userId = authorization.getUserId()
        perfil = Perfil(this, getSharedPreferences("loginPrefs", Context.MODE_PRIVATE).edit())

        perfil.getUser(userId, object : Perfil.GetUserCallback {
            override fun onSuccess(user: Perfil.User) {
                val sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("cargoId", user.cargoId)
                editor.putBoolean("isColaborador", user.isColaborador)
                editor.apply()
                nome.text = user.primeiroNome + " " + user.ultimoNome
            }

            override fun onFailure(errorMessage: String) {
               Log.d("tag", errorMessage)
            }
        })

        val op_btn = findViewById<ImageButton>(R.id.oportunities_btn)
        val ben_btn = findViewById<ImageButton>(R.id.benefits_btn)
        val jb_btn = findViewById<ImageButton>(R.id.job_btn)
        val id_btn = findViewById<ImageButton>(R.id.ideas_btn)
/*---------------------Hooks------------------------*/
        op_btn.setOnClickListener {
            val x = Intent(this, MainOportunidades::class.java)
            startActivity(x)
        }

        ben_btn.setOnClickListener {
            val x = Intent(this, MainBeneficio::class.java)
            startActivity(x)
        }

        jb_btn.setOnClickListener {
            val x = Intent(this, MainOferta_Vaga::class.java)
            startActivity(x)
        }

        id_btn.setOnClickListener {
            val x = Intent(this, MainIdeia::class.java)
            startActivity(x)
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