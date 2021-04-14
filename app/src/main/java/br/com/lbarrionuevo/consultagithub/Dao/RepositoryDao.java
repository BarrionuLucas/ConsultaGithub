package br.com.lbarrionuevo.consultagithub.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.lbarrionuevo.consultagithub.Model.Repository;


@Dao
public interface RepositoryDao {

    @Query("SELECT * FROM Repository")
    List<Repository> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRepository(Repository repository);

    @Query("DELETE FROM Repository")
    void deleteAll();

    @Update
    void updateRepository(Repository repository);

}
