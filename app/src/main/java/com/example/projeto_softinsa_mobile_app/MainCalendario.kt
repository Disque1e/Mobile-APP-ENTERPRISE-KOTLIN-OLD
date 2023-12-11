package com.example.projeto_softinsa_mobile_app

import android.R.color
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.style.LineBackgroundSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.API.Evento
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_calendario
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_evento
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_calendario
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_parceria
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import com.devstune.searchablemultiselectspinner.*
import com.example.projeto_softinsa_mobile_app.API.userEvento
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_lista_user


class MainCalendario : AppCompatActivity() {


        private lateinit var toggle: ActionBarDrawerToggle
        private lateinit var drawerLayout: DrawerLayout
        private lateinit var navigationView: NavigationView
        private lateinit var listView: ListView
        private lateinit var adapter: Lvadapador_calendario
        private val decoratorDates = mutableListOf<CalendarDay>()
        private val calendarioArrayList = ArrayList<imagem_evento>()
        private var decorator: YourDecorator = YourDecorator(emptyList())
        private lateinit var  adicionarEvento: FloatingActionButton
        private lateinit var textViewInicial: TextView
        private lateinit var textViewFinal: TextView
        private lateinit var btParticipantes: Button
        private var usersList: List<imagem_lista_user> = emptyList()
        private lateinit var timePickerDialog: TimePickerDialog
        var selectedStartDate: java.util.Date? = null
        var selectedEndDate: java.util.Date? = null
        val selectedItemsList = mutableListOf<SearchableItem>()

        var cargoId = 0
        var nome = ""
        var email = mutableListOf<String>()
        var userId = 0

        private fun openDatePicker(final: Boolean, datePickerDialog: DatePickerDialog) {
                Log.d("DatePicker", "DatePicker opened.")
                val currentDay = Calendar.getInstance()

                // Set the callback to handle date picking
                datePickerDialog.setOnDateSetListener { datePicker, year, month, day ->
                        val selectedDate = Calendar.getInstance()
                        if (!final) {

                                selectedDate.set(Calendar.YEAR, year)
                                selectedDate.set(Calendar.MONTH, month)
                                selectedDate.set(Calendar.DAY_OF_MONTH, day)
                                textViewInicial.setText("$year.$month.$day")
                        } else {

                                selectedDate.set(Calendar.YEAR, year)
                                selectedDate.set(Calendar.MONTH, month)
                                selectedDate.set(Calendar.DAY_OF_MONTH, day)
                                textViewFinal.setText("$year.$month.$day")
                        }

                        // Open the time picker dialog after picking the date
                        openTimePicker(selectedDate, final)
                }

                datePickerDialog.updateDate(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.show()
        }



        private fun openTimePicker(selectedDate: Calendar, final: Boolean) {
                val currentTime = Calendar.getInstance()
                val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        val selectedDateTime = selectedDate.clone() as Calendar
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedDateTime.set(Calendar.MINUTE, minute)
                        val formattedTime = String.format("%02d:%02d", hourOfDay, minute)

                        if (!final) {
                                // Store selectedDateTime directly
                                selectedStartDate = selectedDateTime.time
                                textViewInicial.append(" - $formattedTime")
                        } else {
                                // Store selectedDateTime directly
                                selectedEndDate = selectedDateTime.time
                                textViewFinal.append(" - $formattedTime")
                        }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false)

                timePickerDialog.show()
        }

        class YourDecorator(dates: List<CalendarDay>) : DayViewDecorator {
                private val dates: HashSet<CalendarDay> = HashSet(dates)

                override fun shouldDecorate(day: CalendarDay): Boolean {
                        return dates.contains(day)
                }

                override fun decorate(view: DayViewFacade) {
                        view.addSpan(DotSpan(8f, Color.RED)) // Customize the dot size and color as desired
                        view.setDaysDisabled(false)
                }
        }


        private fun filterEventsByDate(selectedDate: Date) {
                val filteredEvents = ArrayList<imagem_evento>()

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDateString = dateFormat.format(selectedDate)
                Log.i("Tag", "Selected Date: $selectedDateString")

                for (event in calendarioArrayList) {
                        val eventDateStr = event.dataInicio_img_evento

                        // Parse the eventDate string to a Date object
                        val eventDate = dateFormat.parse(eventDateStr)

                        // Format the eventDate back to a string for comparison (optional, for logging purposes)
                        val eventDateString = dateFormat.format(eventDate)


                        if (eventDateString == selectedDateString) {
                                filteredEvents.add(event)
                        }
                }

                // Pass the filtered events to your adapter and update the ListView
                val listView = findViewById<ListView>(R.id.lv_lista_Eventos)
                listView.adapter = Lvadapador_calendario(this, filteredEvents)
        }


        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main_calendario)
                adicionarEvento = findViewById(R.id.adicionarEvento)
                val user = Perfil(this, null)
                cargoId = user.getStoredCargoId()
                val auth = Authorization(this, null)
                userId = auth.getUserId()
                listView = findViewById<ListView>(R.id.lv_lista_Eventos)
                adapter = Lvadapador_calendario(this, calendarioArrayList)
                listView.adapter = adapter
                val calendarView = findViewById<MaterialCalendarView>(R.id.calendarView)

                // ...
                calendarView.addDecorators(decorator)

                // ...

                // Inside the onSuccess callback after updating the calendarioArrayList, update the decoratorDates
                val eventoAPI = Evento(this, null)
                eventoAPI.listEventos(object : Evento.GetEventoCallback {
                        override fun onSuccess(eventos: List<imagem_evento>) {
                                calendarioArrayList.clear()
                                if (cargoId <= 2) {
                                        calendarioArrayList.addAll(eventos)
                                        Log.d("tag", eventos.toString())
                                } else {
                                        val filteredEventos = eventos.filter { it.userId_img_evento == userId }
                                        calendarioArrayList.addAll(filteredEventos)
                                        Log.d("tag", filteredEventos.toString())
                                }


                                decoratorDates.clear() // Clear the previous decoratorDates
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                                for (event in calendarioArrayList) {
                                        val date = dateFormat.parse(event.dataInicio_img_evento)
                                        val calendar = Calendar.getInstance()
                                        calendar.time = date
                                        val calendarDay = CalendarDay.from(
                                                calendar.get(Calendar.YEAR),
                                                calendar.get(Calendar.MONTH) + 1,
                                                calendar.get(Calendar.DAY_OF_MONTH)
                                        )

                                        Log.i("Tag", "Ano: ${calendar.get(Calendar.YEAR)}")
                                        Log.i("Tag", "Mês: ${calendar.get(Calendar.MONTH) + 1}")
                                        Log.i("Tag", "Dia: ${calendar.get(Calendar.DAY_OF_MONTH)}")
                                        val decorator = YourDecorator(
                                                calendarioArrayList.map { imagemEvento ->
                                                        CalendarDay.from(
                                                                calendar.get(Calendar.YEAR),
                                                                calendar.get(Calendar.MONTH) + 1,
                                                                calendar.get(Calendar.DAY_OF_MONTH)
                                                        )
                                                }
                                        )
                                }

                                // Recreate the decorator using the updated list of CalendarDay objects
                                calendarView.selectedDate = CalendarDay.today()
                                var date = CalendarDay.today()
                                val calendar = Calendar.getInstance()
                                calendar.set(date.year, date.month-1, date.day)
                                val selectedDateSql: java.sql.Date = Date(calendar.timeInMillis)
                                filterEventsByDate(selectedDateSql)
                                calendarView.addDecorators(decorator)

                                // Notify the adapter and update the ListView with the filtered events
                                adapter.notifyDataSetChanged()

                                calendarView.setOnDateChangedListener { widget, date, selected ->
                                        val calendar = Calendar.getInstance()
                                        calendar.set(date.year, date.month-1, date.day)
                                        val selectedDateSql: java.sql.Date = Date(calendar.timeInMillis)
                                        filterEventsByDate(selectedDateSql)
                                }
                        }

                        override fun onFailure(errorMessage: String) {
                                // Handle the API request failure
                                Toast.makeText(this@MainCalendario, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                })

                adicionarEvento.setOnClickListener {
                        val dialogView = layoutInflater.inflate(R.layout.dialog_evento, null) // Replace with your actual dialog layout XML
                        val dialogBuilder = AlertDialog.Builder(this)
                        dialogBuilder.setView(dialogView)
                        dialogBuilder.setTitle("Adicionar Evento")
                        val datePickerDialog = DatePickerDialog(this)
                        textViewInicial = dialogView.findViewById(R.id.textViewInicial)
                        textViewFinal = dialogView.findViewById(R.id.textViewFinal)
                        btParticipantes = dialogView.findViewById(R.id.btParticipantes)

                        dialogView.findViewById<Button>(R.id.btDateTime).setOnClickListener {
                                openDatePicker(false, datePickerDialog)
                        }
                        dialogView.findViewById<Button>(R.id.btDateTime2).setOnClickListener {
                                openDatePicker(true, datePickerDialog)
                        }
                        val listaUsers = mutableListOf<SearchableItem>()

                        user.listUser(object : Perfil.GetUserListaCallback {
                                override fun onSuccess(users: List<imagem_lista_user>) {
                                        usersList = users
                                        for (user in users) {
                                                val searchableItem =
                                                        user.Nome_img_lista_user?.let { it1 ->
                                                                SearchableItem(
                                                                        it1, user.Id_img_lista_user.toString())
                                                        }
                                                if (searchableItem != null) {
                                                        listaUsers.add(searchableItem)
                                                }
                                                if(user.Id_img_lista_user == userId)
                                                {
                                                        nome  = user.Nome_img_lista_user.toString() + " " + user.Sobrenome_img_lista_user.toString()
                                                }
                                        }
                                }

                                override fun onFailure(errorMessage: String) {
                                        // Handle the API request failure
                                        Toast.makeText(this@MainCalendario, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                        })
                        btParticipantes.setOnClickListener {
                                SearchableMultiSelectSpinner.show(this, "Select Items","Done", listaUsers, object :
                                        SelectionCompleteListener {
                                        override fun onCompleteSelection(selectedItems: ArrayList<SearchableItem>) {
                                                selectedItemsList.clear();
                                                selectedItemsList.addAll(selectedItems)
                                                Log.e("data", selectedItems.toString())
                                                for (selectedItem in selectedItemsList) {
                                                        val selectedItemCode = selectedItem.code.toInt()
                                                        for (user in usersList) {
                                                                if (selectedItemCode == user.Id_img_lista_user) {
                                                                        user.Email_img_lista_user?.let { it1 ->
                                                                                email.add(
                                                                                        it1
                                                                                )
                                                                        }
                                                                }
                                                        }
                                                }


                                        }
                                })
                        }

                        dialogBuilder.setPositiveButton("Adicionar") { dialog, which ->
                                val spinnerEventType = dialogView.findViewById<Spinner>(R.id.spinnerTipoEvento)
                                val opcaoEvento = spinnerEventType.selectedItem.toString()
                                val titulo = dialogView.findViewById<EditText>(R.id.editTextTitulo).text.toString()
                                val descricao = dialogView.findViewById<EditText>(R.id.editTextDescricao).text.toString()
                                val btInicial = dialogView.findViewById<Button>(R.id.btDateTime)
                                val btFinal = dialogView.findViewById<Button>(R.id.btDateTime2)

                                val userEventoApi = userEvento(this, null)
                                var i = -1
                                //Preciso adicionar o foreach para cada user
                                eventoAPI.createEvento(titulo, descricao, opcaoEvento,selectedStartDate.toString(), selectedEndDate.toString(), 0 ,  userId, "", object: Evento.GetEventoSingleCallback
                                {

                                        override fun onSuccess(evento: Evento.Evento) {
                                                for (selectedItem in selectedItemsList) {
                                                        i++
                                                        Log.e("data", email[i])
                                                        Log.e("data", nome)
                                                        Log.e("data", i.toString())
                                                        userEventoApi.createEvento(
                                                                evento.eventoId,
                                                                selectedItem.code.toInt(),
                                                                evento.titulo,
                                                                evento.descricao,
                                                                evento.tipo,
                                                                evento.dataInicio,
                                                                evento.dataFim,
                                                                email[i],
                                                                nome,
                                                                object :
                                                                        userEvento.CreateUserEventoCallback {
                                                                        override fun onSuccess(
                                                                                resposta: String
                                                                        ) {
                                                                                Toast.makeText(
                                                                                        this@MainCalendario,
                                                                                        "Evento Criado com Sucesso!",
                                                                                        Toast.LENGTH_SHORT
                                                                                ).show()

                                                                        }

                                                                        override fun onFailure(
                                                                                errorMessage: String
                                                                        ) {
                                                                                // Handle the API request failure
                                                                                Toast.makeText(
                                                                                        this@MainCalendario,
                                                                                        errorMessage,
                                                                                        Toast.LENGTH_SHORT

                                                                                ).show()

                                                                        }

                                                                })

                                                }


                                        }

                                        override fun onFailure(errorMessage: String) {
                                                // Handle the API request failure
                                                Toast.makeText(this@MainCalendario, errorMessage, Toast.LENGTH_SHORT).show()

                                        }



                                })


                                //Isto serve para dar refresh!!
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                        }
                                .setNegativeButton("Cancelar") { dialog, which ->
                                        dialog.dismiss()
                                }

                        val alertDialog = dialogBuilder.create()
                        alertDialog.show()

                        val items = arrayOf("Reunião", "Entrevista", "Outro")
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
                        val spinnerEventType = dialogView.findViewById<Spinner>(R.id.spinnerTipoEvento)
                        spinnerEventType.adapter = adapter


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