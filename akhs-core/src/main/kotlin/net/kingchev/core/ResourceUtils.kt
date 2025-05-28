package net.kingchev.core

fun getenv(name: String): String? =
    try {
        System.getenv(name)
    } catch (_: NullPointerException) {
        null
    }