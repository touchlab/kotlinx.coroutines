package kotlinx.coroutines

/**
 * Call on an object which should never be frozen. Will help debug when something inadvertently is.
 */
internal expect fun Any.ensureNeverFrozen()