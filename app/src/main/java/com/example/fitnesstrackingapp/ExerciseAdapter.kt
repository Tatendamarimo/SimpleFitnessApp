package com.example.fitnesstrackingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseAdapter(private val exerciseList: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    // ViewHolder class to hold the views for each item
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textExerciseType: TextView = itemView.findViewById(R.id.textExerciseType)
        val textDuration: TextView = itemView.findViewById(R.id.textDuration)
        val textDistance: TextView = itemView.findViewById(R.id.textDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        // Inflate the item layout for each exercise
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        // Bind the exercise data to the views
        val currentExercise = exerciseList[position]
        holder.textExerciseType.text = currentExercise.exerciseType
        holder.textDuration.text = "Duration: ${currentExercise.duration}"
        holder.textDistance.text = "Distance: ${currentExercise.distance}"
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}
