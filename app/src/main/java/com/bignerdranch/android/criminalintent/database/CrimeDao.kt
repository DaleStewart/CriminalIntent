package com.bignerdranch.android.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.criminalintent.Crime
import java.util.*

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT *  FROM crime WHERE id = (:id)")
    fun getCrime(id: UUID) : LiveData<Crime?>

    //room generates the proper SQL commands for @Update & @Insert annotations
    //don't need to supply them, crime parameter is automatically handled the proper way
    @Update
    fun updateCrime(crime: Crime)

    @Insert
    fun addCrime(crime: Crime)

}