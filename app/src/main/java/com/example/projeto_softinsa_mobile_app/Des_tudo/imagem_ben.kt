package com.example.projeto_softinsa_mobile_app.Des_tudo

class imagem_ben {

    var titulo_img_ben: String? = null
    var tipo_img_ben: String? = null
    var descricao_img_ben: String? = null
    var imagem_drawable_ben: Int = 0


    fun da_titulo_ben(): String {
        return titulo_img_ben.toString()
    }

    fun da_tipo_ben(): String {
        return tipo_img_ben.toString()
    }

    fun da_descricao_ben(): String {
        return descricao_img_ben.toString()
    }
    //-----------------------------------------------------------
    fun atr_titulo_ben(name: String) {
        this.titulo_img_ben = name
    }
    fun atr_tipo_ben(name: String) {
        this.tipo_img_ben = name
    }
    fun atr_descricao_ben(name: String) {
        this.descricao_img_ben = name
    }

    fun da_drawables_ben(): Int {
        return imagem_drawable_ben
    }

    fun atr_drawables_ben(image_drawable: Int) {
        this.imagem_drawable_ben = image_drawable
    }

}