package com.georgian.moviedatabase
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DataBaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "movie_database.db"
        private const val DATABASE_VERSION = 2
    }

    init {
        //Checking for DataBase
        val checkIfExists = checkDatabase()

        if (!checkIfExists) {
            this.readableDatabase
            try {
                getDataBaseFiles()
            } catch (e: IOException) {
                throw Error("Error loading database")
            }
        }
    }

    private fun checkDatabase(): Boolean {
        val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath
        Log.d("DatabasePath", "Database path: $dbPath")
        return File(dbPath).exists()
    }


    private fun getDataBaseFiles() {
        val input: InputStream = context.resources.openRawResource(R.raw.movie_database)
        val output: OutputStream = FileOutputStream(context.getDatabasePath(DATABASE_NAME))

        val buffer = ByteArray(1024)
        var length: Int
        while (input.read(buffer).also { length = it } > 0) {
            output.write(buffer, 0, length)
        }

        output.flush()
        output.close()
        input.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}
