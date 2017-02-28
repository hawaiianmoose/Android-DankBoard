package com.boomcity.dankboard

import android.content.SharedPreferences
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson

class Utils {
    companion object{
        private lateinit var viewPager: ViewPager
        private lateinit var tabsDataObject: TabsData
        private lateinit var sharedPreferences: SharedPreferences
        private var gson = Gson()

        fun getTabsData() : TabsData{
            return tabsDataObject
        }

        fun init(vp: ViewPager, tabsData: TabsData, sharedPrefs: SharedPreferences) {
            viewPager = vp
            tabsDataObject = tabsData
            sharedPreferences = sharedPrefs
        }

        fun addClipToFavoriteTab(soundClip: SoundClip, tabIndex: Int){
            var selectedTabView = viewPager.getChildAt(tabIndex)
            var recyclerView = selectedTabView.findViewById(R.id.recycler_view) as RecyclerView
            var soundAdapter = recyclerView.adapter as SoundRecyclerAdapter
            soundAdapter.addSoundClip(soundClip)
            tabsDataObject.getTab(tabIndex)!!.soundClips.add(soundClip)
            var json = gson.toJson(tabsDataObject)
            sharedPreferences.edit().putString("TabsDataInfo", json).apply()
        }
    }
}
