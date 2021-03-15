package affine_cipher

import common.*
import kotlin.math.pow

fun main() {
    val cipherText = AffineCipher.encrypt(MESSAGE, AffineCipher.getRandomAffineKey())

    var foundKey = false

    // we know there are ~SYMBOLS.length keys for keyA and the same for keyB so... we try them all.
    // I could also remove some keys that can't be used but whatever
    val lastKey = SYMBOLS.length.toDouble().pow(2.0).toInt()
    println("\nTrying keys 0 to $lastKey...")

    for (key in 0..lastKey) {
        val keyA = AffineCipher.getKeyParts(key).first
        if (CryptoMath.gcd(keyA, SYMBOLS.length) != 1) {
            continue
        }

        val plainText = AffineCipher.decrypt(cipherText, key)

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