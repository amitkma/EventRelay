package com.amitkma.eventrelay

/**
 * Observer for handling events
 */
interface EventObserver<T: Event> {

    /**
     * Accepts a given value by the relay and pass it to the observer implementation.
     */
    fun accept(event: Event)

    /**
     * Complete call to do any post processing after event is being transmitted.
     * Can be used for any cleanup if needed.
     */
    fun complete()
}