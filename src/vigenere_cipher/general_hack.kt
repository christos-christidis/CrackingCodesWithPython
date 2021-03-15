package vigenere_cipher

import common.DetectLanguage
import common.MESSAGE
import common.SYMBOLS
import common.firstLine
import frequency_analysis.FrequencyAnalysis

fun main() {
    val maxKeyLength = 16   // search up to this length
    val humLettersToTry = 4 // attempt this many letters per subkey

    val cipherText = VigenereCipher.translate(MESSAGE, "Asimov", "encrypt")

    val allLikelyKeyLengths = kasiskiExamination(cipherText, maxKeyLength)
    println("\nThe most likely key lengths in order of likelihood are: $allLikelyKeyLengths\n")

    var foundKey = false

    for (keyLength in allLikelyKeyLengths) {
        if (attemptHackWithKeyLength(cipherText, keyLength, humLettersToTry)) {
            foundKey = true
            break
        }
    }

    if (!foundKey) {
        println("\nFailed to hack encryption")
    }
}

@Suppress("SameParameterValue")
private fun kasiskiExamination(cipherText: String, maxKeyLength: Int): List<Int> {
    val repeatSeqSpacings = findRepeatSequenceSpacings(cipherText)

    val repeatSeqFactors = mutableMapOf<String, MutableList<Int>>()
    for (seq in repeatSeqSpacings.keys) {
        repeatSeqFactors[seq] = mutableListOf()
        for (spacing in repeatSeqSpacings.getValue(seq))
            repeatSeqFactors[seq]!!.addAll(getUsefulFactors(spacing, maxKeyLength))
    }

    val allFactorsWithFreq = repeatSeqFactors.flatMap { it.value }.groupBy { it }.map { it.key to it.value.size }
        .sortedByDescending { it.second }
    return allFactorsWithFreq.map { it.first }
}

// find 3- to 5-letter sequences that are repeated.
private fun findRepeatSequenceSpacings(cipherText: String): Map<String, List<Int>> {
    val seqSpacings = mutableMapOf<String, MutableList<Int>>()

    for (seqLength in 3..5) {
        for (seqStart in 0..cipherText.length - seqLength) {
            val seq = cipherText.slice(seqStart until seqStart + seqLength)

            // look for this sequence in all subsequent cipherText
            for (idx in seqStart + seqLength..cipherText.length - seqLength)
                if (cipherText.slice(idx until idx + seqLength) == seq) {
                    seqSpacings.getOrPut(seq) { mutableListOf() }
                        .add(idx - seqStart)
                }
        }
    }

    return seqSpacings
}

// returns those factors that are > 1 and < KEY_LENGTH + 1
private fun getUsefulFactors(num: Int, maxKeyLength: Int): Set<Int> {
    if (num < 2) {
        return emptySet()
    }

    val factors = mutableSetOf<Int>()
    for (i in 2..maxKeyLength) {
        if (num % i == 0) {
            factors.add(i)
        }
    }

    return factors
}

fun attemptHackWithKeyLength(cipherText: String, keyLength: Int, numLettersToTry: Int): Boolean {
    val text = cipherText.mapNotNull { if (it in SYMBOLS) it else null }.joinToString("")

    val allCharsToTry = mutableListOf<String>()
    for (start in 0 until keyLength) {
        val subKeyLetters = text.slice(start until text.length step keyLength)

        val charsWithScore = mutableListOf<Pair<Char, Int>>()
        for (possibleKey in SYMBOLS) {
            val plainText = VigenereCipher.translate(subKeyLetters, possibleKey.toString(), "decrypt")
            val score = FrequencyAnalysis.getEnglishMatchScore(plainText)
            charsWithScore.add(Pair(possibleKey, score))
        }
        val mostPromisingCharsWithFreq = charsWithScore.sortedByDescending { it.second }.take(numLettersToTry)
        val charsToTryForThisSubkey = mostPromisingCharsWithFreq.map { it.first }.joinToString("")
        allCharsToTry.add(charsToTryForThisSubkey)
    }

    println("Trying most promising $keyLength-letter keys...")
    val possibleKeys = allCharsToTry.combinations()

    for (key in possibleKeys) {
        val plainText = VigenereCipher.translate(cipherText, key, "decrypt")
        // must up the percentage to 70 cause I get a lot of false positives
        if (DetectLanguage.isEnglish(plainText, minWordPercentage = 70)) {
            println("\nPromising decryption key: $key")
            println("text = ${plainText.firstLine()}...")
            print("\nEnter d if done, anything else to continue: ")
            if (readLine()!!.startsWith("d")) {
                println("\nDecrypted text:")
                print(plainText)
                return true
            }
        }
    }

    return false
}

private fun List<String>.combinations(): List<String> {
    if (this.size < 2) {
        return this
    }

    var stringsSoFar = this.first().map { it.toString() }
    var remainingSubKeys = this.drop(1)
    var combinedResult: MutableList<String>
    while (remainingSubKeys.isNotEmpty()) {
        combinedResult = mutableListOf()
        val s2 = remainingSubKeys.first()
        remainingSubKeys = remainingSubKeys.drop(1)
        for (s in stringsSoFar) {
            for (y in s2) {
                combinedResult.add(s + y.toString())
            }
        }
        stringsSoFar = combinedResult
    }

    return stringsSoFar
}
