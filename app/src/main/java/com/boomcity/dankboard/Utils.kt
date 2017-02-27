package com.boomcity.dankboard

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson

class Utils {
    companion object{
        lateinit var viewPager: ViewPager

        fun addClipToFavoriteTab(soundClip: SoundClip, tabIndex: Int){
            var selectedTabView = viewPager.getChildAt(tabIndex)
            var recyclerView = selectedTabView.findViewById(R.id.recycler_view) as RecyclerView
            var soundAdapter = recyclerView.adapter as SoundRecyclerAdapter
            soundAdapter.addSoundClip(soundClip)
        }
    }
}
