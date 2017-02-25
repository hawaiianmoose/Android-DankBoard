package com.boomcity.dankboard

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.support.design.widget.TabLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.widget.*

class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(mViewPager)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            addNewTab()
        }

        //favorite tab selector dialog
        val builder = AlertDialog.Builder(this)
        builder.setIcon(android.R.drawable.ic_dialog_email)
        builder.setTitle("swag")

        var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
        arrayAdapter.add("Hardik")
        arrayAdapter.add("Archit")
        arrayAdapter.add("Jignesh")
        arrayAdapter.add("Umang")
        arrayAdapter.add("Gatti")

        builder.setNegativeButton("cancel", { dialog, which ->
            dialog.dismiss()
        })

        builder.setAdapter(arrayAdapter, { dialog, which ->

            var strName = arrayAdapter.getItem(which)
            var builderInner = AlertDialog.Builder(this)
            builderInner.setMessage(strName)
            builderInner.setTitle("Your Selected Item is")
            builderInner.setPositiveButton("Ok", { dialog, which ->
                dialog.dismiss()
            })
            builderInner.show()
        })
        builder.show()

        //sharedPref write
        var sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        //editor.putInt(getString(R.string.saved_high_score), newHighScore);
        //editor.commit()

        //shared pref read
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        //long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);


       // val favoriteTabNames = hashSetOf<String>()
        //favoriteTabNames.add("dickbutt")
        //favoriteTabNames.add("dickbutt2")

       // var set: Set<String> = favoriteTabNames

       // editor.putStringSet("FavoriteTabs",set)
        //editor.commit()


        var favs = sharedPref.getStringSet("FavoriteTabs", hashSetOf("Favorites 1"))

        for (fav in favs!!) {
            var check = fav
            var check2 = check
        }

    }

    fun addNewTab() {
        tabLayout?.addTab(tabLayout!!.newTab().setText("New Tab"))
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

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        lateinit var mRecyclerView: RecyclerView
        lateinit var mAdapter: RecyclerView.Adapter<SoundRecyclerAdapter.ViewHolder>
        lateinit var mLayoutManager: RecyclerView.LayoutManager

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)

            mRecyclerView = rootView.findViewById(R.id.recycler_view) as RecyclerView
            mRecyclerView.setHasFixedSize(true)
            mLayoutManager = LinearLayoutManager(rootView.context)
            mRecyclerView.setLayoutManager(mLayoutManager)

            var myDataset = listOf<String>("Dick","Dong","Balls","Dick","Dong","Balls","Dick","Dong","Balls","Dick","Dong","Balls")



            mAdapter = SoundRecyclerAdapter(myDataset)
            mRecyclerView.setAdapter(mAdapter)


            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    class SoundRecyclerAdapter(data: List<String>) : RecyclerView.Adapter<SoundRecyclerAdapter.ViewHolder>() {
        private val mDataset: List<String> = data

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            var text = holder!!.mView.findViewById(R.id.sound_clip_text_view) as TextView
            text.setText(mDataset[position])

            var playButton = holder!!.mView.findViewById(R.id.play_button) as ImageButton
            val mp = MediaPlayer.create(holder!!.mView.context, R.raw.test_sound)
            mp.setOnCompletionListener {
                playButton.setImageResource(android.R.drawable.ic_media_play)
            }

            playButton.setOnClickListener {
                if(mp.isPlaying) {
                    mp.pause()
                    playButton.setImageResource(android.R.drawable.ic_media_play)
                }
                else
                {
                    mp.start()
                    playButton.setImageResource(android.R.drawable.ic_media_pause)
                }
            }

        }

        override fun getItemCount(): Int {
            return mDataset!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            var v = LayoutInflater.from(parent!!.getContext()).inflate(R.layout.fragment_sound_clip, parent, false)

            var vh = ViewHolder(v)
            return vh
        }

        class ViewHolder(var mView: View) : RecyclerView.ViewHolder(mView)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var tabCount: Int = 2

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
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
