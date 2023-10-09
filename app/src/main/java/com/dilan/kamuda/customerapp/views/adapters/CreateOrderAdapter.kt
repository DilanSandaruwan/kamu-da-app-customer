package com.dilan.kamuda.customerapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.model.order.OrderItem
import com.dilan.kamuda.customerapp.model.order.OrderItemIntermediate
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class CreateOrderAdapter(
    private val itemClickListener: OnItemClickListener,
    private val checkedItemListener: CheckedItemListener,
    private val onItemQuantityChangeListener: OnItemQuantityChangeListener,
) : ListAdapter<OrderItemIntermediate, CreateOrderAdapter.ViewHolder>(diff_util) {
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
        holder.tvItemCount.text = item.quantity.toString()
        holder.cbxOrderItem.isChecked = item in checkedItems

        holder.cbxOrderItem.setOnCheckedChangeListener { _, isChecked ->
            checkedItemListener.onItemChecked(item, isChecked) // Call the callback method

            holder.tvItemCount.visibility = if (isChecked) VISIBLE else INVISIBLE
            holder.btnDecrement.visibility =
                if (isChecked) VISIBLE else INVISIBLE // Enable decrement if checked
            holder.btnIncrement.visibility =
                if (isChecked) VISIBLE else INVISIBLE // Enable increment if checked

            if (!isChecked) {
                item.quantity = 0 // Reset item count to 0 when unchecked
                holder.tvItemCount.text = "0"
            }
        }

        if (item in checkedItems) {
            holder.tvItemCount.visibility =
                if (holder.cbxOrderItem.isChecked) VISIBLE else INVISIBLE
            holder.btnDecrement.visibility =
                if (holder.cbxOrderItem.isChecked) VISIBLE else INVISIBLE // Enable decrement if checked
            holder.btnIncrement.visibility =
                if (holder.cbxOrderItem.isChecked) VISIBLE else INVISIBLE // Enable increment if checked

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