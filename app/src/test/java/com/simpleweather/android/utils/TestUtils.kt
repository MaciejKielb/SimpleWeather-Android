package com.simpleweather.android.utils

fun readFileFromResources(fileName: String): String {
    return object {}.javaClass.classLoader?.getResource(fileName)?.readText()
        ?: throw IllegalArgumentException("File not found: $fileName")
}