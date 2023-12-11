package com.example.projeto_softinsa_mobile_app.Des_tudo

class imagem_tipoProjeto{
    var tipoProjetoId_img_tipoProjeto: Int?  = null
    var tipoProjetoNome_img_tipoProjeto: String?  = null

    fun da_tipoProjetoId_tipoProjeto(): Int? {
        return tipoProjetoId_img_tipoProjeto
    }

    fun da_tipoProjetoNome_tipoProjeto(): String? {
        return tipoProjetoNome_img_tipoProjeto
    }

    fun atr_tipoProjetoId_tipoProjeto(id: Int?) {
        // You can decide whether to allow or disallow changing the value
        this.tipoProjetoId_img_tipoProjeto = id
    }

    fun atr_tipoProjetoNome_tipoProjeto(nome: String?) {
        // You can decide whether to allow or disallow changing the value
        this.tipoProjetoNome_img_tipoProjeto = nome
    }

}