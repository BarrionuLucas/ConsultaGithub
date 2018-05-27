package br.com.lbarrionuevo.consultagithub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.lbarrionuevo.consultagithub.Model.Repository;
import br.com.lbarrionuevo.consultagithub.Model.RepositoryJSON;
import br.com.lbarrionuevo.consultagithub.Utils.CircleTransform;
import retrofit2.Callback;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder>  {

    private Context context;
    private List<Repository> repoHistorical;
    protected List<Repository> filteredList;

    Animation animation;
    private int maxPosition = -1;
    public RepoAdapter(List<Repository> mListRepo, Context context) {

        this.repoHistorical = mListRepo;
        this.filteredList = new ArrayList<>();
        this.filteredList.addAll(this.repoHistorical);
        this.context = context;
    }


    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForListItem = R.layout.repository_cell;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachImmediatly = false;

        View view = inflater.inflate(layoutForListItem, parent, attachImmediatly);
        RepoViewHolder viewHolder = new RepoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {

        Repository repo = repoHistorical.get(position);

        if (filteredList.size() >0) {
            repo = filteredList.get(position);
        }

        holder.bind(repo, position, context);

    }


    @Override
    public int getItemCount() {

        if (filteredList.size() >0) {
            return filteredList.size();
        }

        return repoHistorical.size();

    }

    public void searchRepositories( String filter) {
        filteredList.clear();
        if (filter.isEmpty()) {
            filteredList.addAll(repoHistorical);
        } else {
            for (Repository item : repoHistorical) {
                if (item.getNmRepo().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRepo, tvDescRepo, tvUser, tvNmUser, tvStar, tvFork;
        public ImageView ivAvatar, ivFav;
        public RepoViewHolder(View itemView) {
            super(itemView);

            ivAvatar = (ImageView)itemView.findViewById(R.id.ivAvatar);
            ivFav = (ImageView)itemView.findViewById(R.id.ivFav);

            tvRepo = (TextView) itemView.findViewById(R.id.tvRepositotio);
            tvDescRepo = (TextView) itemView.findViewById(R.id.tvDescRepo);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            tvNmUser = (TextView) itemView.findViewById(R.id.tvCreatedAt);
            tvStar = (TextView) itemView.findViewById(R.id.tvStar);
            tvFork = (TextView) itemView.findViewById(R.id.tvFork);
        }


        void bind(final Repository repo, final int pos, final Context context) {
            if(repo.getUrlImage() != " ") {
                ivAvatar.setImageBitmap(null);
            }
            Picasso.with(context)
                    .load(repo.getUrlImage())
                    .transform(new CircleTransform())
                    .into(ivAvatar);
            tvRepo.setText(repo.getNmRepo());
            tvDescRepo.setText(repo.getDescRepo());
            tvUser.setText(repo.getUser());

            tvStar.setText(String.valueOf(repo.getStars()));
            tvFork.setText(String.valueOf(repo.getForks()));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                
                    Intent i = new Intent(context, PullRequestActivity.class);
                    i.putExtra("user", repo.getUser());
                    i.putExtra("repo", repo.getNmRepo());
                    context.startActivity(i);
                }
            });
            ivFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(repo.isFavorited()) {
                        ivFav.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_border_black_24dp));
                        ivFav.setBackgroundColor(context.getResources().getColor(R.color.red));
                        repo.setFavorited(false);
                    }else{
                        ivFav.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_black_24dp));
                        ivFav.setBackgroundColor(context.getResources().getColor(R.color.red));
                        repo.setFavorited(true);
                    }
                }
            });
            if(pos > maxPosition){
                animation = AnimationUtils.loadAnimation(context, R.anim.right_to_left);
                itemView.startAnimation(animation);
                maxPosition = pos;
            }
        }



    }
}
