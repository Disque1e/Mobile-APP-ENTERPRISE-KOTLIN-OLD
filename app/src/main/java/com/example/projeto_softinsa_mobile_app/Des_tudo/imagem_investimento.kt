package com.example.negocio_softinsa_mobile_app.Des_tudo

class imagem_investimento{
    var investimentoId_img_investimento: Int?  = null
    var montante_img_investimento: String?  = null
    var descricao_img_investimento: String?  = null
    var dataRegisto_img_investimento: String?  = null
    var dataAtualizacao_img_investimento: String?  = null
    var dataInvestimento_img_investimento: String?  = null
    var estadoId_img_investimento: Int? = null
    var userId_img_investimento: Int? = null

    fun da_investimentoId_investimento(): Int? {
        return investimentoId_img_investimento
    }

    fun da_montante_investimento(): String? {
        return montante_img_investimento
    }


    fun da_descricao_investimento(): String? {
        return descricao_img_investimento
    }


    fun da_dataRegisto_investimento(): String? {
        return dataRegisto_img_investimento
    }

    fun da_dataAtualizacao_investimento(): String? {
        return dataAtualizacao_img_investimento
    }

    fun da_dataInvestimento_investimento(): String? {
        return dataInvestimento_img_investimento
    }

    fun da_estadoId_investimento(): Int? {
        return estadoId_img_investimento
    }

    fun da_userId_investimento(): Int? {
        return userId_img_investimento
    }


    fun atr_investimentoId_investimento(id: Int?) {
        // You can decide whether to allow or disallow changing the value
        this.investimentoId_img_investimento = id
    }

    fun atr_montante_investimento(montante: String?) {
        // You can decide whether to allow or disallow changing the value
        this.montante_img_investimento = montante
    }

    fun atr_descricao_investimento(descricao: String?) {
        // You can decide whether to allow or disallow changing the value
        this.descricao_img_investimento = descricao
    }

    fun atr_dataRegisto_investimento(dataRegisto: String?) {
        // You can decide whether to allow or disallow changing the value
        this.dataRegisto_img_investimento = dataRegisto
    }

    fun atr_dataAtualizacao_investimento(dataAtualizacao: String?) {
        // You can decide whether to allow or disallow changing the value
        this.dataAtualizacao_img_investimento = dataAtualizacao
    }

    fun atr_dataInvestimento_investimento(dataAtualizacao: String?) {
        // You can decide whether to allow or disallow changing the value
        this.dataInvestimento_img_investimento = dataAtualizacao
    }

    fun atr_estadoId_investimento(estadoId: Int?) {
        // You can decide whether to allow or disallow changing the value
        this.estadoId_img_investimento = estadoId
    }

    fun atr_userId_investimento(userId: Int?) {
        // You can decide whether to allow or disallow changing the value
        this.userId_img_investimento = userId
    }

}