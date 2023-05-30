package com.example.tute5_1

import com.example.tute5_1.adapters.TodoAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tute5_1.database.TodoDatabase
import com.example.tute5_1.database.entities.Todo
import com.example.tute5_1.repositories.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = TodoRepository(TodoDatabase.getInstance(this))
        val recyclerView: RecyclerView = findViewById(R.id.rvTodoList)
        val ui = this
        val adapter = TodoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val btnAddTodo = findViewById<Button>(R.id.btnAddTodo)


        btnAddTodo.setOnClickListener {
            displayDialog(repository, adapter)
        }


        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodos()
            adapter.setData(data, ui)
        }
    }

    fun displayDialog(repository: TodoRepository, adapter: TodoAdapter) {
        // Create a new instance of AlertDialog.Builder
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title and message
        builder.setTitle("Enter New Todo item:")
        builder.setMessage("Enter the todo item below:")


        // Create an EditText input field
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)


        // Set the positive button action
        builder.setPositiveButton("OK") { dialog, which ->
            // Get the input text and display a Toast message
            if (input.text.isEmpty()) {
                Toast.makeText(this, "Please type the To do", Toast.LENGTH_LONG).show()
            } else {
                val item = input.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insert(Todo(item))
                    val data = repository.getAllTodos()
                    runOnUiThread {
                        adapter.setData(data, this@MainActivity)
                    }
                }
            }
        }

        // Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        // Create and show the alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }
}