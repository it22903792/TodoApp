package sliit.it22903792.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sliit.it22903792.todoapp.adapter.TodoAdapter
import sliit.it22903792.todoapp.database.TodoDatabase
import sliit.it22903792.todoapp.database.repositories.TodoRepository
import sliit.it22903792.todoapp.model.Todo
import sliit.it22903792.todoapp.view_model.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private var viewModel:MainActivityViewModel? = null
    private var repository: TodoRepository? = null

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var addTodoItemBtn:FloatingActionButton

    private val addTodoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            if(it.resultCode == RESULT_OK){
                //reload todos
                loadTodos()
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        repository = TodoRepository(TodoDatabase.getInstance(this))

        todoRecyclerView = findViewById(R.id.todoRecyclerView)
        addTodoItemBtn = findViewById(R.id.addTodoItemBtn)

        todoRecyclerView.setHasFixedSize(true)
        todoRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        addTodoItemBtn.setOnClickListener{
            val intent = Intent(this@MainActivity,AddTodoActivity::class.java)
            addTodoLauncher.launch(intent)
        }

        viewModel!!.todoList.observe(this){
            val todoAdapter = TodoAdapter(this,it, viewModel!!)
            todoRecyclerView.adapter = todoAdapter
        }

        loadTodos()

    }

    private fun loadTodos(){

        CoroutineScope(Dispatchers.IO).launch {

            val todos = repository?.getAllTodo()

            runOnUiThread {
                if(todos != null){
                    viewModel!!.setTodoList(todos)
                }
            }

        }

    }


}