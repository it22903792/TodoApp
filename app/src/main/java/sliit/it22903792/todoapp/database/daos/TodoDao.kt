package sliit.it22903792.todoapp.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import sliit.it22903792.todoapp.model.Todo

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAll():List<Todo>

    @Insert
    fun insert(todo: Todo)

    @Delete
    fun delete(todo:Todo)

}