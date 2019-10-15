package com.trugath.kotlincommits

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.trugath.kotlincommits.github.api.commitLog.CommitLogRequest
import com.trugath.kotlincommits.github.api.commitLog.CommitLogResponse
import com.trugath.kotlincommits.github.api.URLRequester
import kotlinx.coroutines.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import org.joda.time.DateTime
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // wire up the recycle view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val commitAdapter = CommitEntryAdapter(this)
        recyclerView.adapter = commitAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // update the view when the database updates
        KotlinCommits.commits().getAll().observe(this, Observer {t ->
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            commitAdapter.setData(t.toMutableList())
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
        } )

        // attempt to load more entries when the top or bottom of the view is reached
        recyclerView.addOnScrollListener( object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() <= 0) {
                    GlobalScope.launch {
                        loadMore(applicationContext, after = KotlinCommits.commits().getNewest()?.timeStamp)
                    }
                } else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() >= commitAdapter.itemCount - 1) {
                    GlobalScope.launch {
                        loadMore(applicationContext, before = KotlinCommits.commits().getOldest()?.timeStamp)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        // load any newer entries than known
        GlobalScope.launch {
            loadMore(applicationContext, after = KotlinCommits.commits().getNewest()?.timeStamp)
        }
    }

    companion object {
        // used for compare and set
        private val isLoading: AtomicBoolean = AtomicBoolean(false)

        /**
         * Request more CommitLog entries and store them in the database
         */
        suspend fun loadMore(context: Context, after: DateTime? = null, before: DateTime? = null) {
            if(isLoading.compareAndSet(false, true)) {
                val requester = URLRequester("https://api.github.com")
                when(val response = requester.request(
                    CommitLogRequest(
                        "JetBrains",
                        "kotlin",
                        before,
                        after
                    )
                )) {
                    is CommitLogResponse -> {
                        KotlinCommits
                            .commits()
                            .insertAll(*response.entries().toTypedArray())
                    }
                    else -> {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, context.getString(R.string.failed_update), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                isLoading.set(false)
            }
        }
    }
}
