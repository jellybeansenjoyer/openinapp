package com.example.openinapp.UI

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openinapp.MainActivity
import com.example.openinapp.Model.Cards
import com.example.openinapp.Model.Links
import com.example.openinapp.R
import com.example.openinapp.Service.MainViewModel
import com.example.openinapp.UI.adapter.CardsAdapter
import com.example.openinapp.UI.adapter.LinksAdapter
import com.example.openinapp.UI.adapter.onItemClick
import com.example.openinapp.databinding.FragmentMainBinding


private const val TAG = "MainFragment"
class MainFragment : Fragment(),onItemClick {
    lateinit private var clipboardManager: ClipboardManager
    lateinit private var topLinks: ArrayList<Links>
    lateinit private var recentLinks: ArrayList<Links>
    lateinit private var mBinding: FragmentMainBinding
    private val _adapter2 = LinksAdapter(this)
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

    override fun onLinkClicked(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
    }
    private fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(this.context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    override fun onCopyClicked(link: String) {
        clipboardManager = (activity as MainActivity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        copyToClipboard(link)
        Toast.makeText(this.context, "Link Copied to Clipboard", Toast.LENGTH_SHORT).show()
    }
}