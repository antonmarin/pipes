package ru.antonmarin.autoget.framework

import org.testcontainers.containers.GenericContainer
import java.net.URL

/**
 * now rename only torrent file
 */
interface TransmissionDependent {
    companion object {
        private val server = GenericContainer("linuxserver/transmission:4.0.5")
        private const val TRANSMISSION_RPC_PORT = 9091
        private const val PEERPORT = 51413

        fun getApiUrl(): URL {
            server.also {
                it.stop()
                it
                    .withEnv("PEERPORT", "$PEERPORT")
                    .withEnv("CURL_CA_BUNDLE", "/etc/ssl/certs/ca-certificates.crt")
                    .withExposedPorts(TRANSMISSION_RPC_PORT, PEERPORT)
                    .start()
            }
            @Suppress("HttpUrlsUsage")
            return URL("http://${server.host}:${server.getMappedPort(TRANSMISSION_RPC_PORT)}/transmission/rpc")
        }
    }
}
