package caesar_cipher

import common.SYMBOLS

object CaesarCipher {

    fun encrypt(message: String, key: Int): String {
        var cipherText = ""
        for (symbol in message) {
            if (symbol !in SYMBOLS) {
                cipherText += symbol
                continue
            }

            val symbolIndex = SYMBOLS.indexOf(symbol)

            var cipherIndex = symbolIndex + key

            if (cipherIndex >= SYMBOLS.length) {
                cipherIndex -= SYMBOLS.length
            }

            cipherText += SYMBOLS[cipherIndex]
        }

        return cipherText
    }

    fun decrypt(cipherText: String, key: Int): String {
        var plainText = ""
        for (symbol in cipherText) {
            if (symbol !in SYMBOLS) {
                plainText += symbol
                continue
            }

            val symbolIndex = SYMBOLS.indexOf(symbol)

            var plainTextIndex = symbolIndex - key

            if (plainTextIndex < 0) {
                plainTextIndex += SYMBOLS.length
            }

            plainText += SYMBOLS[plainTextIndex]
        }

        return plainText
    }
}