package com.kuanhsien.app.sample.android_google_map_demo.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.data.DemoType
import kotlinx.android.synthetic.main.activity_main_selection.*


class MainSelectionActivity : AppCompatActivity(),
    MainSelectionAdapter.OnItemClickListener {

    private lateinit var viewModel: MainSelectionViewModel
    private lateinit var adapter: MainSelectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_selection)

        initRecyclerView()

        viewModel = ViewModelProviders.of(this).get(MainSelectionViewModel::class.java)

        viewModel.apply {
            demoTypeListLiveData.observe(this@MainSelectionActivity, Observer {
                adapter.setData(it)
            })

            // prepare data
            prepareData()
        }

    }

    override fun onResume() {
        super.onResume()

        recyclerview_main_selection_list.isClickable = true
    }

    /**
     * Setup recycler view
     */
    private fun initRecyclerView() {
        adapter = MainSelectionAdapter()
        adapter.setOnItemClickListener(this)
        recyclerview_main_selection_list.adapter = adapter
        recyclerview_main_selection_list.layoutManager = LinearLayoutManager(this)
    }

    /**
     * implement onItemClick
     */
    override fun onItemClick(demoType: DemoType) {

        if (recyclerview_main_selection_list.isClickable) {
            recyclerview_main_selection_list.isClickable = false

            startActivity(Intent(this, demoType.activityClass))
        }
    }
}