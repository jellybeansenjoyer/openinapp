package com.example.openinapp.UI

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat


private const val TAG = "MainFragment"
class MainFragment : Fragment(),onItemClick {
    lateinit private var clipboardManager: ClipboardManager
    lateinit private var topLinks: ArrayList<Links>
    lateinit private var recentLinks: ArrayList<Links>
    lateinit private var mBinding: FragmentMainBinding
    private val _adapter2 = LinksAdapter(this)
    private val _adapter = CardsAdapter()
    lateinit private var mGraphMap:Map<String,String>
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
            mGraphMap = viewModel.graphData()
            setFirstAndLastDate()
            makeChart()
            _adapter2.pushList(topLinks)
            Log.e("missi",it.top_source+" a" +it.top_location)
            _adapter.pushList(arrayListOf<Cards>(Cards(R.drawable.cursor,it.total_clicks.toString(),"Today's clicks"),Cards(R.drawable.location,it.top_location,"Top Location"),Cards(R.drawable.globe,it.top_source,"Top source")))
        }
        mBinding.recyclerView2.apply {
            adapter = _adapter2
            layoutManager = LinearLayoutManager(this@MainFragment.context)
        }
      mBinding.chipGroup.setOnCheckedChangeListener { group, checkedId ->

            val chip = view.findViewById<Chip>(R.id.recentLinks)
            val chip2 = view.findViewById<Chip>(R.id.topLinksButton)
            when(checkedId){
                R.id.recentLinks ->{
                    _adapter2.pushList(recentLinks)
                    chip.setChipBackgroundColorResource(R.color.marine)
                    chip.setTextColor(resources.getColor(R.color.white))
                    chip2.setChipBackgroundColorResource(R.color.grey_light)
                    chip2.setTextColor(resources.getColor(R.color.darker))
                }
                R.id.topLinksButton->{
                    _adapter2.pushList(topLinks)
                    chip2.setChipBackgroundColorResource(R.color.marine)
                    chip2.setTextColor(resources.getColor(R.color.white))
                    chip.setChipBackgroundColorResource(R.color.grey_light)
                    chip.setTextColor(resources.getColor(R.color.darker))
                }
            }
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

    fun makeChart(){
        val lineChart = mBinding.lineChart

        // Example dates and corresponding data
        val dates = mGraphMap.keys.toList()
        val _data = mGraphMap.values.toList()

        val entries = ArrayList<Entry>()

        // Prepare the entries for the line graph
        for (i in dates.indices) {
            val value = Entry(i.toFloat(), _data[i].toFloat())
            entries.add(value)
        }

        val lineDataSet = LineDataSet(entries, "Data Set")

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // Customize the X-axis
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = -45f
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)

        // Customize the Y-axis
        val yAxis: YAxis = lineChart.axisLeft
        yAxis.granularity = 1f

        // Disable the right Y-axis
        lineChart.axisRight.isEnabled = false

        // Style the chart
        val startColor = ContextCompat.getColor(requireContext(), R.color.morelessalice)
        val endColor = ContextCompat.getColor(requireContext(), R.color.white)
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColor)
        )

        // Set the shape and bounds of the drawable
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.setBounds(lineChart.left, lineChart.top, lineChart.right, lineChart.bottom)

        // Apply the gradient to the line
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = gradientDrawable
        lineDataSet.color = resources.getColor(R.color.marine)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawValues(false)

        // Refresh the chart
        lineChart.invalidate()
    }
    fun setFirstAndLastDate(){
        val formatter1 = SimpleDateFormat("yyyy-MM-dd")
        val date1 = mGraphMap.keys.toList().first()
        val date2 = mGraphMap.keys.toList().last()
        val date = formatter1.parse(date1)
        val _date = formatter1.parse(date2)
        val formatter2 = SimpleDateFormat("yy MMM")
        val str = formatter2.format(date)
        val str2 = formatter2.format(_date)
        mBinding.dateEnds.setText(str+" - "+str2)
    }
}