package com.example.friends.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.friends.R
import com.example.friends.adapter.FriendListAdapter
import com.example.friends.models.FriendData
import com.example.friends.models.FriendInfo
import com.example.friends.network.RestClient
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), FriendListAdapter.OnViewFriendItem {
    private val llm: LinearLayoutManager = LinearLayoutManager(this)
    private var glm: GridLayoutManager = GridLayoutManager(this, 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            friendInfoData()
        }
    }

    private fun friendInfoData() {
        val getImages = RestClient.get().getFriendsInfo(10)
        getImages.enqueue(object :
            Callback<FriendData> {
            override fun onFailure(call: Call<FriendData>, t: Throwable) {
                Toast.makeText(this@MainActivity, "onFailure Method Calling", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<FriendData>, response: Response<FriendData>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    friendItems(response.body()!!)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Sorry! Something goes wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun friendItems(friendData: FriendData) {

        val recyclerView: RecyclerView = findViewById(R.id.friendInfoReView)

        llm.orientation = LinearLayoutManager.VERTICAL
        val orientation: Int = this.resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            glm
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            glm = GridLayoutManager(this, 3)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = FriendListAdapter(friendData.results, this@MainActivity)
        }
        recyclerView.layoutManager = glm
    }

    @SuppressLint("SetTextI18n")
    override fun onFriendItemView(friendInfo: FriendInfo, position: Int) {
        val dialogInflater = this.layoutInflater
        val openImageView: View = dialogInflater.inflate(R.layout.friend_detail_layout, null)
        val dialogBuilder =
            AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar)
        val image: ImageView = openImageView.findViewById(R.id.friendImgView)
        val name: TextView = openImageView.findViewById(R.id.nameDiaTV)
        val address: TextView = openImageView.findViewById(R.id.addressDiaTV)
        val email: TextView = openImageView.findViewById(R.id.emailDiaTV)
        val phone: TextView = openImageView.findViewById(R.id.phoneDiaTV)
        //val name: TextView = openImageView.findViewById(R.id.nameDiaTV)

        Picasso.get().load(friendInfo.picture.large).fit().centerInside().into(image)
        name.text = friendInfo.name.title + " " + friendInfo.name.first + " " + friendInfo.name.last
        address.text =
            friendInfo.location.city + ", " + friendInfo.location.state + ", " + friendInfo.location.country
        email.text = friendInfo.email
        phone.text = friendInfo.phone

        email.setOnClickListener {
            emailFriend(friendInfo.email)
        }

        phone.setOnClickListener {
            callFriend(friendInfo.phone)
        }

        dialogBuilder.setView(openImageView)
            .setCancelable(false)
            .setTitle(getString(R.string.friends_detail))
            .setNeutralButton(R.string.back, null)

        dialogBuilder.create().show()
    }

    private fun emailFriend(email: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Greetings")
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, "Hi, My Dear Friends")


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun callFriend(phone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        startActivity(callIntent)
    }
}