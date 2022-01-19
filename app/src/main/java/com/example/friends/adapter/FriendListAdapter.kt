package com.example.friends.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.friends.R
import com.example.friends.models.FriendInfo
import com.squareup.picasso.Picasso

class FriendListAdapter(
    private val friendImageList: List<FriendInfo>,
    private var clickListener: OnViewFriendItem
) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.friend_data_item, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount() = friendImageList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friendInfoList = friendImageList[position]
        Picasso.get().load(friendInfoList.picture.large).fit().centerInside()
            .into(holder.friendImage)
        holder.friendName.text =
            friendInfoList.name.title + " " + friendInfoList.name.first + " " + friendInfoList.name.last
        holder.friendCountry.text = friendInfoList.location.country
        holder.viewImage(friendInfoList, clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardItem: CardView = itemView.findViewById(R.id.friendCard)
        val friendImage: ImageView = itemView.findViewById(R.id.friendImg)
        val friendName: TextView = itemView.findViewById(R.id.friendNameTV)
        val friendCountry: TextView = itemView.findViewById(R.id.friendCountryTV)

        fun viewImage(friendInfo: FriendInfo, action: OnViewFriendItem) {
            cardItem.setOnClickListener {
                action.onFriendItemView(
                    friendInfo,
                    adapterPosition
                )
            }
        }
    }

    interface OnViewFriendItem {
        fun onFriendItemView(friendInfo: FriendInfo, position: Int)
    }
}