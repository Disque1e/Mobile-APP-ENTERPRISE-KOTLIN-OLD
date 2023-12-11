package com.example.projeto_softinsa_mobile_app.Des_tudo

import android.util.Base64
import com.google.gson.Gson

class imagem_candidatura {

    var candidaturaId_img_candidatura: Int? = null
    var dataCriacao_img_candidatura: String? = null
    var dataAtualizacao_img_candidatura: String? = null
    var cv_img_candidatura: CV? = null
    var isAtiva_img_candidatura: Boolean? = null
    var userId_img_candidatura: Int? = null
    var vagaId_img_candidatura: Int? = null

    constructor() {
        // Default (no-argument) constructor
    }

    fun da_candidaturaId_candidatura(): Int? {
        return candidaturaId_img_candidatura
    }

    fun da_dataCriacao_candidatura(): String? {
        return dataCriacao_img_candidatura
    }

    fun da_dataAtualizacao_candidatura(): String? {
        return dataAtualizacao_img_candidatura
    }

    fun da_cv_candidatura(): CV? {
        return cv_img_candidatura
    }

    fun da_isAtiva_candidatura(): Boolean? {
        return isAtiva_img_candidatura
    }

    fun da_userId_candidatura(): Int? {
        return userId_img_candidatura
    }

    fun da_vagaId_candidatura(): Int? {
        return vagaId_img_candidatura
    }

    fun atr_candidaturaId_candidatura(candidaturaId: Int?) {
        candidaturaId_img_candidatura = candidaturaId
    }

    fun atr_dataCriacao_candidatura(dataCriacao: String?) {
        dataCriacao_img_candidatura = dataCriacao
    }

    fun atr_dataAtualizacao_candidatura(dataAtualizacao: String?) {
        dataAtualizacao_img_candidatura = dataAtualizacao
    }

    fun atr_cv_candidatura(cv: CV?) {
        cv_img_candidatura = cv
    }

    fun atr_isAtiva_candidatura(isAtiva: Boolean?) {
        isAtiva_img_candidatura = isAtiva
    }

    fun atr_userId_candidatura(userId: Int?) {
        userId_img_candidatura = userId
    }

    fun atr_vagaId_candidatura(vagaId: Int?) {
        vagaId_img_candidatura = vagaId
    }

    data class CV(
        val type: String?,
        val data: List<Int>?
    )



}
