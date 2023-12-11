package com.example.projeto_softinsa_mobile_app.Des_tudo

import java.sql.Date
import java.sql.Time

class imagem_calendario {
    var titulo_img_calendario: String? = null
    var dia_img_calendario: Date? = null
    var horaInicio_img_calendario: Time? = null
    var horaFinal_img_calendario: Time? = null

    fun da_titulo_calendario(): String {
        return titulo_img_calendario.toString()
    }

    fun da_dia_calendario(): String {
        return dia_img_calendario.toString()
    }

    fun da_horaInicio_calendario(): String {
        return horaInicio_img_calendario.toString()
    }

    fun da_horaFinal_calendario(): String {
        return horaFinal_img_calendario.toString()
    }
    //-----------------------------------------------------------
    fun atr_titulo_calendario(name: String) {
        this.titulo_img_calendario = name
    }
    fun atr_dia_calendario(name: Date) {
        this.dia_img_calendario = name
    }
    fun atr_horaInicio_calendario(name: Time) {
        this.horaInicio_img_calendario = name
    }
    fun atr_horaFinal_calendario(name: Time) {
        this.horaFinal_img_calendario = name
    }

}