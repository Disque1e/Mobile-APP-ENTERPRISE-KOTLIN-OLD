package com.example.projeto_softinsa_mobile_app.Des_tudo

class imagem_user_evento {

    var eventoId_img_user_evento: Int? = null
    var userId_img_user_evento: Int? = null
    var titulo_img_user_evento: String? = null
    var descricao_img_user_evento: String? = null
    var dataInicio_img_user_evento: String? = null
    var dataFim_img_user_evento: String? = null
    var tipo_img_user_evento: String? = null
    var email_img_user_evento: String? = null
    var nome_img_user_evento: String? = null

    fun da_eventoId_user_evento(): Int? {
        return eventoId_img_user_evento
    }

    fun da_userId_user_evento(): Int? {
        return userId_img_user_evento
    }

    fun da_descricao_user_evento(): String {
        return descricao_img_user_evento.toString()
    }

    fun da_titulo_user_evento(): String {
        return titulo_img_user_evento.toString()
    }

    fun da_dataInicio_user_evento(): String {
        return dataInicio_img_user_evento.toString()
    }

    fun da_dataFim_user_evento(): String {
        return dataFim_img_user_evento.toString()
    }

    fun da_tipo_user_evento(): String? {
        return tipo_img_user_evento
    }

    fun da_email_user_evento(): String? {
        return email_img_user_evento
    }
    fun da_nome_user_evento(): String? {
        return nome_img_user_evento.toString()
    }

    //-----------------------------------------------------------
    fun atr_eventoId_img_user_evento(name: Int) {
        this.eventoId_img_user_evento = name
    }

    fun atr_userId_img_user_evento(name: Int) {
        this.userId_img_user_evento = name
    }

    fun atr_titulo_img_user_evento(name: String) {
        this.titulo_img_user_evento = name
    }

    fun atr_descricao_img_user_evento(name: String) {
        this.descricao_img_user_evento = name
    }

    fun atr_dataInicio_img_user_evento(name: String) {
        this.dataInicio_img_user_evento = name
    }

    fun atr_dataFim_img_user_evento(name: String) {
        this.dataFim_img_user_evento = name
    }

    fun atr_tipo_img_user_evento(name: String) {
        this.tipo_img_user_evento = name
    }

    fun atr_email_img_user_evento(name: String) {
        this.email_img_user_evento
    }

    fun atr_nome_img_user_evento(name: String) {
        this.nome_img_user_evento
    }



}