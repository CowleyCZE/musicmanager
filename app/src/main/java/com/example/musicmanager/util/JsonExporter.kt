package com.example.musicmanager.util

import android.content.Context
import android.net.Uri
import com.example.musicmanager.data.model.Song
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream

object JsonExporter {
    fun exportSongsToJson(context: Context, songs: List<Song>, outputStream: OutputStream) {
        val jsonArray = JSONArray()
        songs.forEach { song ->
            val jsonObject = JSONObject().apply {
                put("id", song.id)
                put("title", song.title)
                put("audioUri", song.audioUri)
                put("coverUri", song.coverUri)
                put("lyrics", song.lyrics)
                put("musicDescription", song.musicDescription)
                put("sourceTag", song.sourceTag)
                put("note", song.note)
                put("timestamp", song.timestamp)
            }
            jsonArray.put(jsonObject)
        }
        outputStream.write(jsonArray.toString(4).toByteArray())
        outputStream.close()
    }
}
