package cat.copernic.pdiaza.recolifter.models

import cat.copernic.pdiaza.recolifter.utils.AppConstants


class Language {

    // Initialization
    companion object {
        val instance = Language()
    }

    lateinit var deviceLanguage: LanguageType

    enum class LanguageType(val code: String) {
        ES(AppConstants.SPANISH_LANGUAGE),
        EN(AppConstants.ENGLISH_LANGUAGE),
        CAT(AppConstants.CATALAN_LANGUAGE)
    }


    fun checkDeviceLanguage(language:String){

        this.deviceLanguage = when(language){
            LanguageType.ES.code -> LanguageType.ES
            LanguageType.EN.code -> LanguageType.EN
            LanguageType.CAT.code -> LanguageType.CAT
            else -> LanguageType.EN
        }
    }
}