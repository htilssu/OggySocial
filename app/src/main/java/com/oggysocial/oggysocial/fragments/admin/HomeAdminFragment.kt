package com.oggysocial.oggysocial.fragments.admin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.oggysocial.oggysocial.R
import com.oggysocial.oggysocial.services.PostService
import com.oggysocial.oggysocial.utils.PostUtil
import java.time.LocalDate


class HomeAdminFragment : Fragment() {

    private lateinit var anyChartView: AnyChartView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initViews() {
        anyChartView = requireView().findViewById(R.id.any_chart_view)
    }

    private fun initListeners() {

    }

    private fun initData() {
        val cartesian: Cartesian = AnyChart.line()

        cartesian.animation(true)

        cartesian.padding(10.0, 20.0, 5.0, 20.0)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true) // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.FLOAT)

        cartesian.title(resources.getString(R.string.monthly_post_analysis))


        cartesian.yAxis(0).title("Số bài đăng")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData: MutableList<DataEntry> = MutableList(31) { ValueDataEntry(0, 0) }
        PostUtil.getPostCountForDayOfMonth(
            LocalDate.now().month,
            object : PostUtil.OnPostCountListener {
                override fun onPostCountReceived(postCountList: List<Int>) {
                    for (i in 1..postCountList.size) {
                        seriesData[i - 1] = ValueDataEntry(i, postCountList[i - 1])
                        Handler(Looper.getMainLooper()).post {
                            val set = com.anychart.data.Set.instantiate()
                            set.data(seriesData)
                            val postMapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")

                            val series1: Line = cartesian.line(postMapping)
                            series1.name("Post")
                            series1.hovered().markers().enabled(true)
                            series1.hovered().markers()
                                .type(MarkerType.CIRCLE)
                                .size(4.0)
                            series1.tooltip()
                                .position("right")
                                .anchor(Anchor.LEFT_CENTER)
                                .offsetX(5.0)
                                .offsetY(5.0)


                            cartesian.legend().enabled(true)
                            cartesian.legend().fontSize(13.0)
                            cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)
                            anyChartView.setChart(cartesian)
                        }
                    }
                }

            })



        anyChartView.setChart(cartesian)
    }


}