package com.amitkma.eventrelay

import android.text.format.DateUtils
import java.util.*

/**
 * Simple helper class which mocks a download service
 */
class DownloadService {

    private var keepGoing = true

    fun start() {
        keepGoing = true
        Thread(Runnable {
            while (keepGoing) {
                Thread.sleep(2000)
                EventRelay.push(DownloadEvent(Date(System.currentTimeMillis())))
            }

        }).start()
    }

    fun stop() {
        keepGoing = false
    }

}