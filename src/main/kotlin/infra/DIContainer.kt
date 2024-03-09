package ru.antonmarin.autoget.infra

import kotlin.reflect.KClassifier

class DIContainer {
    private val registry = mutableMapOf<KClassifier, Any>()

    fun <T : Any> getObject(kClass: KClassifier): T {
        return findObject(kClass) ?: throw NotRegisteredException("$kClass not registered")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> findObject(kClass: KClassifier) = registry[kClass] as? T
    fun register(kClass: KClassifier, expectedObject: Any) {
        registry[kClass] = expectedObject
    }
}
class NotRegisteredException(message: String) : Exception(message)
