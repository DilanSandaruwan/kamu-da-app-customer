package com.dilan.kamuda.customerapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.google.android.material.textview.MaterialTextView

class ViewAllOrdersAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<OrderDetail, ViewAllOrdersAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: MaterialTextView = view.findViewById(R.id.mtvOrderId)
        val orderTotal: MaterialTextView = view.findViewById(R.id.mtvOrderTotal)
        val rvOrderItems: RecyclerView = view.findViewById(R.id.rvViewOrderItems)
        val lytBtnToggle: RelativeLayout = view.findViewById(R.id.lytBtnToggle)
        val btnArrowUp: ImageView = view.findViewById(R.id.btnArrowUp)
        val btnArrowDown: ImageView = view.findViewById(R.id.btnArrowDown)
    }

    interface OnItemClickListener {
        fun itemClick(item: OrderDetail)
    }

    companion object {

        val diff_util = object : DiffUtil.ItemCallback<OrderDetail>() {

            override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OrderDetail,
                newItem: OrderDetail
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_order_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.orderId.text = item.id.toString()
        holder.orderTotal.text = item.total.toString()
        // Set up child RecyclerView
        val childAdapter = ViewOrderedItemsAdapter()
        holder.rvOrderItems.layoutManager =
            LinearLayoutManager(holder.rvOrderItems.context) // Set layout manager
        holder.rvOrderItems.addItemDecoration(
            DividerItemDecoration(
                holder.rvOrderItems.context,
                (holder.rvOrderItems.layoutManager as LinearLayoutManager).orientation
            )
        )
        holder.rvOrderItems.adapter = childAdapter
        childAdapter.submitList(item.items)

        holder.lytBtnToggle.setOnClickListener {
            if (holder.btnArrowDown.visibility == VISIBLE) {
                holder.btnArrowDown.visibility = GONE
                holder.btnArrowUp.visibility = VISIBLE
                holder.rvOrderItems.visibility = VISIBLE
            } else {
                holder.btnArrowUp.visibility = GONE
                holder.btnArrowDown.visibility = VISIBLE
                holder.rvOrderItems.visibility = GONE
            }
        }


    }
}