package com.boomcity.dankboard

import android.content.DialogInterface
import android.app.Dialog
import android.os.Bundle
import android.app.AlertDialog
import android.app.DialogFragment
import android.widget.EditText

class EditDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity.layoutInflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.edit_tab_name_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.dialog_ok, DialogInterface.OnClickListener { dialog, id ->
                    var dialogBox = dialog as Dialog
                    var editText = dialogBox.findViewById(R.id.new_tab_name_text) as EditText
                    var newTabName = editText.text.toString()
                    DataService.renameTab(newTabName,this.tag.toInt())
                })
                .setNegativeButton(R.string.dialog_cancel, DialogInterface.OnClickListener { dialog, id -> dialog.dismiss() })
        return builder.create()
    }
}
