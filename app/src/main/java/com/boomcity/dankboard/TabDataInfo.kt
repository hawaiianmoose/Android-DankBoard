package com.boomcity.dankboard

class TabDataInfo (tabName: String, tabPosition: Int, soundClips: MutableList<SoundClip>) {
    private val name: String? = tabName
    private val position: Int = tabPosition
    private val soundClips: MutableList<SoundClip>? = soundClips
}

class TabsData (tabsData: List<TabDataInfo>) {
    private val tabsInfo: List<TabDataInfo> = tabsData
}