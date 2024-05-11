package sliit.it22903792.todoapp.database.repositories

import sliit.it22903792.todoapp.database.TodoDatabase
import sliit.it22903792.todoapp.model.Todo

class TodoRepository(private val database:TodoDatabase) {

    suspend fun insert(todo:Todo) = database.getDao().insert(todo)
    suspend fun delete(todo:Todo) = database.getDao().delete(todo)
    fun getAllTodo():List<Todo> = database.getDao().getAll();

}