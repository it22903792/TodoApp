package sliit.it22903792.todoapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sliit.it22903792.todoapp.model.Todo

class MainActivityViewModel : ViewModel() {

    private val mTodoList = MutableLiveData<List<Todo>>()
    var todoList: LiveData<List<Todo>> = mTodoList

    fun setTodoList(todoList:List<Todo>){
        mTodoList.value = todoList
    }

}