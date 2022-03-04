package com.app.meupedido.util

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.meupedido.R.drawable
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGesture(context: Context) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {

    private val deleteColor = ContextCompat.getColor(context, R.color.holo_red_light)
    private val deleteIconColor = ContextCompat.getColor(context, R.color.white)
    private val deleteLabelColor = ContextCompat.getColor(context, R.color.white)
    private val deleteIcon = R.drawable.ic_menu_delete

    private val archivedColor = Color.parseColor("#008a00")
    private val archivedIconColor = ContextCompat.getColor(context, R.color.white)
    private val archivedLabelColor = ContextCompat.getColor(context, R.color.white)
    private val archivedIcon = drawable.ic_archive


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
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
        isCurrentlyActive: Boolean,
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c, recyclerView, viewHolder, dX,
            dY, actionState, isCurrentlyActive
        )
            .addSwipeRightBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(deleteIcon)
            .setSwipeRightActionIconTint(deleteIconColor)
            .addSwipeRightLabel("Excluir")
            .setSwipeRightLabelColor(deleteLabelColor)

            .addSwipeLeftBackgroundColor(archivedColor)
            .addSwipeLeftActionIcon(archivedIcon)
            .setSwipeLeftActionIconTint(archivedIconColor)
            .addSwipeLeftLabel("Arquivar")
            .setSwipeLeftLabelColor(archivedLabelColor)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}