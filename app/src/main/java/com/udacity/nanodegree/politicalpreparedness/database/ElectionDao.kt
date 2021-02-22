package com.udacity.nanodegree.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.nanodegree.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
    //Added insert query
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg election: Election)

    //Added select all election query
    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    //Added select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election

    //Added delete query
    @Delete
    suspend fun deleteElection(election: Election)

    //Added clear query
    @Query("DELETE FROM election_table")
    suspend fun clear()

    @Update
    suspend fun updateElection(election: Election)

    @Query("SELECT * FROM election_table WHERE isFollowed = 1")
    fun getFollowedElections(): LiveData<List<Election>>


}