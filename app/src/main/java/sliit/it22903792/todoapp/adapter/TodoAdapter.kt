package sliit.it22903792.todoapp.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sliit.it22903792.todoapp.R
import sliit.it22903792.todoapp.database.TodoDatabase
import sliit.it22903792.todoapp.database.repositories.TodoRepository
import sliit.it22903792.todoapp.model.Todo
import sliit.it22903792.todoapp.view_model.MainActivityViewModel

class TodoAdapter(context:Context, todoList:List<Todo>, viewModel: MainActivityViewModel) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val context:Context
    private var todoList:List<Todo>
    private var databaseRepo:TodoRepository
    private var viewModel:MainActivityViewModel

    init{
        this.context = context
        this.todoList = todoList
        databaseRepo = TodoRepository(TodoDatabase.getInstance(context))
        this.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_item_view,parent,false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val todo = todoList.get(position)
        holder.todoItemText.text = todo.title
        holder.todoItemDescription.text = todo.description

        //handle priority color
        if(todo.priority == "Very High"){
            holder.todoCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FF5A5A")))
        }else if(todo.priority == "High"){
            holder.todoCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FFCACA")))
        }else{
            holder.todoCardView.setCardBackgroundColor(ColorStateList.valueOf(context.getColor(android.R.color.transparent)))
        }

        holder.itemView.setOnLongClickListener {
            deleteTodoItem(todo,position)
            true
        }

    }

    class TodoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val todoCardView:CardView = itemView.findViewById(R.id.todoCardView)
        val todoItemText: TextView = itemView.findViewById(R.id.todoItemText)
        val todoItemDescription:TextView = itemView.findViewById(R.id.todoItemDescription)

    }

    private fun deleteTodoItem(todo: Todo,position:Int){
        CoroutineScope(Dispatchers.IO).launch {
            databaseRepo.delete(todo)
            val list = databaseRepo.getAllTodo()
            withContext(Dispatchers.Main){
                viewModel.setTodoList(list)
                Toast.makeText(context,"Item deleted",Toast.LENGTH_LONG).show()
            }
        }
    }

}