package com.app.meupedido.adapter

import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import com.app.meupedido.databinding.ItemOrderBinding
import com.app.meupedido.model.Order

class OrderArchivedAdapter(val ordersArchived: List<Order>) :
    RecyclerView.Adapter<OrderArchivedAdapter.OrderViewHolder>() {

    private val selectedOrders = SparseBooleanArray()
    //private var currentSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemOrderBinding =
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return OrderViewHolder(itemOrderBinding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(ordersArchived[position])
        holder.itemView.setOnClickListener {
            if (selectedOrders.isNotEmpty())
                onItemClick?.invoke(position)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position)
            return@setOnLongClickListener true
        }
        //if (currentSelectedPos == position) currentSelectedPos = -1

    }

    override fun getItemCount(): Int = ordersArchived.size

    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class OrderViewHolder(private val itemOrderBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemOrderBinding.root) {
        fun bind(order: Order) {
            itemOrderBinding.viewOrder.setBackgroundColor(Color.parseColor("#00b400"))
            itemOrderBinding.tvOrderNumber.text = order.number
            itemOrderBinding.tvStatus.text = order.status
        }
    }
}