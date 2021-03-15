package frequency_analysis

import common.UPPERCASE_LETTERS
import common.inverted

object FrequencyAnalysis {

    private const val ETAOIN = "ETAOINSHRDLCUMWFGYPBVKJXQZ"

    private fun getLetterCount(text: String): Map<Char, Int> {
        val map = mutableMapOf<Char, Int>()
        for (char in UPPERCASE_LETTERS) {
            map[char] = 0
        }

        for (char in text.toUpperCase()) {
            if (char in UPPERCASE_LETTERS) {
                map[char] = map.getValue(char) + 1
            }
        }

        return map
    }

    private fun getFrequencyOrder(text: String): String {
        val lettersToFreq = getLetterCount(text)
        val freqToLetters = lettersToFreq.inverted()

        // we want the letters with the same freq ordered from least to most common
        // so that we don't get a high frequency match by some fluke
        for ((_, list) in freqToLetters) {
            list.sortByDescending { ETAOIN.indexOf(it) }
        }

        // now we simply flatten the contents and produce the frequency order string
        return freqToLetters.toList().sortedByDescending { it.first }.map { it.second }.flatten().joinToString("")
    }

    fun getEnglishMatchScore(text: String): Int {
        val freqOrder = getFrequencyOrder(text)
        var score = 0

        for (char in ETAOIN.take(6)) {
            if (char in freqOrder.take(6)) {
                score += 1
            }
        }

        for (char in ETAOIN.takeLast(6)) {
            if (char in freqOrder.takeLast(6)) {
                score += 1
            }
        }

        return score
    }
}