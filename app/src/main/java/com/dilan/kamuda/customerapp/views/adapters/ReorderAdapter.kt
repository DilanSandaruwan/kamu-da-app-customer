package com.dilan.kamuda.customerapp.views.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.model.order.OrderItemIntermediate
import com.dilan.kamuda.customerapp.util.component.RoundedImageView
import com.dilan.kamuda.customerapp.views.fragments.order.ReorderFragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class ReorderAdapter(
    private val contextIs: ReorderFragment,
    private val itemClickListener: ReorderAdapter.OnItemClickListener,
    private val checkedItemListener: ReorderAdapter.CheckedItemListener,
    private val onItemQuantityChangeListener: ReorderAdapter.OnItemQuantityChangeListener,
) : ListAdapter<OrderItemIntermediate, ReorderAdapter.ViewHolder>(ReorderAdapter.diff_util) {

    interface OnItemClickListener {
        fun itemClick(item: OrderItemIntermediate)
    }

    interface CheckedItemListener {
        fun onItemChecked(item: OrderItemIntermediate, isChecked: Boolean)
    }

    interface OnItemQuantityChangeListener{
        fun onItemQuantityChanged(isChanged:Boolean)
    }

    private val checkedItems = mutableListOf<OrderItemIntermediate>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: MaterialTextView = view.findViewById(R.id.mtvItemName)
        val itemPrice: MaterialTextView = view.findViewById(R.id.mtvItemPrice)
        val cbxOrderItem: MaterialCheckBox = view.findViewById(R.id.mcbOrderItem)
        val btnIncrement: ImageButton = view.findViewById(R.id.btnIncrement)
        val btnDecrement: ImageButton = view.findViewById(R.id.btnDecrement)
        val tvItemCount: TextView = view.findViewById(R.id.tvItemCount)
        val ivRoundedImageView: RoundedImageView = view.findViewById(R.id.ivRoundMenuItem)
    }
    companion object {

        val diff_util = object : DiffUtil.ItemCallback<OrderItemIntermediate>() {

            override fun areItemsTheSame(oldItem: OrderItemIntermediate, newItem: OrderItemIntermediate): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: OrderItemIntermediate,
                newItem: OrderItemIntermediate
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReorderAdapter.ViewHolder {
        val _view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_order_item, parent, false)
        return ViewHolder(_view)
    }

    override fun onBindViewHolder(holder: ReorderAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        // Set data to the EditText views
        holder.itemName.text = item.name.toString()
        holder.itemPrice.text = item.price.toString()
        holder.tvItemCount.text = item.quantity.toString()
        holder.cbxOrderItem.isChecked = item in checkedItems
        if (item.image != null) {
            var imageBitmap =
                BitmapFactory.decodeByteArray(item.image as ByteArray?, 0, item.image.size)
            Glide.with(contextIs)
                .load(imageBitmap)
                .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
                .into(holder.ivRoundedImageView)
        }
        holder.cbxOrderItem.setOnCheckedChangeListener { _, isChecked ->
            checkedItemListener.onItemChecked(item, isChecked) // Call the callback method

            holder.tvItemCount.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            holder.btnDecrement.visibility =
                if (isChecked) View.VISIBLE else View.INVISIBLE // Enable decrement if checked
            holder.btnIncrement.visibility =
                if (isChecked) View.VISIBLE else View.INVISIBLE // Enable increment if checked

            if (!isChecked) {
                item.quantity = 0 // Reset item count to 0 when unchecked
                holder.tvItemCount.text = "0"
            }
        }

        if (item in checkedItems) {
            holder.tvItemCount.visibility =
                if (holder.cbxOrderItem.isChecked) View.VISIBLE else View.INVISIBLE
            holder.btnDecrement.visibility =
                if (holder.cbxOrderItem.isChecked) View.VISIBLE else View.INVISIBLE // Enable decrement if checked
            holder.btnIncrement.visibility =
                if (holder.cbxOrderItem.isChecked) View.VISIBLE else View.INVISIBLE // Enable increment if checked

            if (!holder.cbxOrderItem.isChecked) {
                item.quantity = 0 // Reset item count to 0 when unchecked
                holder.tvItemCount.text = "0"
            }
        }

        holder.btnIncrement.setOnClickListener {
            item.quantity++
            holder.tvItemCount.text = item.quantity.toString()
            onItemQuantityChangeListener.onItemQuantityChanged(true)
        }

        holder.btnDecrement.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                holder.tvItemCount.text = item.quantity.toString()
                onItemQuantityChangeListener.onItemQuantityChanged(true)
            }
        }
    }

    fun setCheckedItems(items: List<OrderItemIntermediate>) {
        checkedItems.clear()
        checkedItems.addAll(items)
        submitList(currentList)
    }

    fun getCheckedItemsList(): List<OrderItemIntermediate> {
        return checkedItems.toList()
    }
}