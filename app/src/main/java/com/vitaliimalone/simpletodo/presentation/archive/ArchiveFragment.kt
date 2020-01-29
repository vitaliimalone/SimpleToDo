package com.vitaliimalone.simpletodo.presentation.archive

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.vitaliimalone.simpletodo.R
import com.vitaliimalone.simpletodo.domain.models.Task
import com.vitaliimalone.simpletodo.presentation.base.BaseFragment
import com.vitaliimalone.simpletodo.presentation.hometab.common.TaskTouchHelperCallback
import com.vitaliimalone.simpletodo.presentation.hometab.common.TasksAdapter
import com.vitaliimalone.simpletodo.presentation.popups.duedatepopup.DueDatePopup
import com.vitaliimalone.simpletodo.presentation.utils.Res
import com.vitaliimalone.simpletodo.presentation.views.DefaultDividerItemDecoration
import kotlinx.android.synthetic.main.archive_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArchiveFragment : BaseFragment(R.layout.archive_fragment) {
    private val viewModel: ArchiveViewModel by viewModel()
    private val tasksAdapter by lazy { TasksAdapter(::onTaskClicked, ::onTaskLongClick) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
        setupClickListeners()
        setupObservers()
    }

    private fun setupViews() {
        toolbar.title = Res.string(R.string.archive_toolbar_title)
        clearAllTextView.text = Res.string(R.string.clear_all)
        archiveRecyclerView.adapter = tasksAdapter
        archiveRecyclerView.addItemDecoration(
            DefaultDividerItemDecoration(
                requireContext(),
                marginLeft = Res.dimen(requireContext(), R.dimen.home_divider_margin),
                marginRight = Res.dimen(requireContext(), R.dimen.home_divider_margin)
            )
        )
        val itemTouchHelper = ItemTouchHelper(
            TaskTouchHelperCallback(
                requireContext(),
                { position, _ -> onTabSwipe(position) },
                Res.string(R.string.delete),
                Res.string(R.string.delete),
                Res.color(requireContext(), R.attr.themeColorError),
                Res.color(requireContext(), R.attr.themeColorError)
            )
        )
        itemTouchHelper.attachToRecyclerView(archiveRecyclerView)
    }

    private fun onTabSwipe(position: Int) {
        val swipedTask = tasksAdapter.tasks[position]
        viewModel.deleteTask(swipedTask)
        val swipedSnackbar = Snackbar.make(
            archiveRecyclerView,
            Res.string(R.string.snackbar_task_deleted),
            Snackbar.LENGTH_LONG
        )
        swipedSnackbar.setAction(Res.string(R.string.snackbar_undo)) {
            viewModel.undoDelete()
        }
        swipedSnackbar.show()
    }

    private fun setupClickListeners() {
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        clearAllTextView.setOnClickListener { viewModel.deleteAllArchivedTasks() }
    }

    private fun setupObservers() {
        viewModel.archivedTasks.observe(viewLifecycleOwner, Observer {
            tasksAdapter.tasks = it
        })
    }

    private fun onTaskClicked(task: Task) {
        val action = ArchiveFragmentDirections.actionArchiveFragmentToTaskDetailsFragment(task)
        findNavController().navigate(action)
    }

    private fun onTaskLongClick(task: Task, coordinates: Point) {
        DueDatePopup(requireContext(), task.dueTo) { pickedDate ->
            viewModel.updateTaskDueDate(task, pickedDate)
        }.run {
            showAtLocation(requireView(), Gravity.NO_GRAVITY, coordinates.x, coordinates.y)
        }
    }
}