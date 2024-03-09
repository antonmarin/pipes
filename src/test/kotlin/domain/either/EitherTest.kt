package ru.antonmarin.autoget.domain.either

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import ru.antonmarin.autoget.domain.Either

class EitherTest {
    @Nested
    inner class CatchTest {
        @Test
        fun `should return when thrown`() {
            Assertions.assertThatCode {
                val either = Either.catch {
                    @Suppress("ConstantConditionIf")
                    if (true) {
                        throw IllegalArgumentException("should be returned instead")
                    }
                    true
                }

                Assertions.assertThat(either).isInstanceOf(Either.Left::class.java)
                val left = either as Either.Left
                Assertions.assertThat(left.value).isInstanceOf(IllegalArgumentException::class.java)
            }.doesNotThrowAnyException()
        }
    }

    @Nested
    inner class MapLeft {
        @Test
        fun `should return right value when right`() {
            val expectedValue = 12
            val either = Either.Right(expectedValue) as Either<IllegalArgumentException, Int>

            val result = either.mapLeft {
                fail("Unexpected behavior")
            }.fold({ fail("Unexpected behavior") }, { it })

            Assertions.assertThat(result).isEqualTo(expectedValue)
        }

        @Test
        fun `should return lambda result when left`() {
            val either = Either.Left(TestingError.SOME_1) as Either<TestingError, Unit>

            val result = either.mapLeft {
                when (it) {
                    TestingError.SOME_1 -> TestingError.SOME_2
                    TestingError.SOME_2 -> fail("Unexpected behavior")
                }
            }.fold({ it }, { fail("Unexpected behavior") })

            Assertions.assertThat(result).isEqualTo(TestingError.SOME_2)
        }
    }

    enum class TestingError {
        SOME_1,
        SOME_2,
    }

}
