package ru.antonmarin.autoget.actions.contracts

import java.net.URL
import kotlin.reflect.KClass

interface XmlReader{
    fun <ResponseType : Any> read(url: URL, className: KClass<ResponseType>): ResponseType
}
