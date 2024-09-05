package com.example.music

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.example.music.data.model.MusicModel
import com.example.music.data.model.ArtistModel
import java.io.File

fun getMusicFile(context: Context): List<MusicModel> {
    val musicList = mutableListOf<MusicModel>()
    val contentResolver = context.contentResolver
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        )
    val selectionMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    val cursor: Cursor? = contentResolver.query(uri, projection, selectionMusic, null, null)
    cursor?.use { cursors ->
        val dataIndex = cursors.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val titleIndex = cursors.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val authorIndex = cursors.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

        if (cursors.moveToFirst()) {
            do {
                val title = cursors.getString(titleIndex)
                val author = cursors.getString(authorIndex)
                val uriData = cursors.getString(dataIndex)
                val file = File(uriData)
                Log.e("fileImage", "$file")
                Log.e("authorIndex","$authorIndex")
                try {
                    if (file.exists() &&
                        uriData.endsWith(".mp3", ignoreCase = true) &&
                        uriData.startsWith("/storage/emulated/0/Music/")
                    ) {
                        musicList.add(
                            MusicModel(
                                uri = uriData,
                                musicTitle = title,
                                authorMusic = author,
                                )
                        )

                        Log.e("uriData", uriData)
                    } else {
                        Log.e("MusicListError", "File not found: $uriData")
                    }

                } catch (e: Exception) {
                    Log.e("MusicListError", "Error processing file: $uriData", e)
                }

            } while (cursors.moveToNext())
        }
    }
    cursor?.close()
    return musicList
}
fun getArtistFile(context: Context): List<ArtistModel> {
    val musicMap = mutableMapOf<String, MutableList<MusicModel>>()
    val contentResolver = context.contentResolver
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
    )
    val selectionMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    val cursor: Cursor? = contentResolver.query(uri, projection, selectionMusic, null, null)
    cursor?.use { cursors ->
        val dataIndex = cursors.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val titleIndex = cursors.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val authorIndex = cursors.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

        if (cursors.moveToFirst()) {
            do {
                val title = cursors.getString(titleIndex)
                var author = cursors.getString(authorIndex) ?: "Unknown artist"
                val uriData = cursors.getString(dataIndex)
                val file = File(uriData)
                Log.e("fileImage", "$file")
                Log.e("authorIndex", author)
                if (author == "<unknown>") {
                    author = "Unknown artist"
                }
                try {
                    if (file.exists() &&
                        uriData.endsWith(".mp3", ignoreCase = true) &&
                        uriData.startsWith("/storage/emulated/0/Music/")
                    ) {
                        val song = MusicModel(title, uriData)
                        if (author in musicMap) {
                            musicMap[author]?.add(song)
                        } else {
                            musicMap[author] = mutableListOf(song)
                        }

                        Log.e("uriData", uriData)
                    } else {
                        Log.e("MusicListError", "File not found: $uriData")
                    }

                } catch (e: Exception) {
                    Log.e("MusicListError", "Error processing file: $uriData", e)
                }

            } while (cursors.moveToNext())
        }
    }
    cursor?.close()

    return musicMap.map { (author, songs) ->
        ArtistModel(
            firstCharacterArtist = author.first().uppercaseChar().toString(),
            artistName = author,
            numberOfSongs = songs
        )
    }
}



