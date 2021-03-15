package transposition_cipher

import kotlin.math.ceil

object TranspositionCipher {

    fun encrypt(message: String, key: Int): String {
        val cipherColumns = MutableList(key) { "" }

        for (column in 0 until key) {
            var currentIndex = column
            while (currentIndex < message.length) {
                cipherColumns[column] += message[currentIndex].toString()
                currentIndex += key
            }
        }

        return cipherColumns.joinToString("")
    }

    fun decrypt(cipherText: String, key: Int): String {
        val numColumns = ceil(cipherText.length / key.toDouble()).toInt()
        val numRows = key
        val numShadedBoxes = numColumns * numRows - cipherText.length

        val plaintextColumns = MutableList(numColumns) { "" }

        var row = 0
        var column = 0

        for (symbol in cipherText) {
            plaintextColumns[column] += symbol.toString()
            column += 1

            if (column == numColumns || (column == numColumns - 1 && row >= numRows - numShadedBoxes)) {
                column = 0
                row += 1
            }
        }

        return plaintextColumns.joinToString("")
    }
}