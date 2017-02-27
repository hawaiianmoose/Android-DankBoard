package com.boomcity.dankboard

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson

class TabFragment : Fragment() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<SoundRecyclerAdapter.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_sound_tab, container, false)

        mRecyclerView = rootView.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(rootView.context)
        mRecyclerView.setLayoutManager(mLayoutManager)


        //get tab info
        var sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("TabDataInfo", "")
        val tabDataInfo = gson.fromJson<TabDataInfo>(json, TabDataInfo::class.java)


        //Use this to initiate the proper tab
        var tabIndex = this.arguments["tab_number"]

        if (tabIndex == 1) {
            var myDataset = mutableListOf<SoundClip>(SoundClip("Dong",R.raw.test_sound), SoundClip("Dank",R.raw.test_sound))

            mAdapter = SoundRecyclerAdapter(myDataset)
            mRecyclerView.setAdapter(mAdapter)
        }
        else {
            var myDataset = mutableListOf<SoundClip>()

            mAdapter = SoundRecyclerAdapter(myDataset)
            mRecyclerView.setAdapter(mAdapter)
        }

        return rootView
    }

    companion object {
        val ARG_SECTION_NUMBER = "tab_number"

        fun newInstance(tabNumber: Int): TabFragment {
            val fragment = TabFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, tabNumber)
            fragment.arguments = args
            return fragment
        }
    }
}
