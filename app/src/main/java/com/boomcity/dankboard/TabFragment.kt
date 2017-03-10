package com.boomcity.dankboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TabFragment : Fragment() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<SoundRecyclerAdapter.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_sound_tab, container, false)

        mRecyclerView = rootView.findViewById(R.id.recycler_view) as RecyclerView
        //mRecyclerView.setOnScrollListener(HidingScrollListener())
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(rootView.context)
        mRecyclerView.setLayoutManager(mLayoutManager)

        //Use this to initiate the proper tab
        var tabIndex = this.arguments["tab_number"] as Int
        var tabInfo = DataService.getTabsData().getTab(tabIndex)

        if (tabIndex == 0) {
            var myDataset = mutableListOf<SoundClip>(SoundClip("Dong",R.raw.test_sound), SoundClip("Dank",R.raw.test_sound))

            mAdapter = SoundRecyclerAdapter(myDataset, tabIndex)
            mRecyclerView.setAdapter(mAdapter)
        }
        else if (tabInfo != null) {
            var myDataset = tabInfo.soundClips

            mAdapter = SoundRecyclerAdapter(myDataset, tabIndex)
            mRecyclerView.setAdapter(mAdapter)
        }
        else {
            var myDataset = mutableListOf<SoundClip>()

            mAdapter = SoundRecyclerAdapter(myDataset, tabIndex)
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

//    class HidingScrollListener : RecyclerView.OnScrollListener() {
//        private var scrolledDistance = 0
//        private var controlsVisible = true
//
//        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//
//            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
//                onHide()
//                controlsVisible = false
//                scrolledDistance = 0
//            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
//                onShow()
//                controlsVisible = true
//                scrolledDistance = 0
//            }
//
//            if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
//                scrolledDistance += dy
//            }
//        }
//
//        fun onHide() {
//             mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//
//  FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
//  int fabBottomMargin = lp.bottomMargin;
//mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
//        }
//        fun onShow()
//
//        companion object {
//            private val HIDE_THRESHOLD = 20
//        }
//
//    }
}
