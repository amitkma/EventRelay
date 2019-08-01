package com.amitkma.eventrelay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.widget.TextView
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView

class MainActivity : AppCompatActivity(){
    private lateinit var downloadService: DownloadService

    private var count = 0
    private lateinit var tickerView: TickerView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tickerView = findViewById(R.id.tickerView)
        tickerView.setCharacterLists(TickerUtils.provideNumberList())
        textView = findViewById(R.id.timeView)
        downloadService = DownloadService()
        downloadService.start()
    }

    override fun onResume() {
        super.onResume()
        EventRelay.subscribe(DownloadEventObserver(this))
    }

    override fun onPause() {
        super.onPause()
        EventRelay.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadService.stop()
    }

    fun incrementCount(downloadEvent: DownloadEvent) {
        count++
        tickerView.setText(count.toString(), true)
        textView.setText("Latest Received at "+downloadEvent.date)
    }
}
