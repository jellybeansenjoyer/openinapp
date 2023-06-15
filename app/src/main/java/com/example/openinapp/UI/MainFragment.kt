package com.example.openinapp.UI

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openinapp.Model.Cards
import com.example.openinapp.Model.Links
import com.example.openinapp.R
import com.example.openinapp.Service.MainViewModel
import com.example.openinapp.UI.adapter.CardsAdapter
import com.example.openinapp.UI.adapter.LinksAdapter
import com.example.openinapp.databinding.FragmentMainBinding
import java.util.ArrayList

private const val TAG = "MainFragment"
class MainFragment : Fragment() {
    lateinit private var topLinks: ArrayList<Links>
    lateinit private var recentLinks: ArrayList<Links>
    lateinit private var mBinding: FragmentMainBinding
    private val _adapter2 = LinksAdapter()
    private val _adapter = CardsAdapter()
    private val viewModel by activityViewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recyclerViewSocial.apply{
            adapter = _adapter
            layoutManager = LinearLayoutManager(this@MainFragment.context,LinearLayoutManager.HORIZONTAL,false)
        }
        viewModel.response.observe(viewLifecycleOwner){
                if(it==null)
                    Log.e("king","null")
                Log.e("king",it.toString())
            recentLinks = viewModel.getLinks()
            topLinks = viewModel.getTopLinks()
            _adapter2.pushList(topLinks)
            Log.e("missi",it.top_source+" a" +it.top_location)
            _adapter.pushList(arrayListOf<Cards>(Cards(R.drawable.cursor,it.total_clicks.toString(),"Today's clicks"),Cards(R.drawable.location,it.top_location,"Top Location"),Cards(R.drawable.globe,it.top_source,"Top source")))
        }
        mBinding.recyclerView2.apply {
            adapter = _adapter2
            layoutManager = LinearLayoutManager(this@MainFragment.context)
        }
        mBinding.recentLinks.setOnClickListener {
            _adapter2.pushList(recentLinks)
        }
        mBinding.topLinksButton.setOnClickListener {
            _adapter2.pushList(topLinks)
        }
    }
}