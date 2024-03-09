package ru.antonmarin.autoget.server.configuration

import java.io.InputStream

class ConfigReader {
    fun read(@Suppress("UNUSED_PARAMETER") input: InputStream): ServerConfig = ServerConfig(emptyList())
}
