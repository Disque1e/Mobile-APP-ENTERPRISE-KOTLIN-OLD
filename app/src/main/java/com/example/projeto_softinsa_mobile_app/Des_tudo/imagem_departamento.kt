package com.example.projeto_softinsa_mobile_app.Des_tudo

import java.util.*

class imagem_departamento {
    var departamentoId_img_departamento: Int? = null
    var departamentoNome_img_departamento: String? = null
    var dataCriacao_img_departamento: String? = null
    var descricao_img_departamento: String? = null

    fun da_departamentoId_departamento(): Int? {
        return departamentoId_img_departamento
    }

    fun da_descricao_departamento(): String? {
        return descricao_img_departamento
    }

    fun da_departamentoNome_departamento(): String? {
        return departamentoNome_img_departamento
    }

    fun da_dataCriacao_img_departamento(): String? {
        return dataCriacao_img_departamento
    }

    fun atr_departamentoId_departamento(departamentoId: Int?) {
        departamentoId_img_departamento = departamentoId
    }

    fun atr_descricao_departamento(descricao: String?) {
        descricao_img_departamento = descricao
    }

    fun atr_departamentoNome_departamento(nome: String?) {
        departamentoNome_img_departamento = nome
    }

    fun atr_dataCriacao_departamento(dataCriacao: String?) {
        dataCriacao_img_departamento = dataCriacao
    }


}