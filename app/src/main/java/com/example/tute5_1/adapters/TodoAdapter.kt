package com.example.tute5_1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tute5_1.R
import com.example.tute5_1.database.TodoDatabase
import com.example.tute5_1.database.entities.Todo
import com.example.tute5_1.repositories.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//extend the TodoAdapter to Recycleview
class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    lateinit var data: List<Todo>
    lateinit var context: Context

    //ViewHolder is default item, but here we are extend it and create our own viewholder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbTodo: CheckBox
        val ivDelete: ImageView

        init {
            cbTodo = view.findViewById(R.id.cbTodo)
            ivDelete = view.findViewById(R.id.ivDelete)
        }
    }

    fun setData(data: List<Todo>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }

    //This function help to connect with view_item.xml file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        return ViewHolder(view)
    }

    //This function assign values to view_item.xml file items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cbTodo.text = data[position].item

        //checked Todos delete button become red
        holder.cbTodo.setOnClickListener {
            if (holder.cbTodo.isChecked)
                holder.ivDelete.setImageResource(R.drawable.delete_icon_selected)
            else
                holder.ivDelete.setImageResource(R.drawable.ic_baseline_delete_forever_24)
        }
        holder.ivDelete.setOnClickListener {
            if (holder.cbTodo.isChecked) {
                val repository = TodoRepository(TodoDatabase.getInstance(context))
                holder.cbTodo.isChecked = false
                holder.ivDelete.setImageResource(R.drawable.delete_icon_selected)

                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(data[position])
                    val data = repository.getAllTodos()
                    withContext(Dispatchers.Main) {
                        setData(data, context)
                        holder.ivDelete.setImageResource(R.drawable.ic_baseline_delete_forever_24)
                    }
                }
            } else {
                Toast.makeText(context, "Cannot delete unmarked Todo items", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}