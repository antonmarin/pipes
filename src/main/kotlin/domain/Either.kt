package ru.antonmarin.autoget.domain

sealed class Either<out A, out B> {
    fun <C> fold(ifLeft: (left: A) -> C, ifRight: (right: B) -> C): C = when (this) {
        is Left -> ifLeft(value)
        is Right -> ifRight(value)
    }

    fun <C> mapLeft(leftMapper: (A) -> C): Either<C, B> = fold({ Left(leftMapper(it)) }, { Right(it) })

    class Left<out A>(val value: A) : Either<A, Nothing>() {
        override fun toString(): String = "Either.Left($value)"
    }

    class Right<out B>(val value: B) : Either<Nothing, B>() {
        override fun toString(): String = "Either.Right($value)"
    }

    companion object {
        inline fun <R> catch(block: () -> R): Either<Throwable, R> = try {
            Right(block())
        } catch (e: Throwable) {
            Left(e)
        }
    }
}
