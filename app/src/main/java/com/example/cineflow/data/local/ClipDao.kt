package com.example.cineflow.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Query("SELECT * FROM video_clips ORDER BY timestampMs DESC")
    fun getAllClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM video_clips WHERE id = :clipId")
    suspend fun getClipById(clipId: String): ClipEntity?

    @Query("SELECT * FROM video_clips WHERE isFavorite = 1 ORDER BY timestampMs DESC")
    fun getFavoriteClips(): Flow<List<ClipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: ClipEntity)

    @Update
    suspend fun updateClip(clip: ClipEntity)

    @Delete
    suspend fun deleteClip(clip: ClipEntity)

    @Query("DELETE FROM video_clips WHERE id = :clipId")
    suspend fun deleteClipById(clipId: String)
}
