package com.example.projeto_softinsa_mobile_app.Detailed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.ListUser.MainListUsers
import com.google.android.material.navigation.NavigationView

class Detailed_ben : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_ben)
        //val imgView = findViewById<ImageView>(R.id.detailed_Iv_Image)
        val texto1 = findViewById<TextView>(R.id.detailed_Tv_Titulo)
        val texto2 = findViewById<TextView>(R.id.detailed_Tv_Tipo)
        val texto3 = findViewById<TextView>(R.id.detailed_Tv_Descricao)

        val givenName = intent.extras?.get("Titulo")
        val givenTipo = intent.extras?.get("Tipo")
        val givenDescricao = intent.extras?.get("Descricao")
        //val givenDraw = intent.extras?.get("drawable").toString()

        texto1.text = givenName.toString()
        texto2.text = givenTipo.toString()
        texto3.text = givenDescricao.toString()

       /* if (givenDraw != null) {
            imgView.setImageResource(givenDraw.toInt())
        }*/

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

        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}