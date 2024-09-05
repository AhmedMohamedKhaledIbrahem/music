package com.example.music.data.model

data class ArtistModel(
     val firstCharacterArtist:String = "",
     val artistName:String = "",
     val numberOfSongs :List<MusicModel> = emptyList(),
)
