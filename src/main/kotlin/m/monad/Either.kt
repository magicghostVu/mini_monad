package m.monad

sealed class Either<out L, out R> {
    companion object {
        fun <T> left(value: T): Either<T, Nothing> {
            return Left(value)
        }

        fun <T> right(value: T): Either<Nothing, T> {
            return Right(value)
        }
    }

    inline fun <L2, R2> map(lMap: (L) -> L2, rMap: (R) -> R2): Either<L2, R2> {
        return when (this) {
            is Left -> left(lMap(this.value))
            is Right -> right(rMap(this.value))
        }
    }

    inline fun <R2> flatMapRight(binding: (R) -> Either<@UnsafeVariance L, R2>): Either<L, R2> {
        return when (this) {
            is Left -> this
            is Right -> {
                binding(value)
            }
        }
    }

}

data class Left<T> internal constructor(val value: T) : Either<T, Nothing>()
data class Right<T> internal constructor(val value: T) : Either<Nothing, T>()
