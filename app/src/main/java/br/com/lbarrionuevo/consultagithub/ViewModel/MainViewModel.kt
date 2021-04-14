package br.com.lbarrionuevo.consultagithub.ViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.lbarrionuevo.consultagithub.Model.Repository
import br.com.lbarrionuevo.consultagithub.Repository.RepositoriesRepository

class MainViewModel : ViewModel() {
    private lateinit var _repoList: MutableLiveData<MutableList<Repository>>
    private lateinit var application: Application
    var page = 1;

    val repoList: MutableLiveData<MutableList<Repository>>
    get(){
        return _repoList
    }

    private fun loadRepositories(page: Int) {
        var repositories = MutableLiveData<MutableList<Repository>>()
        repositories.value = mutableListOf<Repository>()
        val repositoriesRepository = RepositoriesRepository(application, repositories)
        repositoriesRepository.getGithubRepository(page)
    }

}