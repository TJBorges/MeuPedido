package com.app.meupedido.util

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.R
import android.content.Context
import androidx.core.content.ContextCompat
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGesture(context: Context) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.RIGHT){

    val deleteColor = ContextCompat.getColor(context, R.color.holo_red_light)
    val deleteIconColor = ContextCompat.getColor(context, R.color.white)
    val deleteLabelColor = ContextCompat.getColor(context, R.color.white)
    val deleteIcon = R.drawable.ic_menu_delete


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeRightBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(deleteIcon)
            .setActionIconTint(deleteIconColor)
            .addSwipeRightLabel("Excluir")
            .setSwipeRightLabelColor(deleteLabelColor)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}