package kekmech.ru.repository.room

import android.arch.persistence.room.*
import kekmech.ru.core.dto.User

@Dao
interface UserDao {
    @Query("select * from users")
    fun getAll(): List<User>

    @Query("select * from users where id = :id")
    fun getById(id: Int): User

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}