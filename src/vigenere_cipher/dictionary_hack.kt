package vigenere_cipher

import common.DICTIONARY_FILE
import common.DetectLanguage
import common.MESSAGE
import common.firstLine
import java.io.File

// This attack works only if the chosen key happens to be a real word. The case matters. so I may have to check multiple
// versions of each word eg foo, FOO, Foo etc
fun main() {
    val cipherText = VigenereCipher.translate(MESSAGE, "ANTIDISESTABLISHMENTARIANISM", "encrypt")

    val words = File(DICTIONARY_FILE).readLines()

    var foundKey = false
    println("\nTrying words in dictionary...")

    for (word in words) {
        val plainText = VigenereCipher.translate(cipherText, word, "decrypt")
        if (DetectLanguage.isEnglish(plainText)) {
            println("\nPromising decryption key: $word")
            println("text = ${plainText.firstLine()}...")
            print("\nEnter d if done, anything else to continue: ")
            if (readLine()!!.startsWith("d")) {
                foundKey = true
                println("\nDecrypted text:")
                print(plainText)
                break
            }
        }
    }

    if (!foundKey) {
        println("\nFailed to hack encryption")
    }
}


