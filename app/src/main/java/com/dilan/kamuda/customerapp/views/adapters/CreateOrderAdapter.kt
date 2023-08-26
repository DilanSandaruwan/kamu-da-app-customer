package com.dilan.kamuda.customerapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.google.android.material.textview.MaterialTextView

class CreateOrderAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<FoodMenu, CreateOrderAdapter.ViewHolder>(diff_util) {
    interface OnItemClickListener {
        fun itemClick(item: FoodMenu)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val itemName: MaterialTextView = view.findViewById(R.id.mtvItemName)
        val itemPrice: MaterialTextView = view.findViewById(R.id.mtvItemPrice)
    }
    companion object {

        val diff_util = object : DiffUtil.ItemCallback<FoodMenu>() {

            override fun areItemsTheSame(oldItem: FoodMenu, newItem: FoodMenu): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: FoodMenu,
                newItem: FoodMenu
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val _view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_order_item, parent, false)
        return ViewHolder(_view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // Set data to the EditText views
        holder.itemName.text = item.name.toString()
        holder.itemPrice.text = item.price.toString()

        // Set click listener for item click
        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }
    }
}