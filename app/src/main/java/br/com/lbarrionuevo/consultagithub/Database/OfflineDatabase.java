package br.com.lbarrionuevo.consultagithub.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import br.com.lbarrionuevo.consultagithub.Dao.RepositoryDao;
import br.com.lbarrionuevo.consultagithub.Model.Repository;

@Database(entities = {Repository.class}, version=1)
public abstract class OfflineDatabase extends RoomDatabase {
    public abstract RepositoryDao getRepositoryDao();

}
