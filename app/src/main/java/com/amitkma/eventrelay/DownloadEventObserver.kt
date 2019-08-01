package com.amitkma.eventrelay

class DownloadEventObserver
constructor(private var activity: MainActivity) : EventObserver<DownloadEvent> {
    override fun accept(event: Event) {
        activity.runOnUiThread {
            activity.incrementCount(event as DownloadEvent)
        }
    }

    override fun complete() {

    }
}