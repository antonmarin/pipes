package ru.antonmarin.autoget.framework

import ru.antonmarin.autoget.server.controller.TimerController
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object Reflection {
    fun <T> getPrivatePropertyValue(`object`: Any, propertyName: String): T {
        val property = TimerController::class.memberProperties.first { it.name == propertyName }.apply { isAccessible = true }
        @Suppress("UNCHECKED_CAST")
        return property.get(`object` as TimerController) as T
    }
}
