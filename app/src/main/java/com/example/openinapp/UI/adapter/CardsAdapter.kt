package com.example.openinapp.UI.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.openinapp.Model.Cards
import com.example.openinapp.R
import com.example.openinapp.databinding.CardModelBinding

class CardsAdapter() : RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {
    private var oldList: ArrayList<Cards> = ArrayList()
    private var exampleListFull: List<Cards>

    init {
        exampleListFull = ArrayList(oldList)
    }

    class CardsViewHolder(private val view: View, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(view) {
        private val mBinding: CardModelBinding

        init {
            mBinding = DataBindingUtil.bind(view)!!
        }

        fun bind(data: Cards) {
            mBinding.apply {
                mBinding.imageView.setImageDrawable(parent.resources.getDrawable(data.drawable))
                mBinding.metaTextView.setText(data.meta)
                mBinding.nameTextView.setText(data.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_model, parent, false)
        return CardsViewHolder(view, parent)
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        holder.bind(oldList.get(position))
    }

    override fun getItemCount() = oldList.size

    fun pushList(newList: ArrayList<Cards>) {
        val diffutil = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
            }
        }
        val calc = DiffUtil.calculateDiff(diffutil)
        oldList = newList
        exampleListFull = ArrayList(oldList)
        calc.dispatchUpdatesTo(this)
    }
}
