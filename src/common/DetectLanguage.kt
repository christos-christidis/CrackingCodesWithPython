package common

import java.io.File

object DetectLanguage {

    private val LETTERS_AND_SPACE = "$LOWERCASE_LETTERS$UPPERCASE_LETTERS \t\n"
    private val englishWords = File(DICTIONARY_FILE).readLines().toSet()

    // 20% of words must exist in the dictionary and 85% of chars must be letters or spaces
    fun isEnglish(message: String, minWordPercentage: Int = 20, minLetterPercentage: Int = 85): Boolean {
        val englishWords = getEnglishWordPercentage(message.toUpperCase())
        val englishLetters = removeNonLetters(message).length / message.length.toDouble() * 100
        return englishWords >= minWordPercentage && englishLetters >= minLetterPercentage
    }

    private fun getEnglishWordPercentage(message: String): Double {
        val lettersOnly = removeNonLetters(message)
        val possibleWords = lettersOnly.split(' ', '\t', '\n')
        val numMatches = possibleWords.count { it in englishWords }
        return numMatches / possibleWords.size.toDouble() * 100
    }

    private fun removeNonLetters(message: String): String {
        val lettersOnly = message.mapNotNull { if (it in LETTERS_AND_SPACE) it else null }
        return lettersOnly.joinToString("")
    }
}
