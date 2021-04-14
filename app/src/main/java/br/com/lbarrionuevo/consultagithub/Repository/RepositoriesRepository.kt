package br.com.lbarrionuevo.consultagithub.Repository

import android.app.Application
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import br.com.lbarrionuevo.consultagithub.Database.OfflineDatabase
import br.com.lbarrionuevo.consultagithub.Model.Repository
import br.com.lbarrionuevo.consultagithub.Model.RepositoryJSON
import br.com.lbarrionuevo.consultagithub.Service.GithubService
import br.com.lbarrionuevo.consultagithub.Utils.Constants
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RepositoriesRepository(val application: Application,
                             var repositories: MutableLiveData<MutableList<Repository>>) {
    private val DATABASE_NAME = "OfflineDatabase"

    fun getGithubRepository(page:Int){
        if(isNetworkAvailable){
            getGithubRepositoryFromServer(page)
        }else{
            repositories.value = getGithubRepositoryFromDatabase()
        }
    }

    private fun getGithubRepositoryFromDatabase(): MutableList<Repository>? {
        val offlineDatabase = Room.databaseBuilder(application.applicationContext,
                OfflineDatabase::class.java, DATABASE_NAME)
                .build()

        val repositoryDao = offlineDatabase.repositoryDao

        return repositoryDao.all
    }

    private fun getGithubRepositoryFromServer(page: Int) {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_REPOSITORIES)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val mainService = retrofit.create(GithubService::class.java)
        val params: MutableMap<String, String> = HashMap()
        params["q"] = "language:Kotlin"
        params["sort"] = "stars"
        params["page"] = page.toString()
        val requestStatus = mainService.listRepos(params)
        requestStatus.enqueue(object : Callback<RepositoryJSON> {
            override fun onResponse(call: Call<RepositoryJSON>, response: Response<RepositoryJSON>) {
                if (!response.isSuccessful) {
                    Log.i("TAG", "Erro!: " + response.code())
                } else {
                    val repoList = mutableListOf<Repository>()
                    val repo = response.body()
                    val offlineDatabase = Room.databaseBuilder(application.applicationContext,
                            OfflineDatabase::class.java, DATABASE_NAME)
                            .build()
                    val repositoryDao = offlineDatabase.repositoryDao
                    //
                    for (item in repo.items) {
                        val repository = Repository(
                                item.name,
                                item.description,
                                item.owner.login,
                                item.forksCount,
                                item.stargazersCount,
                                item.owner.avatarUrl)
                        repoList.add(repository)
                        repositoryDao.insertRepository(repository)
                    }
                }
            }

            override fun onFailure(call: Call<RepositoryJSON>, t: Throwable) {
                Log.e("Erro:", " " + t.message)

            }
        })
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = application.applicationContext.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
}