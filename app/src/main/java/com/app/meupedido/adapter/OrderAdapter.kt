package com.app.meupedido.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import com.app.meupedido.databinding.ItemOrderBinding
import com.app.meupedido.model.Order

class OrderAdapter(val orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var _binding: ItemOrderBinding? = null
    private val binding get() = _binding!!
    private val selectedOrders = SparseBooleanArray()
    private var currentSelectedPos = -1

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
        holder.bind(orders[position])
        holder.itemView.setOnClickListener {
            if (selectedOrders.isNotEmpty())
                onItemClick?.invoke(position)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position)
            return@setOnLongClickListener true
        }
        if (currentSelectedPos == position) currentSelectedPos = -1

    }

    override fun getItemCount(): Int = orders.size

    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class OrderViewHolder(private val itemOrderBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemOrderBinding.root) {
        fun bind(order: Order) {
            itemOrderBinding.tvOrderNumber.text = order.number
            itemOrderBinding.tvStatus.text = order.status
        }
    }
}