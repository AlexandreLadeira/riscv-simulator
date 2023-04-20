package extensions

import kotlin.reflect.KClass

fun <T : Enum<*>> KClass<T>.paddedName() = this.simpleName?.padEnd(8, ' ')
