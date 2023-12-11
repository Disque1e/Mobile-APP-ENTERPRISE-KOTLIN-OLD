package com.example.projeto_softinsa_mobile_app

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Candidatura
import com.google.android.material.navigation.NavigationView


class AddCandidatura : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var pdfTextView: TextView
    private lateinit var btCandidatar: Button
    private lateinit var pdfUri: Uri
    private lateinit var pdfData: ByteArray
    var userId = 0

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }

    //BASE DE DADOS EXEMPLO:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_candidatura)
        val authorization = Authorization(this, null)
        userId = authorization.getUserId()
        pdfTextView = findViewById(R.id.selectedPDF)
        btCandidatar = findViewById(R.id.bt_candidatura_candidatar)
        val IDVaga = intent.getIntExtra("ID", 0)

        pdfTextView.setOnClickListener {
            selectPdf()

        }

        btCandidatar.setOnClickListener {
            val candidatura = Candidatura(this, null)
            candidatura.createCandidatura(
                userId,
                IDVaga,
                pdfData,
                object : Candidatura.GetCandidaturaSingleCallback {
                    override fun onSuccess(candidatura: Boolean) {
                        Toast.makeText(
                            this@AddCandidatura,
                            "Candidatura Submetida",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(errorMessage: String) {
                        Toast.makeText(this@AddCandidatura, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                })
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

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // For loading PDF
        when (requestCode) {
            12 -> if (resultCode == RESULT_OK) {

                pdfUri = data?.data!!
                val uri: Uri = data?.data!!
                val uriString: String = uri.toString()
                var pdfName: String? = null
                if (uriString.startsWith("content://")) {
                    var myCursor: Cursor? = null
                    try {
                        // Setting the PDF to the TextView
                        myCursor = applicationContext!!.contentResolver.query(uri, null, null, null, null)
                        if (myCursor != null && myCursor.moveToFirst()) {
                            pdfName = myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            pdfTextView.text = pdfName
                        }
                    } finally {
                        myCursor?.close()
                    }
                }
                pdfUri = data?.data!!
                val inputStream = contentResolver.openInputStream(pdfUri)
                pdfData = inputStream?.readBytes() ?: ByteArray(0)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}