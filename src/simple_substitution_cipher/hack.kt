package simple_substitution_cipher

import common.DICTIONARY_FILE
import common.UPPERCASE_LETTERS
import common.firstLine
import java.io.File

fun main() {
    // I use this text instead of Common.MESSAGE cause the latter can't be resolved satisfactorily; we end up with too
    // many blanks which shows that a longer text doesn't necessarily work better.
    // Also the code must not change spaces cause then I lose the ability to recognize cipherWords.
    val message = """If a man is offered a fact which goes against his instincts, he will scrutinize
        |it closely, and unless the evidence is overwhelming, he will refuse to believe
        |it. If, on the other hand, he is offered something which affords a reason for
        |acting in accordance to his instincts, he will accept it even on the slightest
        |evidence. The origin of myths is explained in this way.
        |-Bertrand Russell
        |""".trimMargin()

    val cipherText = SimpleSubstitutionCipher.translate(message, SimpleSubstitutionCipher.getRandomKey(), "encrypt")

    val wordPatterns = getDictionaryWordPatterns()

    val globalLetterMappings = getFullMappings()

    val cipherWords = cipherText.toUpperCase().split("[^A-Z]".toRegex()).mapNotNull { if (it.isEmpty()) null else it }

    for (cipherWord in cipherWords) {
        val cipherWordMappings = mutableMapOf<Char, MutableSet<Char>>()
        val pattern = getWordPattern(cipherWord)
        for (candidateWord in wordPatterns.getOrDefault(pattern, emptyList())) {
            for (index in cipherWord.indices) {
                cipherWordMappings.getOrPut(cipherWord[index]) { mutableSetOf() } += candidateWord[index]
            }
        }
        for ((k, v) in cipherWordMappings) {
            globalLetterMappings[k] = globalLetterMappings.getValue(k).intersect(v).toMutableSet()
        }
    }

    // if a cipherLetter maps to a single letter, we can remove the latter from all the other mappings. And we can keep
    // doing this until no more removals can be done
    removeSolvedLetters(globalLetterMappings)

    val plainText = decrypt(cipherText, globalLetterMappings)

    println("\nCipherText:")
    println("${cipherText.firstLine()}...")
    println("\nHacking...")
    println("\nPlainText:")
    println(plainText)

    println("Unsolved letters:")
    for ((k, v) in globalLetterMappings) {
        if (v.size > 1 && k in cipherText.toUpperCase()) {
            println("$k can be mapped to ${globalLetterMappings[k]}")
        }
    }
}

private fun getDictionaryWordPatterns(): Map<String, List<String>> {
    val patterns = mutableMapOf<String, MutableList<String>>()

    val words = File(DICTIONARY_FILE).readLines()

    for (word in words) {
        val pattern = getWordPattern(word)

        if (pattern !in patterns) {
            patterns[pattern] = mutableListOf()
        }
        patterns[pattern]?.add(word)
    }

    return patterns
}

private fun getWordPattern(word: String): String {
    var num = 0
    val dict = mutableMapOf<Char, Int>()
    var pattern = ""
    for ((index, char) in word.withIndex()) {
        if (char !in dict) {
            dict[char] = num
            num += 1
        }
        if (index != 0) {
            pattern += '.'
        }
        pattern += dict[char]
    }
    return pattern
}

fun getFullMappings(): MutableMap<Char, MutableSet<Char>> {
    val map = mutableMapOf<Char, MutableSet<Char>>()
    for (char in UPPERCASE_LETTERS) {
        map[char] = UPPERCASE_LETTERS.toList().toMutableSet()
    }
    return map
}

private fun removeSolvedLetters(letterMappings: Map<Char, MutableSet<Char>>) {
    var somethingChanged = true
    while (somethingChanged) {
        somethingChanged = false
        for ((_, v) in letterMappings) {
            if (v.size == 1) {
                val singleLetter = v.first()
                for ((_, v2) in letterMappings) {
                    if (v2.size > 1 && singleLetter in v2) {
                        v2.remove(singleLetter)
                        somethingChanged = true
                    }
                }
            }
        }
    }
}

@Suppress("LiftReturnOrAssignment")
private fun decrypt(message: String, letterMappings: Map<Char, Set<Char>>): String {
    var plainText = ""
    for (symbol in message) {
        if (symbol.toUpperCase() !in UPPERCASE_LETTERS) {
            plainText += symbol
            continue
        }

        val possibleLetters = letterMappings[symbol.toUpperCase()]
        if (possibleLetters?.size == 1) {
            plainText += if (symbol.isUpperCase())
                possibleLetters.first() else
                possibleLetters.first().toLowerCase()
        } else {
            plainText += '_'
        }
    }
    return plainText
}
