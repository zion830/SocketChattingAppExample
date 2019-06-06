package com.example.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import com.example.R
import com.example.model.MsgModel
import com.example.network.URL
import com.example.network.coupleLinkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent


class MainActivity : AppCompatActivity() {
    private lateinit var mClient: StompClient
    private var mRestPingDisposable: Disposable? = null
    private var compositeDisposable: CompositeDisposable? = null

    private val mMsgArray = arrayListOf<String>()
    private lateinit var mMsgAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, URL.get())
        initView()
    }

    private fun initView() {
        mMsgAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mMsgArray)
        lv_main.adapter = mMsgAdapter

        btn_connect.setOnClickListener {
            connectSocket()
        }

        btn_disconnect.setOnClickListener {
            mClient.disconnect()
        }

        btn_send.setOnClickListener {
            if (TextUtils.isEmpty(et_main_name.text))
                showStatus("이름을 입력해주세요~")
            else
                sendMsg(et_main_name.text.toString())
        }
    }

    private fun showStatus(status: String) {
        runOnUiThread {
            tv_main.text = status
        }
    }

    private fun addMsg(msg: String) {
        runOnUiThread {
            mMsgAdapter.add(msg)
        }
    }

    private fun connectSocket() {
        mClient.withClientHeartbeat(1000).withServerHeartbeat(1000)
        resetSubscriptions()

        // socket 상태 표시
        val lifecycle = mClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        showStatus("Stomp connection opened")
                        changeBtnStatus(true)
                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.e("error : ", event.exception.toString())
                        changeBtnStatus(false)
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        showStatus("connection closed")
                        resetSubscriptions()
                        changeBtnStatus(false)
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        showStatus("Stomp failed server heartbeat")
                        changeBtnStatus(false)
                    }
                }
            }
        compositeDisposable?.add(lifecycle)

        // Receive greetings
        val dispTopic = mClient.topic("/topic/greetings")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage -> addMsg(topicMessage.payload) },
                { t -> showStatus("error : " + t.message) })
        compositeDisposable?.add(dispTopic)
        mClient.connect()
    }

    private fun changeBtnStatus(isConnected: Boolean) {
        btn_connect.isEnabled = !isConnected
        btn_send.isEnabled = isConnected
        btn_disconnect.isEnabled = isConnected
    }

    private fun sendMsg(msg: String) {
        mRestPingDisposable = coupleLinkApi()
            .sendMsg(MsgModel(msg))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ item -> addMsg(item.content) },
                { t -> showStatus("error : " + t.message) })
    }

    private fun resetSubscriptions() {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        } else {
            compositeDisposable?.dispose()
        }
    }
}
