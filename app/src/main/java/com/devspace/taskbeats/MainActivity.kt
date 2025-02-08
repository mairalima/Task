package com.devspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskBeatDataBase::class.java,
            "database-task-beat"
        ).build()
    }

    private val categoryDao: CategoryDao by lazy {
        db.getCategoryDao()
    }

    private val taskDao: TaskDao by lazy {
        db.getTaskDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        val rvTask = findViewById<RecyclerView>(R.id.rv_tasks)

        val taskAdapter = TaskListAdapter()
        val categoryAdapter = CategoryListAdapter()

        categoryAdapter.setOnClickListener { selected ->
          /*  val categoryTemp = categories.map { item ->
                when {
                    item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                    item.name == selected.name && item.isSelected -> item.copy(isSelected = false)
                    else -> item
                }
            }

            val taskTemp =
                if (selected.name != "ALL") {
                    tasks.filter { it.category == selected.name }
                } else {
                    tasks
                }
            taskAdapter.submitList(taskTemp)
            categoryAdapter.submitList(categoryTemp)*/
        }

        rvCategory.adapter = categoryAdapter
        getCategoriesFromDataBase(categoryAdapter)

        rvTask.adapter = taskAdapter
        getTasksFromDataBase(taskAdapter) // Corrigi o nome da instância
    }


    private fun getCategoriesFromDataBase(adapter: CategoryListAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            val categoriesFromDb: List<CategoryEntity> = categoryDao.getAll()
            val categoriesUiData = categoriesFromDb.map {
                CategoryUiData(
                    name = it.name,
                    isSelected = it.isSelected
                )
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(categoriesUiData)
            }
        }
    }

    private fun getTasksFromDataBase(adapter: TaskListAdapter) { // Corrigi a indentação e a sintaxe
        lifecycleScope.launch(Dispatchers.IO) {
            val tasksFromDb: List<TaskEntity> = taskDao.getAll() // Corrigi a chamada do DAO
            val tasksUiData = tasksFromDb.map {
                TaskUiData(
                    name = it.name,
                    category = it.category
                )
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(tasksUiData)
            }
        }
    }
}

