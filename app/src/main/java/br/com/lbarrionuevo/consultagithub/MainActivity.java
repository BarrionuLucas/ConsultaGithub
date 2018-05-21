package br.com.lbarrionuevo.consultagithub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.lbarrionuevo.consultagithub.Model.Item;
import br.com.lbarrionuevo.consultagithub.Model.Repository;
import br.com.lbarrionuevo.consultagithub.Model.RepositoryJSON;
import br.com.lbarrionuevo.consultagithub.Service.GithubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.lbarrionuevo.consultagithub.Utils.Constants.API_REPOSITORIES;

public class MainActivity extends AppCompatActivity{


    private List<Repository> repoList = new ArrayList<>();
    private RepoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    Repository repository;
    InputStream inImage;
    LinearLayoutManager layoutManager;
    int pag=1;
    boolean mLoading = false;
    int lastVisibleItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_repo);

        mAdapter = new RepoAdapter(repoList, this);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        getData(pag);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItem = layoutManager.getItemCount();

                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!mLoading && lastVisibleItem == totalItem - 1) {
                    mLoading = true;
                    pag++;

                    getData(pag);

                    mLoading = false;


                    Log.i("pos: ", String.valueOf(repoList.size() -1));
                }
            }
        });
        //mRecyclerView.getLayoutManager().scrollToPosition(((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    public void getData(int pag){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_REPOSITORIES)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        GithubService mainService = retrofit.create(GithubService.class);

        Map<String, String> params = new HashMap<String, String>();

        params.put("q", "language:Kotlin");
        params.put("sort", "stars");
        params.put("page", String.valueOf(pag));

        Call<RepositoryJSON> requestStatus = mainService.listRepos(params);

        requestStatus.enqueue(new Callback<RepositoryJSON>() {
            @Override
            public void onResponse(Call<RepositoryJSON> call, Response<RepositoryJSON> response) {
                if (!response.isSuccessful()) {
                    Log.i("TAG", "Erro!!!!!!!!!: " + response.code());
                } else {
                    RepositoryJSON repo =  response.body();

                    for ( Item item: repo.getItems()) {


                        repository = new Repository(item.getName(),item.getDescription(),  item.getOwner().getLogin(),
                                item.getForksCount(), item.getStargazersCount(), item.getOwner().getAvatarUrl() );

                        repoList.add(repository);
                        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), repoList.size() - 1);
                    }

                }
            }
            @Override
            public void onFailure(Call<RepositoryJSON> call, Throwable t) {
                Log.e( "Erro:", " " + t.getMessage());
                Toast.makeText(MainActivity.this,"Falha na conexão", Toast.LENGTH_LONG).show();
            }


        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

