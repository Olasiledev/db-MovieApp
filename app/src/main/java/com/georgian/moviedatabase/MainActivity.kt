package com.georgian.moviedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.database.Cursor
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DB Init
        dbHelper = DataBaseHelper(this)

        // recyclerView
        recyclerView = findViewById(R.id.recyclerListView)
        adapter = MyAdapter(getDataFromDatabase())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getDataFromDatabase(): List<MyDataModel> {
        val data: MutableList<MyDataModel> = mutableListOf()

        // Reading Data
        val db = dbHelper.readableDatabase

        // Querying Database
        val cursor: Cursor = db.rawQuery("SELECT * FROM movies", null)
        Log.i("MyApp", "Row count: ${cursor.count}")

        // Processing Results
        if (cursor.moveToFirst()) {
            do {
                val movieId = cursor.getInt(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_MOVIE_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_TITLE))
                val studio = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_STUDIO))
                val criticsRating = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_CRITICS_RATING))

                // DataModel Instance
                val myDataModel = MyDataModel(
                    movieId,
                    title,
                    studio,
                    criticsRating
                )

                data.add(myDataModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        Log.i("Data", data.toString())

        return data
    }



}

object MovieEntry {
    const val COLUMN_MOVIE_ID = "movieID"
    const val COLUMN_TITLE = "title"
    const val COLUMN_STUDIO = "studio"
    const val COLUMN_CRITICS_RATING = "criticsRating"
}
