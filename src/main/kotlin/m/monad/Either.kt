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
}

class Left<T> internal constructor(val value: T) : Either<T, Nothing>()
class Right<T> internal constructor(val value: T) : Either<Nothing, T>()
