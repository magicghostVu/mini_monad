package m.monad

sealed class Try<out T> {
    companion object {
        fun <T> of(block: () -> T): Try<T> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Failed(e)
            }
        }

        suspend fun <T> ofSuspend(block: suspend () -> T): Try<T> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Failed(e)
            }
        }

        fun <T> success(value: T): Try<T> {
            return Success(value)
        }

        fun <T> failed(th: Throwable): Try<T> {
            return Failed(th)
        }

        fun <T> sequence(iter: Iterable<Try<T>>): Try<Iterable<T>> {
            val h = iter.fold(of { mutableListOf<T>() }) { acc, current ->
                acc.flatMap {
                    current.map { it2 ->
                        it.add(it2)
                        it
                    }
                }
            }
            return h.map { l -> l.asIterable() }
        }

        fun <T> Try<T>.withDefault(factory: () -> T): T {
            return when (this) {
                is Failed -> factory()
                is Success -> this.value
            }
        }
    }

    fun <T2> map(mapper: (T) -> T2): Try<T2> {
        return when (this) {
            is Success<T> -> {
                of {
                    mapper(value)
                }
            }

            is Failed -> {
                this
            }
        }
    }

    suspend fun <T2> mapSuspend(mapper: suspend (T) -> T2): Try<T2> {
        return when (this) {
            is Success<T> -> {
                ofSuspend {
                    mapper(value)
                }
            }

            is Failed -> {
                this
            }
        }
    }

    fun <T2> flatMap(binding: (T) -> Try<T2>): Try<T2> {
        return when (this) {
            is Success<T> -> {
                binding(value)
            }

            is Failed -> {
                return this
            }
        }
    }

    suspend fun <T2> flatMapSuspend(binding: suspend (T) -> Try<T2>): Try<T2> {
        return when (this) {
            is Success<T> -> {
                binding(value)
            }

            is Failed -> {
                return this
            }
        }
    }
}

class Success<T>(val value: T) : Try<T>()
class Failed(val th: Throwable) : Try<Nothing>()