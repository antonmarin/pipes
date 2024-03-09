package ru.antonmarin.autoget.server.controller

import ru.antonmarin.autoget.Pipeline
import ru.antonmarin.autoget.server.Controller

class OnceController : Controller {
    override fun execute(pipeline: Pipeline) {
        pipeline.execute()
    }
}
