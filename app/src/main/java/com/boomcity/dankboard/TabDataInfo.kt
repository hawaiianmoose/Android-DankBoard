package com.boomcity.dankboard

class TabDataInfo (tabName: String, tabPosition: Int, soundClips: MutableList<SoundClip> = arrayListOf()) {
    val name: String = tabName
    val position: Int = tabPosition
    val soundClips: MutableList<SoundClip> = soundClips
}

class TabsData (tabsData: MutableList<TabDataInfo>?) {
    val tabsList: MutableList<TabDataInfo>? = tabsData

    fun getTab(tabId: Int) : TabDataInfo? {
        for (tab in tabsList!!) {
            if (tab.position == tabId) {
                return tab
            }
        }
        return null
    }
}