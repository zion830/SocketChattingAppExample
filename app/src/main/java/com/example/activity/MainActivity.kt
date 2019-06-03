package com.example.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val host = "http://35.221.98.14"
    private val port = 8000
    private val items = arrayListOf<String>()
    private lateinit var itemsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        itemsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lv_main.adapter = itemsAdapter
    }
}
