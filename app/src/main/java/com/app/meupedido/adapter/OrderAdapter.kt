package com.app.meupedido.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.meupedido.R
import com.app.meupedido.databinding.ItemOrderBinding
import com.app.meupedido.model.Order
import java.lang.reflect.Array.get

class OrderAdapter(private val context: Context,
                   private val orders: List<Order>) : BaseAdapter() {

    private var _binding: ItemOrderBinding? = null

    private val binding get() = _binding!!

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        _binding = ItemOrderBinding.inflate(layoutInflater, parent, false)

        val order = orders[position]
        val holder: ViewHolder
        val line: View
        if (view == null) {
            line = binding.root
            holder = ViewHolder(line, binding)
            line.tag = holder
        } else {
            line = view
            holder = view.tag as ViewHolder
        }
        holder.orderNumber.text = order.number
        holder.status.text = order.status

        return line
    }

    override fun getCount() = orders.size

    override fun getItem(position: Int) = orders[position]

    override fun getItemId(position: Int) = position.toLong()


    companion object {
        data class ViewHolder(val view: View, val binding: ItemOrderBinding) {
            val orderNumber: TextView = binding.tvOrderNumber
            val status: TextView = binding.tvStatus
        }
    }

}