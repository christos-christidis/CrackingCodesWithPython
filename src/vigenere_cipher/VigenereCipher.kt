package vigenere_cipher

import caesar_cipher.CaesarCipher
import common.SYMBOLS

object VigenereCipher {

    fun translate(message: String, key: String, mode: String): String {
        var translatedText = ""

        var keyIndex = 0
        for (symbol in message) {
            if (symbol !in SYMBOLS) {
                translatedText += symbol
                continue
            }

            val currentKey = SYMBOLS.indexOf(key[keyIndex])

            val cipherChar = if (mode == "encrypt")
                CaesarCipher.encrypt(symbol.toString(), currentKey) else
                CaesarCipher.decrypt(symbol.toString(), currentKey)

            translatedText += cipherChar

            keyIndex = (keyIndex + 1) % key.length
        }

        return translatedText
    }
}
