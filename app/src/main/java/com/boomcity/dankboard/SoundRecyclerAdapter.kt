package com.boomcity.dankboard

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

class SoundRecyclerAdapter(data: MutableList<SoundClip>, val tabPosition: Int) : RecyclerView.Adapter<SoundRecyclerAdapter.ViewHolder>() {
    private var mDataset: MutableList<SoundClip> = data

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val mSoundClip = SoundClip(mDataset[position].Title,mDataset[position].AudioId, mDataset[position].Path)

        val text = holder!!.mView.findViewById(R.id.sound_clip_text_view) as TextView
        text.setText(mSoundClip.Title)

        val deleteExists = holder.mView.findViewById(R.id.delete_button)
        if (deleteExists != null) {
            val deleteButton = holder.mView.findViewById(R.id.delete_button) as ImageButton
            deleteButton.setOnClickListener {
                DataService.removeSoundClipFromTab(this,mSoundClip, tabPosition)
            }
        }

        val playButton = holder.mView.findViewById(R.id.play_button) as ImageButton
        val mp: MediaPlayer

        if(mSoundClip.Path != null) {
            mp = MediaPlayer.create(holder.mView.context, Uri.parse(mSoundClip.Path))
        }
        else {
            mp = MediaPlayer.create(holder.mView.context, mSoundClip.AudioId)
        }

        mp.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_playbutton)
        }

        playButton.setOnClickListener {
            if(mp.isPlaying) {
                mp.pause()
                playButton.setImageResource(R.drawable.ic_playbutton)
            }
            else
            {
                mp.start()
                playButton.setImageResource(android.R.drawable.ic_media_pause)
            }
        }

        val favoritesExists = holder.mView.findViewById(R.id.favorite_button)
        if (favoritesExists != null) {
            val addToFavoriteButton = holder.mView.findViewById(R.id.favorite_button) as ImageButton
            addToFavoriteButton.setTag(mDataset[position].AudioId)
            addToFavoriteButton.setOnClickListener {
                showTabSelectionDialog(holder.mView.context, mSoundClip)
            }
        }
    }

    fun showTabSelectionDialog(context: Context, soundClip: SoundClip) {
        val builder = AlertDialog.Builder(context, R.style.DankAlertDialogStyle)
        builder.setTitle("Add dank sound to which tab?")

        var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.select_dialog_item)

        for (tab in DataService.getTabsData().tabsList!!) {
            if(tab.position != 0) {
                arrayAdapter.add(tab.name)
            }
        }

        builder.setNegativeButton("Cancel", { dialog, which ->
            dialog.dismiss()
        })

        builder.setAdapter(arrayAdapter, { dialog, which ->

            if (DataService.getTabsData().getTab(which + 1)!!.soundClips.any { clip -> clip.AudioId == soundClip.AudioId }) {
                val errorBuilder = AlertDialog.Builder(context, R.style.DankAlertDialogStyle)
                errorBuilder.setTitle("This dank sound clip is already in that favorite tab bruh!")
                errorBuilder.setNegativeButton(R.string.dialog_aight, { dialog, which ->
                    dialog.dismiss()
                })
                errorBuilder.show()
            }
            else {
                DataService.addClipToFavoriteTab(soundClip, which + 1)
            }
        })

        builder.show()
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        var soundClipFragmentId: Int

        if (tabPosition > 0) {
            soundClipFragmentId = R.layout.fragment_sound_clip
        }
        else {
            soundClipFragmentId = R.layout.fragment_sound_clip_all
        }

        val v = LayoutInflater.from(parent!!.getContext()).inflate(soundClipFragmentId, parent, false)
        val vh = ViewHolder(v)
        return vh
    }

    class ViewHolder(var mView: View) : RecyclerView.ViewHolder(mView)

}
