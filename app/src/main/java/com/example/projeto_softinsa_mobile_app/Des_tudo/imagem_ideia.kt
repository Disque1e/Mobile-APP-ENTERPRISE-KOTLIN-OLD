package com.example.projeto_softinsa_mobile_app.Des_tudo

class imagem_ideia {
    var titulo_img_ideia: String? = null
    var tipo_img_tipo_ideia: String? = null
    var tipo_img_User_ideia: Int? = null
    var tipo_img_Desc_ideia: String? = null

    fun da_titulo_ideia(): String {
        return titulo_img_ideia.toString()
    }

    fun da_tipo_ideia(): String {
        return tipo_img_tipo_ideia.toString()
    }

    fun da_User_ideia(): Int? {
        return tipo_img_User_ideia
    }

    fun da_Desc_ideia(): String {
        return tipo_img_Desc_ideia.toString()
    }

}