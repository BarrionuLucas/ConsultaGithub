package br.com.lbarrionuevo.consultagithub.ui

import androidx.appcompat.app.AppCompatActivity
import br.com.lbarrionuevo.consultagithub.RepoAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.lbarrionuevo.consultagithub.Database.OfflineDatabase
import br.com.lbarrionuevo.consultagithub.Dao.RepositoryDao
import android.os.Bundle
import br.com.lbarrionuevo.consultagithub.R
import androidx.room.Room
import br.com.lbarrionuevo.consultagithub.ui.MainActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import br.com.lbarrionuevo.consultagithub.Service.GithubService
import br.com.lbarrionuevo.consultagithub.Model.RepositoryJSON
import android.widget.Toast
import android.app.SearchManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import br.com.lbarrionuevo.consultagithub.Model.Repository
import br.com.lbarrionuevo.consultagithub.Utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity() {
    private var repoList: MutableList<Repository> = ArrayList()
    private lateinit var mAdapter: RepoAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    var pag = 1
    var mLoading = false
    var currentItem = 0
    var totalItem = 0
    var scrollOutItem = 0
    lateinit var repositoryDao: RepositoryDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mRecyclerView = findViewById<View>(R.id.rv_repo) as RecyclerView
        layoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        repoList = repositoryDao.getAll()

        mAdapter = RepoAdapter(repoList, this, repositoryDao)
        mRecyclerView.adapter = mAdapter
    }

    override fun onPostResume() {
        super.onPostResume()
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mLoading = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem = layoutManager!!.childCount
                totalItem = layoutManager!!.itemCount
                scrollOutItem = layoutManager!!.findFirstVisibleItemPosition()
                if (mLoading && currentItem + scrollOutItem == totalItem) {
                    pag++
                    getData(pag)
                    mLoading = false
                }
            }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mAdapter!!.searchRepositories(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter!!.searchRepositories(newText)
                return true
            }
        })
        return true
    }



}