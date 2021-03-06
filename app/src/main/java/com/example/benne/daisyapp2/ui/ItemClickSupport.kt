package com.example.benne.daisyapp2.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.view.View.OnLongClickListener
import com.example.benne.daisyapp2.*


/**
 * Created by benne on 7/01/2018.
 */
class ItemClickSupport private constructor(private val mRecyclerView: androidx.recyclerview.widget.RecyclerView) {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private val mOnClickListener = View.OnClickListener { v ->
        if (mOnItemClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            mOnItemClickListener!!.onItemClicked(mRecyclerView, holder.adapterPosition, v)
        }
    }

    private val mOnLongClickListener = OnLongClickListener { v ->
        if (mOnItemLongClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            return@OnLongClickListener mOnItemLongClickListener!!.onItemLongClicked(mRecyclerView, holder.adapterPosition, v)
        }
        false
    }

    private val mAttachListener = object : androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener)
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }
    }

    init {
        mRecyclerView.setTag(R.id.item_click_support, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener): ItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener): ItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }

    private fun detach(view: androidx.recyclerview.widget.RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.item_click_support, null)
    }

    interface OnItemClickListener {

        fun onItemClicked(recyclerView: androidx.recyclerview.widget.RecyclerView, position: Int, v: View)
    }

    interface OnItemLongClickListener {

        fun onItemLongClicked(recyclerView: androidx.recyclerview.widget.RecyclerView, position: Int, v: View): Boolean
    }

    companion object {

        fun addTo(view: androidx.recyclerview.widget.RecyclerView): ItemClickSupport {
            return view.getTag(R.id.item_click_support)
                as? ItemClickSupport
                ?: ItemClickSupport(view)
        }

        fun removeFrom(view: androidx.recyclerview.widget.RecyclerView): ItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as ItemClickSupport
            support.detach(view)
            return support
        }
    }
}