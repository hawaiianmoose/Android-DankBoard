package com.boomcity.dankboard

import android.app.AlertDialog
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.support.v7.widget.RecyclerView
import com.google.gson.*
import android.support.design.widget.AppBarLayout

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var mViewPager: ViewPager
    lateinit var mToolBar: Toolbar
    lateinit var tabLayout: TabLayout
    lateinit var tabFAM: FloatingActionMenu
    lateinit var renameTabFab: FloatingActionButton
    lateinit var deleteTabFab: FloatingActionButton

    companion object {
        val READ_REQUEST_CODE = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolBar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolBar)
        getStoredTabData()

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager.adapter = mSectionsPagerAdapter
        mViewPager.setOffscreenPageLimit(10) //10 max tabs

        DataService.setViewpager(mViewPager)

        tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        tabLayout.addOnTabSelectedListener(this)

        tabFAM = findViewById(R.id.tab_FAM) as FloatingActionMenu
        renameTabFab = findViewById(R.id.floating_menu_rename) as FloatingActionButton
        deleteTabFab = findViewById(R.id.floating_menu_delete) as FloatingActionButton

        tabFAM.visibility = View.INVISIBLE

        renameTabFab.setOnClickListener({
            renameTab()
        })
        deleteTabFab.setOnClickListener({
            deleteTab()
        })
    }

    private fun getStoredTabData() {
        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString("TabsDataInfo", "")
        var tabsData = gson.fromJson<TabsData>(json, TabsData::class.java)

        if (tabsData == null) {
            //first startup
            val defaultTabSounds = mutableListOf<SoundClip>(SoundClip("AirPorn",R.raw.airporn), SoundClip("AirHorn",R.raw.horn),SoundClip("BBC",R.raw.bbc),
                    SoundClip("BlahBlahBlah",R.raw.blahblah),
                    SoundClip("Bobby",R.raw.bobby),SoundClip("Scott Bradford",R.raw.bradford), SoundClip("Cowbell",R.raw.cowbell),
                    SoundClip("Dedotated Wham",R.raw.dedotated),SoundClip("Deez Nuts",R.raw.deeznuts),SoundClip("Dew",R.raw.dew),
                    SoundClip("Dota",R.raw.dota),SoundClip("Get Dumped On",R.raw.dumpedon),SoundClip("Eat A Dick",R.raw.eatadick),
                    SoundClip("Eggs",R.raw.eggs), SoundClip("Harry",R.raw.harry),
                    SoundClip("I'm Gay",R.raw.imgay),SoundClip("Jonwall",R.raw.jonwallmissplay),
                    SoundClip("Juice Me Bitch",R.raw.juiceme),SoundClip("Ohhhhh",R.raw.ohhhhh),
                    SoundClip("Ohhhhh2",R.raw.ohhhh2),SoundClip("Psyche",R.raw.psyche),SoundClip("Take a Sip",R.raw.sip),SoundClip("2Spooky",R.raw.spookyhorn),
                    SoundClip("Triple",R.raw.triple),SoundClip("Wine Poop",R.raw.winepoop),SoundClip("Wombo Combo",R.raw.wombo),
                    SoundClip("Wrecking Ball",R.raw.wreckingball))
            tabsData = TabsData(mutableListOf(TabDataInfo("All",0, defaultTabSounds),TabDataInfo("Favorites", 1, mutableListOf())))
        }

        DataService.init(tabsData,sharedPrefs)
    }

    fun addNewTab(tabName: String) {
        tabLayout.addTab(tabLayout.newTab())
        mSectionsPagerAdapter!!.addNewTab(tabName)
    }

    fun renameTab() {
        val newFragment = EditDialogFragment(this)
        newFragment.show(fragmentManager, tabLayout.selectedTabPosition.toString())
        tabFAM.close(true)
    }

    fun deleteTab() {
        val selectedTabIndex = tabLayout.selectedTabPosition
        if (selectedTabIndex > 1) {
            mSectionsPagerAdapter!!.removeTab(selectedTabIndex)
            val tab = tabLayout.getTabAt(selectedTabIndex - 1)
            tab!!.select()
            tabFAM.close(true)
        }
    }

    fun newSoundClipName(soundClipName: String, audioUri: String) {
        if (DataService.getTabsData().getTab(0)!!.soundClips.any { clip -> clip.Title.toLowerCase() == soundClipName.toLowerCase() }) {
            val errorBuilder = AlertDialog.Builder(this, R.style.DankAlertDialogStyle)
            errorBuilder.setTitle(R.string.tab_name_in_use)
            errorBuilder.setNegativeButton(R.string.dialog_aight, { dialog, which ->
                dialog.dismiss()
            })
            errorBuilder.show()
        }
        else {
            val customSoundClip = SoundClip(soundClipName, System.currentTimeMillis().toInt(),audioUri)
            DataService.addClipToFavoriteTab(customSoundClip, 0)
            goToNewlyCreatedSoundClip()
        }
    }

    private fun goToNewlyCreatedSoundClip() {
        tabLayout.getTabAt(0)!!.select()
        val recyclerView = mViewPager.getChildAt(0).findViewById(R.id.recycler_view) as RecyclerView
        val check = recyclerView.adapter.itemCount
        recyclerView.verticalScrollbarPosition = check
        recyclerView.smoothScrollToPosition(check - 1)

        if (mToolBar.getParent() is AppBarLayout) {
            (mToolBar.getParent() as AppBarLayout).setExpanded(false,true)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab!!.position != 0){
            tabFAM.visibility = View.VISIBLE
        }
        else {
            tabFAM.visibility = View.INVISIBLE
        }
        deleteTabFab.isEnabled = tab.position > 1
        tabFAM.close(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_new_tab) {
            if (tabLayout.tabCount < 8) {
                val newFragment = EditDialogFragment(this)
                newFragment.show(fragmentManager, null)
                return true
            }
            else {
                val errorBuilder = AlertDialog.Builder(this, R.style.DankAlertDialogStyle)
                errorBuilder.setTitle("Ya'll got too many tabs dawg.")
                errorBuilder.setNegativeButton(R.string.dialog_aight, { dialog, which ->
                    dialog.dismiss()
                })
                errorBuilder.show()
            }
        }

        if (id == R.id.action_new_sound) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "audio/*"
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        return super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val audioUri: Uri
            if (resultData != null) {
                audioUri = resultData.data
                val newSoundNameFragment = NewSoundClipDialogFragment(this)
                newSoundNameFragment.show(fragmentManager,audioUri.toString())
            }
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var tabCount: Int = DataService.getTabsData().tabsList!!.size

        override fun getItem(position: Int): Fragment {
            return TabFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return tabCount
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "All"
            }
            return DataService.getTabsData().tabsList!![position].name
        }

        fun addNewTab(tabName: String) {
            DataService.addNewTab(tabName, tabCount)
            tabCount++
            notifyDataSetChanged()
        }

        fun removeTab(tabIndex: Int) {
            DataService.deleteTab(tabIndex)
            tabCount--
            notifyDataSetChanged()
        }
    }
}
