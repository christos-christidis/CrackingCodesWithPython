package affine_cipher

import common.CryptoMath
import common.SYMBOLS
import kotlin.random.Random

object AffineCipher {

    @Suppress("LiftReturnOrAssignment")
    fun encrypt(message: String, key: Int): String {
        val (keyA, keyB) = getKeyParts(key)
        var cipherText = ""
        for (symbol in message) {
            if (symbol in SYMBOLS) {
                val symbolIndex = SYMBOLS.indexOf(symbol)
                val cipherIndex = (symbolIndex * keyA + keyB) % SYMBOLS.length
                cipherText += SYMBOLS[cipherIndex]
            } else {
                cipherText += symbol
            }
        }
        return cipherText
    }

    fun decrypt(cipherText: String, key: Int): String {
        val (keyA, keyB) = getKeyParts(key)
        val modInverseOfKeyA = CryptoMath.modInverse(keyA, SYMBOLS.length)

        var plainText = ""
        for (symbol in cipherText) {
            if (symbol in SYMBOLS) {
                val symbolIndex = SYMBOLS.indexOf(symbol)
                var originalIndex = (symbolIndex - keyB) * modInverseOfKeyA % SYMBOLS.length

                // read findModInverse() to see why this correction is needed
                if (originalIndex < 0) {
                    originalIndex += SYMBOLS.length
                }
                plainText += SYMBOLS[originalIndex]
            } else {
                plainText += symbol
            }
        }
        return plainText
    }

    // I can use this function to generate a random affine key for any text
    fun getRandomAffineKey(): Int {
        while (true) {
            val keyA = Random.nextInt(2, SYMBOLS.length)
            val keyB = Random.nextInt(1, SYMBOLS.length)
            if (CryptoMath.gcd(keyA, SYMBOLS.length) == 1) {    // these numbers must be co-prime
                return keyA * SYMBOLS.length + keyB
            }
        }
    }

    // we split a single integer into 2 keys (multiplicative and caesar) cause it's easier to remember one key
    fun getKeyParts(key: Int): Pair<Int, Int> {
        return Pair(key / SYMBOLS.length, key % SYMBOLS.length)
    }
}

