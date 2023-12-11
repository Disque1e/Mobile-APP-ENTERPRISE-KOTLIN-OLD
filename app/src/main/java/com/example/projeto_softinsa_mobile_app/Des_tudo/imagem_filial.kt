package com.example.projeto_softinsa_mobile_app.Des_tudo

class imagem_filial {
    var filialId: Int = 0
    var filialNome: String? = null
    var morada: String? = null
    var telemovel: String? = null
    var email: String? = null

    fun atr_filialId_filial(id: Int) {
        this.filialId = id
    }

    fun atr_filialNome_filial(nome: String) {
        this.filialNome = nome
    }

    fun atr_morada_filial(morada: String) {
        this.morada = morada
    }

    fun atr_telemovel_filial(telemovel: String) {
        this.telemovel = telemovel
    }

    fun atr_email_filial(email: String) {
        this.email = email
    }

    fun da_filialId_filial(): Int {
        return filialId
    }

    fun da_filialNome_filial(): String? {
        return filialNome
    }

    fun da_morada_filial(): String? {
        return morada
    }

    fun da_telemovel_filial(): String? {
        return telemovel
    }

    fun da_email_filial(): String? {
        return email
    }
}