package com.example.projeto_softinsa_mobile_app.Des_tudo

import android.provider.ContactsContract.Data

class imagem_lista_user {
    var Id_img_lista_user: Int? = null
    var Nome_img_lista_user: String? = null
    var Sobrenome_img_lista_user: String? = null
    var Num_Func_img_lista_user: Int? = null
    var Cargo_img_lista_user: Int? = null
    var Email_img_lista_user: String? = null
    var Telemovel_img_lista_user: String? = null
    var Morada_img_lista_user: String? = null
    var DataRegisto_img_lista_user: String? = null
    var isColaborador_img_lista_user: Boolean = false
    var isCandidato_img_lista_user: Boolean = false
    var isAtivo_img_lista_user: Boolean = false
    var Estado_img_lista_user: String? = null
    var Departamento_img_lista_user: Int? = null
    var Filial_img_lista_user: Int? = null

    fun da_Id_img_lista_user(): Int{
        return Id_img_lista_user ?: 0
    }

    fun da_Nome_lista_user(): String {
        return Nome_img_lista_user.toString()
    }

    fun da_Sobrenome_lista_user(): String {
        return Sobrenome_img_lista_user.toString()
    }

    fun da_Num_Func_lista_user(): Int {
        return Num_Func_img_lista_user ?: 0
    }

    fun da_Cargo_lista_user(): Int {
        return Cargo_img_lista_user ?: 0
    }

    fun da_Email_lista_user(): String {
        return Email_img_lista_user.toString()

    }

    fun da_Telemovel_lista_user(): String {
        return Telemovel_img_lista_user.toString()

    }

    fun da_Estado_lista_user(): String {
        return Estado_img_lista_user.toString()

    }

    fun da_DataRegisto_lista_user(): String {
        return DataRegisto_img_lista_user.toString()

    }

    fun da_isColaborador_lista_user(): Boolean {
        return isColaborador_img_lista_user

    }

    fun da_isCandidato_lista_user(): Boolean {
        return isCandidato_img_lista_user

    }

    fun da_isAtivo_lista_user(): Boolean {
        return isAtivo_img_lista_user

    }

    fun da_Morada_lista_user(): String {
        return Morada_img_lista_user.toString()
    }

    fun da_Departamento_lista_user(): Int {
        return Departamento_img_lista_user?: 0
    }

    fun da_Filial_lista_user(): Int {
        return Filial_img_lista_user?: 0
    }

    //-----------------------------------------------------------


    fun atr_Id_img_lista_user(value: Int?) {
        this.Id_img_lista_user = value
    }

    fun atr_Nome_lista_user(name: String) {
        this.Nome_img_lista_user = name
    }

    fun atr_Sobrenome_lista_user(name: String) {
        this.Sobrenome_img_lista_user = name
    }

       fun atr_Num_Func_lista_user(value: Int?) {
           this.Num_Func_img_lista_user = value
       }

    fun atr_Cargo_lista_user(value: Int?) {
        this.Cargo_img_lista_user = value
    }

    fun atr_Email_lista_user(name: String) {
       this.Email_img_lista_user = name
    }

    fun atr_Telemovel_lista_user(name: String) {
        this.Telemovel_img_lista_user = name
    }

    fun atr_Estado_lista_user(name: String) {
        this.Estado_img_lista_user = name
    }

    fun atr_DataRegisto_lista_user(name: String) {
        this.DataRegisto_img_lista_user = name
    }

    fun atr_isColaborador_lista_user(name: Boolean) {
        this.isColaborador_img_lista_user = name
    }

    fun atr_isCandidato_lista_user(name: Boolean) {
        this.isCandidato_img_lista_user = name
    }

    fun atr_isAtivo_lista_user(name: Boolean) {
        this.isAtivo_img_lista_user = name
    }

    fun atr_Morada_lista_user(name: String) {
        this.Morada_img_lista_user = name
    }

    fun atr_Departamento_lista_user(name: Int) {
        this.Departamento_img_lista_user = name
    }

    fun atr_Filial_lista_user(name: Int) {
        this.Filial_img_lista_user = name
    }
}