package m.monad

sealed class Option<out T> {

    fun <T2> map(mapper: (T) -> T2): Option<T2> {
        return when (this) {
            is Some<T> -> {
                some(mapper(value))
            }
            None -> none()
        }
    }

    fun <T2> flatMap(binding: (T) -> Option<T2>): Option<T2> {
        return when (this) {
            is Some<T> -> binding(this.value)
            None -> none()
        }
    }

    fun ifPresent(block: (T) -> Unit): Unit {
        when (this) {
            is Some<T> -> block(value)
            None -> Unit
        }
    }

    companion object {
        fun <TT> some(value: TT): Option<TT> {
            return Some(value)
        }

        fun <TT> none(): Option<TT> {
            return None
        }
    }
}

class Some<T>internal constructor(val value: T) : Option<T>()
object None : Option<Nothing>()
