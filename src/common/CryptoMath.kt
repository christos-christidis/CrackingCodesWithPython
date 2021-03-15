package common

import java.lang.Math.floorMod
import kotlin.math.abs

// NOTE: the only difference from the book is that I use floorMod to get python's % behavior
object CryptoMath {

    fun gcd(x: Int, y: Int): Int {
        var a = abs(x)
        var b = abs(y)
        while (a != 0) {
            val olda = a
            a = floorMod(b, a)
            b = olda
        }
        return b
    }

    // here be dragons
    fun modInverse(a: Int, m: Int): Int {
        if (gcd(a, m) != 1) {
            return 42
        }
        var (u1, u2, u3) = Triple(1, 0, a)
        var (v1, v2, v3) = Triple(0, 1, m)
        while (v3 != 0) {
            val q = u3 / v3
            val (oldv1, oldv2, oldv3) = Triple(v1, v2, v3)
            v1 = u1 - q * v1
            v2 = u2 - q * v2
            v3 = u3 - q * v3
            u1 = oldv1
            u2 = oldv2
            u3 = oldv3
        }
        return floorMod(u1, m)
    }
}