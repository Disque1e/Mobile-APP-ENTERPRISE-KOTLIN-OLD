package com.example.projeto_softinsa_mobile_app.Des_tudo

class imagem_evento {

    var eventoId_img_evento: Int? = null
    var titulo_img_evento: String? = null
    var descricao_img_evento: String? = null
    var dataInicio_img_evento: String? = null
    var dataFim_img_evento: String? = null
    var estadoId_img_evento: Int? = null
    var userId_img_evento: Int? = null
    var notas_img_evento: String? = null

    fun da_eventoId_evento(): Int? {
        return eventoId_img_evento
    }

    fun da_titulo_evento(): String {
        return titulo_img_evento.toString()
    }

    fun da_descricao_evento(): String {
        return descricao_img_evento.toString()
    }

    fun da_dataInicio_evento(): String {
        return dataInicio_img_evento.toString()
    }

    fun da_dataFim_evento(): String {
        return dataFim_img_evento.toString()
    }

    fun da_estadoId_evento(): Int? {
        return estadoId_img_evento
    }

    fun da_userId_evento(): Int? {
        return userId_img_evento
    }
    fun da_notas_evento(): String {
        return notas_img_evento.toString()
    }

    //-----------------------------------------------------------
    fun atr_eventoId_evento(name: Int) {
        this.eventoId_img_evento = name
    }

    fun atr_titulo_evento(name: String) {
        this.titulo_img_evento = name
    }

    fun atr_descricao_evento(name: String) {
        this.descricao_img_evento = name
    }

    fun atr_tipo_evento(name: String) {
        this.titulo_img_evento = name
    }

    fun atr_dataInicio_evento(name: String) {
        this.dataInicio_img_evento = name
    }

    fun atr_dataFim_evento(name: String) {
        this.dataFim_img_evento = name
    }

    fun atr_estadoId_evento(name: Int) {
        this.estadoId_img_evento
    }

    fun atr_userId_evento(name: Int) {
        this.userId_img_evento
    }

    fun atr_notas_evento(name: String) {
        this.notas_img_evento
    }



}