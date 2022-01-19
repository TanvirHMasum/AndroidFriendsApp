package com.example.friends.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.friends.R

open class ProgressDialog(private val mActivity:Activity) {
    private lateinit var dialog:AlertDialog

    fun startProgress(){
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_items, null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }
     fun dismissProgress(){
        dialog.dismiss()
    }
}