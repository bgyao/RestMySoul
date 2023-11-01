package com.example.restmysoul.ui.goals

import android.content.Intent
import android.database.Cursor
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restmysoul.NewGoalActivity
import com.example.restmysoul.database.DatabaseHandler
import com.example.restmysoul.database.ReadDetailsModelClass
import com.example.restmysoul.databinding.FragmentGoalsBinding
import kotlinx.android.synthetic.main.fragment_goals.*


class GoalsFragment : Fragment(), ItemSelectListener {
    private lateinit var goalsAdapter: GoalsAdapter
    private var _binding: FragmentGoalsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        _binding!!.buttonNewGoal.setOnClickListener {
            newGoalClick()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val databaseHandler = DatabaseHandler(this@GoalsFragment.context)
       // var addReadingDetails = ArrayList<ReadDetailsModelClass>()
       // val readingList = databaseHandler.getUserId()?.let { databaseHandler.viewReadingPlans(it) }

        val readingList = databaseHandler.viewReadingPlans(databaseHandler.getUserId())
        /* databaseHandler.readingLogCount()?.toList {
            val readingPlans = ReadDetailsModelClass(
                readingdetails_id = it.getColumnIndex("readingPlandetails_id"),
                u_id = it.getColumnIndex("u_id"),
                numchapters = it.getColumnIndex("num_chapters"),
                numdays = it.getColumnIndex("num_days"),
                is_done = it.getColumnIndex("is_done"))
            addReadingDetails.add(readingPlans)
        }*/
        loadData(readingList)
    }

    private fun loadData(list: ArrayList<ReadDetailsModelClass>?) {
        goalsAdapter.submitList(list)
        if (list != null) {
            text_noGoals.visibility = View.GONE
            rv_goals.visibility = View.VISIBLE
        }
        else rv_goals.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        rv_goals.apply {
            goalsAdapter = GoalsAdapter(context, this@GoalsFragment)
            adapter = goalsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun newGoalClick() {
        val intent = Intent(this@GoalsFragment.context, NewGoalActivity::class.java)
        startActivity(intent)
    }

    override fun onItemSelected(position: Int) {
        Toast.makeText(context,"Updating this goal coming soon!", Toast.LENGTH_LONG).show()
    }


}
fun <T> Cursor.toList(block: (Cursor) -> T) : List<T> {
    return mutableListOf<T>().also { list ->
        if (moveToFirst()) {
            do {
                list.add(block.invoke(this))
            } while (moveToNext())
        }
    }
}
