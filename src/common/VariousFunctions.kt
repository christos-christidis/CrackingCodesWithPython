package common

fun String.firstLine(): String {
    val index = this.indexOf('\n')
    return if (index == -1) this else this.slice(0 until index)
}

fun <K, V> Map<K, V>.inverted(): Map<V, MutableList<K>> {
    val newMap = mutableMapOf<V, MutableList<K>>()
    for ((k, v) in this) {
        if (newMap[v] == null) {
            newMap[v] = mutableListOf()
        }
        newMap[v]?.add(k)
    }

    return newMap
}
