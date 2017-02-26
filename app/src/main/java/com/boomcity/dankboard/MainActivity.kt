package com.boomcity.dankboard

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.design.widget.FloatingActionButton
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

class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var mViewPager: ViewPager
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager.adapter = mSectionsPagerAdapter

        tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            addNewTab()
        }

        //sharedPref write
        var sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        //editor.putInt(getString(R.string.saved_high_score), newHighScore);
        //editor.commit()

        //shared pref read
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        //long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);


        //val favoriteTabNames = hashSetOf<String>()
        //favoriteTabNames.add("dickbutt")
        //favoriteTabNames.add("dickbutt2")

        //TODO putString to insert index + tab name
        //TODO putStringSet to insert TabName + ListOfSoundClipIds

       // var set: Set<String> = favoriteTabNames

       // editor.putStringSet("FavoriteTabs",set)
        //editor.commit()


        var favs = sharedPref.getStringSet("FavoriteTabs", hashSetOf("Favorites 1"))

        for (fav in favs!!) {
            var check = fav
            var check2 = check
        }

        Utils.tabLayout = tabLayout
    }

    fun addNewTab() {
        tabLayout.addTab(tabLayout.newTab().setText("New Tab"))
        mSectionsPagerAdapter!!.addNewTab()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        if (id == R.id.action_new_tab) {
            addNewTab()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var tabCount: Int = 2

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return TabFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            return tabCount
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "All"
                1 -> return "Favorites 1"
            }
            return "Favorites " + position
        }

        fun addNewTab() {
            tabCount++
            notifyDataSetChanged()
        }
    }
}
