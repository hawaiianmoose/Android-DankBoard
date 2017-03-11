package com.boomcity.dankboard

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
import com.google.gson.Gson
import com.github.clans.fab.FloatingActionMenu

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var mViewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var tabFAM: FloatingActionMenu
    lateinit var renameTabFab: FloatingActionButton
    lateinit var deleteTabFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        //toolbar.setLogo(R.drawable.ic_marijuanaicon)
        setSupportActionBar(toolbar)
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
            tabsData = TabsData(mutableListOf(TabDataInfo("All",0, mutableListOf()),TabDataInfo("Favorites", 1, mutableListOf())))
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
        var selectedTabIndex = tabLayout.selectedTabPosition
        if (selectedTabIndex > 1) {
            mSectionsPagerAdapter!!.removeTab(selectedTabIndex)
            val tab = tabLayout.getTabAt(selectedTabIndex - 1)
            tab!!.select()
            tabFAM.close(true)
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

        if (id == R.id.action_new_tab && tabLayout.tabCount < 8) {
            val newFragment = EditDialogFragment(this)
            newFragment.show(fragmentManager, null)
            return true
        }
        else {
            //TODO msg popup
        }

        return super.onOptionsItemSelected(item)
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
