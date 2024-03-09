package ru.antonmarin.autoget.server.controller

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.server.Controller
import ru.antonmarin.autoget.Pipeline
import java.time.Clock
import java.time.Duration
import java.util.*

class TimerController(
    private val delayFromStartMilliseconds: Long,
    private val delayBetweenSuccessive: Duration,
    private val clock: Clock,
) : Controller {
    private val timer = Timer()

    override fun execute(pipeline: Pipeline) {
        val task = object : TimerTask() {
            override fun run() {
                pipeline.execute()
                logger.debug("Pipeline execution finished at {}", clock.instant())
            }
        }

        timer.schedule(task, delayFromStartMilliseconds, delayBetweenSuccessive.toMillis())
    }

    fun stop() {
        timer.cancel()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TimerController::class.java)
    }
}
