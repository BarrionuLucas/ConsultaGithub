package br.com.lbarrionuevo.consultagithub.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.lbarrionuevo.consultagithub.Model.Repository;


@Dao
public interface RepositoryDao {

    @Query("SELECT * FROM Repository")
    List<Repository> getAll();

    @Insert
    void insertRepository(Repository repository);

    @Query("DELETE FROM Repository")
    void deleteAll();

    @Update
    void updateRepository(Repository repository);

}
