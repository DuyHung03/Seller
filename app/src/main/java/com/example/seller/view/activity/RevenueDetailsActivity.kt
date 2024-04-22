package com.example.seller.view.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seller.R
import com.example.seller.adapter.RevenueAdapter
import com.example.seller.databinding.ActivityRevenueDetailsBinding
import com.example.seller.entity.Order
import com.example.seller.viewmodel.DataViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class RevenueDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRevenueDetailsBinding
    private val dataViewModel by viewModels<DataViewModel>()
    private lateinit var simpleDateFormat: SimpleDateFormat
    private val listOrder: MutableList<Order> = mutableListOf()
    private lateinit var dateRangePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>
    private lateinit var revenueAdapter: RevenueAdapter
    private val calendar = Calendar.getInstance().apply {
        add(Calendar.MONTH, -1)
    }
    private lateinit var currentDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revenue_details)
        binding = ActivityRevenueDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataViewModel.getOrders()
        simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dataViewModel.ordersLiveData.observe(this) { list ->
            list?.let {
                listOrder.clear()
                listOrder.addAll(list)
                val startDate = simpleDateFormat.format(calendar.time)
                currentDate = simpleDateFormat.format(Date())
                setRangeDateQuery(startDate, currentDate)
            }
        }

        dateRangePicker = createDatePicker()

        binding.closeBtn.setOnClickListener {
            this.finish()
        }



        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            // Handle the selected date range
            val startDateSelected = selection.first ?: return@addOnPositiveButtonClickListener
            val endDateSelected = selection.second ?: return@addOnPositiveButtonClickListener

            // Process selected dates here
            val startCalendar = Calendar.getInstance().apply { timeInMillis = startDateSelected }
            val endCalendar = Calendar.getInstance().apply { timeInMillis = endDateSelected }

            // Convert to your desired date format
            val formattedStartDate = simpleDateFormat.format(startCalendar.time)
            val formattedEndDate = simpleDateFormat.format(endCalendar.time)

            // Show the selected date range
            setRangeDateQuery(formattedStartDate, formattedEndDate)
        }

    }

    private fun createDatePicker(): MaterialDatePicker<androidx.core.util.Pair<Long, Long>> {

        val startDate = calendar.timeInMillis // Get the day before one month from today
        val endDate = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(endDate)
            .setValidator(DateValidatorPointBackward.now()) // Disable dates after today

        return MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(
                androidx.core.util.Pair(
                    startDate,
                    endDate
                )
            )
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    private fun showDatePickerDialog() {
        if (!dateRangePicker.isAdded) {
            binding.pickerLoading.visibility = View.VISIBLE
            dateRangePicker.show(supportFragmentManager, "TAG")
            binding.pickerLoading.visibility = View.GONE
        }
    }

    private fun setRangeDateQuery(startDate: String, endDate: String) {
        //show loading
        binding.progressBar.visibility = View.VISIBLE

        //set text date range
        binding.rangeDate.text = "$startDate   to   $endDate"

        //filter list by selected date range
        val selectedList = listOrder.filter { order ->
            order.confirmed &&
                    (order.createdTime.after(simpleDateFormat.parse(startDate)) ||
                            simpleDateFormat.format(order.createdTime) == startDate) &&
                    order.createdTime.before(simpleDateFormat.parse(endDate))
        }

        //calculate total revenue of selected list
        val total = selectedList.sumOf { order: Order ->
            order.cost.productsCost
        }

        //set total text
        binding.total.text =
            getString(R.string.total_title, selectedList.size.toString(), total.toString())

        //setup recycler view
        revenueAdapter = RevenueAdapter(selectedList) { order ->
            toOrderDetailsScreen(order)
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = revenueAdapter

        //hide loading
        binding.progressBar.visibility = View.GONE
    }

    private fun toOrderDetailsScreen(order: Order) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra("order", order)
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
                .toBundle()
        )
    }
}
