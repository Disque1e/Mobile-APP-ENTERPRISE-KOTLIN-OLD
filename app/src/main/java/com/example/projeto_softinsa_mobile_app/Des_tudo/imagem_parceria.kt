package com.example.negocio_softinsa_mobile_app.Des_tudo

class imagem_parceria{
    var parceriaId_img_parceria: Int?  = null
    var nomeParceiro_img_parceria: String?  = null
    var email_img_parceria: String?  = null
    var telemovel_img_parceria: String?  = null
    var dataRegisto_img_parceria: String?  = null
    var dataAtualizacao_img_parceria: String?  = null
    var userId_img_parceria: Int?  = null

    fun da_parceriaId_parceria(): Int? {
        return parceriaId_img_parceria
    }

    fun da_nomeParceiro_parceria(): String? {
        return nomeParceiro_img_parceria
    }

    fun da_email_parceria(): String? {
        return email_img_parceria
    }

    fun da_telemovel_parceria(): String? {
        return telemovel_img_parceria
    }

    fun da_dataAtualizacao_parceria(): String? {
        return dataAtualizacao_img_parceria
    }

    fun da_dataRegisto_parceria(): String? {
        return dataRegisto_img_parceria
    }

    fun da_userId_parceria(): Int? {
        return userId_img_parceria
    }

    fun atr_parceriaId_parceria(id: Int?) {
        // You can decide whether to allow or disallow changing the value
        this.parceriaId_img_parceria = id
    }

    fun atr_nomeParceiro_parceria(nome: String?) {
        // You can decide whether to allow or disallow changing the value
        this.nomeParceiro_img_parceria = nome
    }

    fun atr_email_parceria(email: String?) {
        // You can decide whether to allow or disallow changing the value
        this.email_img_parceria = email
    }

    fun atr_telemovel_parceria(telemovel: String?) {
        // You can decide whether to allow or disallow changing the value
        this.telemovel_img_parceria = telemovel
    }

    fun atr_dataRegisto_parceria(dataRegisto: String?) {
        // You can decide whether to allow or disallow changing the value
        this.dataRegisto_img_parceria = dataRegisto
    }

    fun atr_dataAtualizacao_parceria(dataAtualizacao: String?) {
        // You can decide whether to allow or disallow changing the value
        this.dataAtualizacao_img_parceria = dataAtualizacao
    }

    fun atr_userId_parceria(userId: Int?){
        this.userId_img_parceria = userId
    }


}