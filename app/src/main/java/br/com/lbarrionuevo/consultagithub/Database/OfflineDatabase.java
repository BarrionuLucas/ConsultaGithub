package br.com.lbarrionuevo.consultagithub.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.lbarrionuevo.consultagithub.Dao.RepositoryDao;
import br.com.lbarrionuevo.consultagithub.Model.Repository;

@Database(entities = {Repository.class}, version=1)
public abstract class OfflineDatabase extends RoomDatabase {
    public abstract RepositoryDao getRepositoryDao();

}
