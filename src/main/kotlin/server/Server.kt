package ru.antonmarin.autoget.server

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.Pipeline

class Server(val items: List<ServerItem>) {
    fun start() {
        items.forEach {
            logger.trace("Starting pipeline with controller {}", it.controller::class.java)
            it.controller.execute(it.pipeline)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Server::class.java)
    }
}

class ServerItem(
    val controller: Controller,
    val pipeline: Pipeline,
)

interface Controller {
    fun execute(pipeline: Pipeline)
}
