package com.example.projeto_softinsa_mobile_app.Des_tudo

import java.util.*

class imagem_vaga {
    var vagaId_img_vaga: Int? = null
    var titulo_img_vaga: String? = null
    var descricao_img_vaga: String? = null
    var habilitacoesMin_img_vaga: String? = null
    var experienciaMin_img_vaga: String? = null
    var remuneracao_img_vaga: Int? = null
    var dataRegisto_img_vaga: String? = null
    var dataAtualizacao_img_vaga: String? = null
    var isInterna_img_vaga: Boolean? = null
    var userId_img_vaga: Int? = null
    var filialId_img_vaga: Int? = null
    var departamentoId_img_vaga: Int? = null

    fun da_vagaId_vaga(): Int? {
        return vagaId_img_vaga
    }

    fun da_descricao_vaga(): String? {
        return descricao_img_vaga
    }

    fun da_habilitacoes_vaga(): String? {
        return habilitacoesMin_img_vaga
    }

    fun da_renumeracao_vaga(): Int? {
        return remuneracao_img_vaga
    }

    fun da_dataRegisto_vaga(): String? {
        return dataRegisto_img_vaga
    }
    fun da_dataAtualizacao_vaga(): String? {
        return dataAtualizacao_img_vaga
    }
    fun da_isInterna_vaga(): Boolean? {
        return isInterna_img_vaga
    }
    fun da_userId_vaga(): Int? {
        return userId_img_vaga
    }
    fun da_experiencia_vaga(): String? {
        return experienciaMin_img_vaga
    }

    fun da_titulo_vaga(): String? {
        return titulo_img_vaga
    }

    fun da_filialId_vaga(): Int? {
        return filialId_img_vaga
    }

    fun da_departamentoId_vaga(): Int? {
        return departamentoId_img_vaga
    }

    fun atr_vagaId_vaga(vagaId: Int?) {
        vagaId_img_vaga = vagaId
    }

    fun atr_descricao_vaga(descricao: String?) {
        descricao_img_vaga = descricao
    }

    fun atr_habilitacoes_vaga(habilitacoes: String?) {
        habilitacoesMin_img_vaga = habilitacoes
    }

    fun atr_renumeracao_vaga(remuneracao: Int?) {
        remuneracao_img_vaga = remuneracao
    }

    fun atr_dataRegisto_vaga(dataRegisto: String?) {
        dataRegisto_img_vaga = dataRegisto
    }

    fun atr_dataAtualizacao_vaga(dataAtualizacao: String?) {
        dataAtualizacao_img_vaga = dataAtualizacao
    }

    fun atr_isInterna_vaga(isInterna: Boolean?) {
        isInterna_img_vaga = isInterna
    }

    fun atr_userId_vaga(userId: Int?) {
        userId_img_vaga = userId
    }

    fun atr_experiencia_vaga(experiencia: String?) {
        experienciaMin_img_vaga = experiencia
    }

    fun atr_titulo_vaga(titulo: String?) {
        titulo_img_vaga = titulo
    }

    fun atr_filialId_vaga(filialId: Int?) {
        filialId_img_vaga = filialId
    }

    fun atr_departamentoId_vaga(departamentoId: Int?) {
        departamentoId_img_vaga = departamentoId
    }

}