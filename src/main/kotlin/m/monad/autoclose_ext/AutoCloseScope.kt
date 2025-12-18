package m.monad.autoclose_ext

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Stack

class AutoCloseScope() {
    val savedToClose: Stack<AutoCloseable> = Stack()

    var autoCloseLogger: Logger? = null

    fun <T : AutoCloseable> install(value: T): T {
        savedToClose.push(value)
        return value
    }

    fun installLogger(logger: Logger) {
        this.autoCloseLogger = logger
    }


    companion object {
        val logger: Logger = LoggerFactory.getLogger("auto-close")

        inline fun <T> autoClose(block: AutoCloseScope.() -> T): T {
            val scope = AutoCloseScope()
            try {
                return scope.block()
            } finally {
                val stack = scope.savedToClose
                while (stack.isNotEmpty()) {
                    val toClose = stack.pop()
                    try {
                        toClose.close()
                    } catch (e: Exception) {
                        val loggerToLog = scope.autoCloseLogger ?: logger
                        loggerToLog.error("err while auto close", e)
                    }
                }
            }
        }

    }
}