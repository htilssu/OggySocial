package com.oggysocial.oggysocial.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.google.firebase.firestore.ListenerRegistration
import com.oggysocial.oggysocial.R
import com.oggysocial.oggysocial.utils.PostUtil
import com.oggysocial.oggysocial.utils.UserUtil
import java.time.LocalDateTime


class HomeAdminFragment : Fragment() {
    private var tvTotalUsers: TextView? = null
    private var tvTotalPosts: TextView? = null
    private var listenerRegistration: ListenerRegistration? = null
    private var chartView: AnyChartView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initViews() {
        tvTotalUsers = view?.findViewById(R.id.tvTotalUsers)!!
        tvTotalPosts = view?.findViewById(R.id.tvTotalPosts)!!
        chartView = view?.findViewById(R.id.any_chart_view)!!


        initListeners()
    }

    private fun initListeners() {

    }

    private fun initData() {
        tvTotalPosts?.text = "_"
        tvTotalUsers?.text = "_"

        PostUtil.getTotalPostCount { count ->
            tvTotalPosts?.text = count.toString()
        }

        UserUtil.getTotalUserCount { count ->
            tvTotalUsers?.text = count.toString()
        }


        getPostChartData()
    }

    private fun getPostChartData() {
        val currentMonth = LocalDateTime.now().month
        PostUtil.getPostCountForDayOfMonth(currentMonth, {
            initPostChartData(it)
        }, {
            listenerRegistration = it
        })

    }

    private fun initPostChartData(postCountList: List<Int>) {

        val chartType = AnyChart.line()
        val data = mutableListOf<DataEntry>()
        postCountList.forEachIndexed { index, count ->
            data.add(ValueDataEntry(index + 1, count))
        }

        chartType.data(data)
        chartType.animation(true)
        chartType.title("Thống kê bài đăng")
        chartType.yAxis(0).title("Số bài đăng");
        chartType.xAxis(0).title("Ngày")
        chartType.getSeries(0).name("Bài đăng")
        chartType.padding(10, 10, 10, 10)

        chartView?.setChart(chartType)

    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
        listenerRegistration = null
        tvTotalUsers = null
        tvTotalPosts = null
    }
}