package frontend

import backend.Configuration

object LanguageTranslator {

    var language: String? = null

    fun get(text: String): String {
        language = Configuration.get("language")

        if(language == "en") {
            if(text == "loading.config") {
                return "Loading config..."
            }else if(text == "loading.fonts") {
                return "Registering fonts..."
            }else if(text == "loading.cpu") {
                return "Searching for CPU..."
            }else if(text == "loading.gpu") {
                return "Searching for GPU..."
            }else if(text == "loading.finished") {
                return "Finished!"
            }
        }else if(language == "de") {
            if(text == "loading.config") {
                return "Lade Konfiguration..."
            }else if(text == "loading.fonts") {
                return "Registriere Schriftarten..."
            }else if(text == "loading.cpu") {
                return "Suche nach CPU..."
            }else if(text == "loading.gpu") {
                return "Suche nach GPU..."
            }else if(text == "loading.finished") {
                return "Beendet!"
            }
        }
        return "\"$text\" with language \"$language\" not found!"
    }

}