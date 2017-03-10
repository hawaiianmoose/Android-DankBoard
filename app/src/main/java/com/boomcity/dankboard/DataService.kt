package com.boomcity.dankboard

import android.content.SharedPreferences
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson

class DataService {
    companion object{
        private lateinit var viewPager: ViewPager
        private lateinit var tabsDataObject: TabsData
        private lateinit var sharedPreferences: SharedPreferences
        private var gson = Gson()

        fun getTabsData() : TabsData{
            return tabsDataObject
        }

        fun setViewpager(vp: ViewPager) {
            viewPager = vp
        }

        fun init(tabsData: TabsData, sharedPrefs: SharedPreferences) {
            tabsDataObject = tabsData
            sharedPreferences = sharedPrefs
        }

        fun addNewTab(tabName: String, tabIndex: Int) {
            tabsDataObject.tabsList!!.add(TabDataInfo(tabName,tabIndex))
            var json = gson.toJson(tabsDataObject)
            sharedPreferences.edit().putString("TabsDataInfo", json).apply()
        }

        fun deleteTab(tabIndex: Int) {
            tabsDataObject.tabsList!!.removeAt(tabIndex)
            var json = gson.toJson(tabsDataObject)
            sharedPreferences.edit().putString("TabsDataInfo", json).apply()
        }

        fun renameTab(newTabName: String, tabIndex: Int) {
            tabsDataObject.tabsList!![tabIndex].name = newTabName
            var json = gson.toJson(tabsDataObject)
            sharedPreferences.edit().putString("TabsDataInfo", json).apply()
            viewPager.adapter.notifyDataSetChanged()
        }

        fun addClipToFavoriteTab(soundClip: SoundClip, tabIndex: Int){
            var selectedTabView = viewPager.getChildAt(tabIndex)
            var recyclerView = selectedTabView.findViewById(R.id.recycler_view) as RecyclerView
            var soundAdapter = recyclerView.adapter as SoundRecyclerAdapter
            tabsDataObject.getTab(tabIndex)!!.soundClips.add(soundClip)
            var json = gson.toJson(tabsDataObject)
            sharedPreferences.edit().putString("TabsDataInfo", json).apply()
            soundAdapter.notifyDataSetChanged()
        }

        fun removeSoundClipFromTab(soundAdapter: SoundRecyclerAdapter, soundClip: SoundClip, tabIndex: Int) {
            tabsDataObject.getTab(tabIndex)!!.soundClips.removeAll { clip ->
                clip.AudioId == soundClip.AudioId
            }
            var json = gson.toJson(tabsDataObject)
            sharedPreferences.edit().putString("TabsDataInfo", json).apply()
            soundAdapter.notifyDataSetChanged()
        }
    }
}
