package com.example.tute5_1.repositories

import com.example.tute5_1.database.TodoDatabase
import com.example.tute5_1.database.entities.Todo

class TodoRepository(private val db: TodoDatabase) {
    suspend fun insert(todo: Todo) = db.getTodoDao().insertTodo(todo)
    suspend fun delete(todo: Todo) = db.getTodoDao().delete(todo)
    fun getAllTodos() =db.getTodoDao().getAllTodos()
}