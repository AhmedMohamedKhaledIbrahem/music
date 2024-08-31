package com.example.music.data

import com.example.music.MusicModelView

data class ArtistModel(
     val firstCharacterArtist:String = "",
     val artistName:String = "",
     val numberOfSongs :List<MusicModelView> = emptyList(),
)
