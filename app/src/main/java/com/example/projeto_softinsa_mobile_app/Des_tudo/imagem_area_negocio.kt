package com.example.negocio_softinsa_mobile_app.Des_tudo

class imagem_area_negocio{
    var areaNegocioId_img_area_negocio: Int?  = null
    var areaNegocioNome_img_area_negocio: String?  = null

    fun da_areaNegocioId_area_negocio(): Int? {
        return areaNegocioId_img_area_negocio
    }

    fun da_areaNegocioNome_area_negocio(): String? {
        return areaNegocioNome_img_area_negocio
    }

    fun atr_areaNegocioId_area_negocio(id: Int?) {
        // You can decide whether to allow or disallow changing the value
        this.areaNegocioId_img_area_negocio = id
    }

    fun atr_areaNegocioNome_area_negocio(nome: String?) {
        // You can decide whether to allow or disallow changing the value
        this.areaNegocioNome_img_area_negocio = nome
    }

}