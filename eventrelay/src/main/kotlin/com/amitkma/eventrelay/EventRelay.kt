@file:Suppress("UNCHECKED_CAST")

package com.amitkma.eventrelay

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * A simple Helper class which can be used to relay the events across different classes (components)
 * while maintaining the order of the events. Events are processed on FIFO basis.
 * This is not a very concrete implementation and it's singleton only as of now. Heavy work is needed.
 * It can be subscribed or unsubscribed from anywhere in the program.
 */
object EventRelay {

    // Queue to store actions if no observer has currently subscribed to this Relay.
    private val events: Queue<Event>
    private var observer: EventObserver<out Event>? = null

    init {
        events = ConcurrentLinkedQueue<Event>()
    }

    /**
     * Push the event to the queue or to the subscriber if it's not null.
     * @param event Event to be pushed on the relay.
     */
    @Synchronized
    fun <T: Event> push(event: T) {
        if (observer != null) {
            observer!!.accept(event)
            if (event !is NonCompletableEvent) {
                observer!!.complete()
            }
        } else {
            this.events.offer(event)
        }
    }

    /**
     * Subscribes a given instance of EventRelay by the observer.
     * Emit events as soon as someone subscribes to this relay.
     *
     * @param observer EventObserver which keeps listening to the event.
     */
    @Synchronized
    fun <T: Event> subscribe(observer: EventObserver<out T>) {
        this.observer = observer
        var event: T? = events.poll() as T?
        var shouldInvokeComplete = false

        // Pass all the events to the observer
        while (event != null) {
            shouldInvokeComplete = shouldInvokeComplete or (event !is NonCompletableEvent)
            this.observer!!.accept(event)
            event = events.poll() as T?
        }

        if (shouldInvokeComplete) this.observer!!.complete()
    }


    /**
     * Unsubscribe the relay to prevent further relaying of events.
     * NOTE: // This must be called if observer is an instance of Android Context (Activity, Application or Service)
     */
    @Synchronized
    fun unsubscribe() {
        if (this.observer != null) {
            observer = null
        }
    }
}
