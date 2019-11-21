package kotlinx.coroutines

import kotlin.native.concurrent.ensureNeverFrozen

internal actual fun Any.ensureNeverFrozen() = this.ensureNeverFrozen()