package transposition_cipher

import common.DetectLanguage
import common.MESSAGE
import common.firstLine

fun main() {
    val cipherText = TranspositionCipher.encrypt(MESSAGE, 13)

    var foundKey = false

    println("\nTrying keys 0 to ${cipherText.length / 2}...")

    for (key in 1..cipherText.length / 2) {
        val plainText = TranspositionCipher.decrypt(cipherText, key)

        if (DetectLanguage.isEnglish(plainText)) {
            println("\nPromising decryption key: $key")
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
