package br.com.lbarrionuevo.consultagithub.Service;

import java.util.ArrayList;
import java.util.Map;

import br.com.lbarrionuevo.consultagithub.Model.PullRequestJSON;
import br.com.lbarrionuevo.consultagithub.Model.RepositoryJSON;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 *
 */

public interface GithubService {
    @GET("search/repositories")
    Call<RepositoryJSON> listRepos(@QueryMap Map< String, String > params);

    @GET("pulls")
    Call<ArrayList<PullRequestJSON>> listPullRequest();
}
