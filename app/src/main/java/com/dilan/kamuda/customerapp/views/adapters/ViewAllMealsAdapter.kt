package com.dilan.kamuda.customerapp.views.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.views.fragments.foodhouse.FoodHouseFragment

class ViewAllMealsAdapter(
    val contextIs: FoodHouseFragment,
    private val itemClickListener: ViewAllMealsAdapter.OnItemClickListener,
) : ListAdapter<FoodMenu, ViewAllMealsAdapter.ViewHolder>(ViewAllMealsAdapter.diff_util) {

    interface OnItemClickListener {
        fun itemClick(item: FoodMenu)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewAllMealsAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_menu_item, parent, false)
        return ViewAllMealsAdapter.ViewHolder(view)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ivMenuItem: ImageView = itemView.findViewById(R.id.menu_image)
        val ivAvailability: ImageView = itemView.findViewById(R.id.ivAvailability)
        val tvMenuItemName: TextView = itemView.findViewById(R.id.tvMenuItemName)
        val tvMenuItemPrice: TextView = itemView.findViewById(R.id.tvMenuItemPrice)

    }

    override fun onBindViewHolder(holder: ViewAllMealsAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvMenuItemName.text = item.name.toString()
        holder.tvMenuItemPrice.text = "LKR " + item.price.toString()
        holder.ivAvailability.visibility = if (item.status != "Y") {
            VISIBLE
        } else {
            GONE
        }
        if (item.image != null) {
            val imageData =
                android.util.Base64.decode(item.image, android.util.Base64.DEFAULT)
            var imageBitmap =
                BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            Glide.with(contextIs)
                .load(imageBitmap)
                .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
                .into(holder.ivMenuItem)
        } else {

        }

    }

    companion object {

        val diff_util = object : DiffUtil.ItemCallback<FoodMenu>() {

            override fun areItemsTheSame(oldItem: FoodMenu, newItem: FoodMenu): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FoodMenu,
                newItem: FoodMenu
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}