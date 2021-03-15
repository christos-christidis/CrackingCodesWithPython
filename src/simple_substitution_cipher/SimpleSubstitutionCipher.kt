package simple_substitution_cipher

import common.UPPERCASE_LETTERS

object SimpleSubstitutionCipher {

    @Suppress("LiftReturnOrAssignment")
    fun translate(message: String, key: String, mode: String): String {
        val initialChars = if (mode == "encrypt") UPPERCASE_LETTERS else key
        val finalChars = if (mode == "encrypt") key else UPPERCASE_LETTERS

        var translatedText = ""
        for (symbol in message) {
            if (symbol.toUpperCase() in initialChars) {
                val symbolIndex = initialChars.indexOf(symbol.toUpperCase())
                if (symbol.isUpperCase()) {
                    translatedText += finalChars[symbolIndex]
                } else {
                    translatedText += finalChars[symbolIndex].toLowerCase()
                }
            } else {
                translatedText += symbol
            }
        }
        return translatedText
    }

    fun getRandomKey(): String {
        return UPPERCASE_LETTERS.toList().shuffled().joinToString("")
    }
}
