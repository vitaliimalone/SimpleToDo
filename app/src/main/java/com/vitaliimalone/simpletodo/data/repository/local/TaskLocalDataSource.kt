package com.vitaliimalone.simpletodo.data.repository.local

import com.vitaliimalone.simpletodo.data.repository.local.dao.TaskDao
import com.vitaliimalone.simpletodo.data.repository.local.models.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskLocalDataSource(
    private val taskDao: TaskDao
) {
    fun getUnarchivedTasksForPeriod(startDate: String, endDate: String): Flow<List<TaskEntity>> {
        return taskDao.getUnarchivedTasksForPeriod(startDate, endDate)
    }

    suspend fun addTask(taskEntity: TaskEntity) {
        taskDao.addTask(taskEntity)
    }

    suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.updateTask(taskEntity)
    }

    suspend fun deleteTask(taskEntity: TaskEntity) {
        taskDao.deleteTask(taskEntity)
    }

    fun getUnarchivedTasksCountForPeriod(startDate: String, endDate: String): Flow<Int> {
        return taskDao.getUnarchivedTasksCountForPeriod(startDate, endDate)
    }

    fun getArchivedTasksCount(): Flow<Int> {
        return taskDao.getArchivedTasksCount()
    }

    fun getArchivedTasks(): Flow<List<TaskEntity>> {
        return taskDao.getArchivedTasks()
    }

    suspend fun deleteArchivedTasks() {
        taskDao.deleteArchivedTasks()
    }
}