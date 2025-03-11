package ru.antonmarin.autoget.actions.contracts

import java.net.URL
import kotlin.jvm.Throws
import kotlin.reflect.KClass

interface XmlReader{
    // refactor to Either
    @Throws(ResponseNotSuccess::class)
    fun <ResponseType : Any> read(url: URL, className: KClass<ResponseType>): ResponseType
    class ResponseNotSuccess(message: String) : Exception(message)
}
