package com.app.meupedido.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import com.app.meupedido.data.Order
import com.app.meupedido.databinding.ItemOrderBinding

class OrderAdapter(val orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val selectedOrders = SparseBooleanArray()
    var orderList = orders

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
        holder.bind(orderList[position])
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

    override fun getItemCount(): Int = orderList.size

    fun setData(order: List<Order>) {
        this.orderList = order
        notifyDataSetChanged()
    }

    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class OrderViewHolder(private val itemOrderBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemOrderBinding.root) {
        fun bind(order: Order) {
            //itemOrderBinding.ivStore
            itemOrderBinding.tvNameStore.text = order.nameStore
            itemOrderBinding.tvOrderNumber.text = order.number
            itemOrderBinding.tvStatus.text = order.status
        }
    }
}