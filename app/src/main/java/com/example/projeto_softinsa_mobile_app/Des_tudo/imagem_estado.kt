package com.example.projeto_softinsa_mobile_app.Des_tudo

class imagem_estado{
    var estadoId_img_estado: Int?  = null
    var estadoNome_img_estado: String?  = null

    fun da_estadoId_img_estado(): Int? {
        return estadoId_img_estado
    }

    fun da_estadoNome_img_estado(): String? {
        return estadoNome_img_estado
    }

    fun atr_estadoId_img_estado(id: Int?) {
        // You can decide whether to allow or disallow changing the value
        this.estadoId_img_estado = id
    }

    fun atr_estadoNome_img_estado(nome: String?) {
        // You can decide whether to allow or disallow changing the value
        this.estadoNome_img_estado = nome
    }

}