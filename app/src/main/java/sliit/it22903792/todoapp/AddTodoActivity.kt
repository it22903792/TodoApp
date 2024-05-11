package sliit.it22903792.todoapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sliit.it22903792.todoapp.database.TodoDatabase
import sliit.it22903792.todoapp.database.repositories.TodoRepository
import sliit.it22903792.todoapp.model.Todo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTodoActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var todoAddTitleInput:TextInputLayout
    private lateinit var todoAddDescInput:TextInputLayout
    private lateinit var todoAddPriorityInput:TextInputLayout
    private lateinit var addTodoBtn: Button

    private var repository:TodoRepository? = null
    private lateinit var autoCompleteTextView: MaterialAutoCompleteTextView
    private var deadlineDate:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        repository = TodoRepository(TodoDatabase.getInstance(this))

        toolbar = findViewById(R.id.toolbar)
        todoAddTitleInput = findViewById(R.id.todoAddTitleInput)
        todoAddDescInput = findViewById(R.id.todoAddDescInput)
        todoAddPriorityInput = findViewById(R.id.todoAddPriorityInput)
        addTodoBtn = findViewById(R.id.addTodoBtn)

        autoCompleteTextView = todoAddPriorityInput.editText!! as MaterialAutoCompleteTextView

        toolbar.title = "Add Todo"
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener {
            finish()
        }

        initializePriority()

        addTodoBtn.setOnClickListener {
            addIem()
        }

    }

    private fun initializePriority(){

        val priorityList = mutableListOf<String>()
        priorityList.add("Very Low")
        priorityList.add("Low")
        priorityList.add("Normal")
        priorityList.add("High")
        priorityList.add("Very High")

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,priorityList)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setText(priorityList[2],false)

    }

    private fun addIem(){

        addTodoBtn.isEnabled = false
        val title = todoAddTitleInput.editText?.text.toString()
        val description = todoAddDescInput.editText?.text.toString()
        val priorityValue = autoCompleteTextView.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            //insert data in table
            repository?.insert(Todo(title,description,priorityValue,0,0,System.currentTimeMillis()))
            //success inserted
            runOnUiThread {
                addTodoBtn.isEnabled = true
                //finish activity
                val intent = Intent()
                setResult(RESULT_OK,intent)
                finish()
            }
        }

    }

    fun deadLinePicker(){

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

            val dateString = i.toString()+"/"+(i2+1)+"/"+i3.toString()
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
            val date = simpleDateFormat.parse(dateString)
            val dateMillis = date?.time

            if(dateMillis != null){
                deadlineDate = dateMillis
            }
        },year,month,day).show()

    }

}