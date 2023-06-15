package com.example.openinapp.UI.adapter

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openinapp.Model.Links
import com.example.openinapp.R
import com.example.openinapp.databinding.LinkModelBinding
import java.text.SimpleDateFormat

private const val TAG = "LinksAdapter"
class LinksAdapter() : RecyclerView.Adapter<LinksAdapter.LinksViewHolder>() {
    private var oldList: ArrayList<Links> = ArrayList()
    private var exampleListFull: List<Links>

    init {
        exampleListFull = ArrayList(oldList)
    }

    class LinksViewHolder(private val view: View, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(view) {
        private val mBinding: LinkModelBinding

        init {
            mBinding = DataBindingUtil.bind(view)!!
        }

        fun bind(data: Links) {
            mBinding.apply {
                mBinding.textViewTitle.setSingleLine()
                mBinding.textViewTitle.ellipsize = TextUtils.TruncateAt.END
                mBinding.linkTextView.setSingleLine()
                mBinding.linkTextView.ellipsize = TextUtils.TruncateAt.END
                val _data = data.metadata.substring(0,data.metadata.indexOf('T'))
                Log.e(TAG,_data)
                val formatter1 = SimpleDateFormat("yyyy-MM-dd")
                val date = formatter1.parse(_data)
                val formatter2 = SimpleDateFormat("dd MMM yyyy")
                val str = formatter2.format(date)
                mBinding.textViewTitle.setText(data.title)
                mBinding.dateTextView.setText(str)
                Glide.with(parent.context).load(data.drawable).into(mBinding.imageView)
                mBinding.clicksTextView.setText(data.clicks)
                mBinding.linkTextView.setText(data.link)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.link_model, parent, false)
        return LinksViewHolder(view, parent)
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        holder.bind(oldList.get(position))
    }

    override fun getItemCount() = oldList.size
    fun clearList(){
        oldList = ArrayList()
    }
    fun pushList(newList: ArrayList<Links>) {
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