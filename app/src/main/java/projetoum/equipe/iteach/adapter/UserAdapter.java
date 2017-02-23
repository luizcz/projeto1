package projetoum.equipe.iteach.adapter;

/**
 * Created by treinamento-asus on 02/02/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.activities.PerfilActivity;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> usuarios;
    private DAO dao;
    private Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(Context ctx) {
        dao = DAO.getInstace(ctx);
        mContext = ctx;
        this.usuarios = new ArrayList<>();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_professor, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {

        holder.nomeUser.setText(usuarios.get(position).getName());
        holder.numAulas.setText(String.valueOf(new Random().nextInt(100)));
        holder.bio.setText(usuarios.get(position).getBio());

        holder.membroSince.setText(usuarios.get(position).getCreationDate());
        if (usuarios.get(position).getLowResURI() != null && !usuarios.get(position).getLowResURI().isEmpty())
            Picasso.with(mContext).load(usuarios.get(position).getLowResURI()).fit().centerCrop().into(holder.img);


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PerfilActivity.class).putExtra("id",usuarios.get(position).getUserId()));
            }
        });

    }


    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    @Override
    public void onViewRecycled(UserViewHolder holder) {
        holder.nomeUser.setText("");
        super.onViewRecycled(holder);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class UserViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView nomeUser;
        TextView numAulas;
        TextView bio;
        TextView membroSince;
        CircleImageView img;

        UserViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.card_professor);

            cv.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.transparent));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                cv.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.cardback));
            }
            nomeUser = (TextView) itemView.findViewById(R.id.prof_name);
            numAulas = (TextView) itemView.findViewById(R.id.num_aulas);
            bio = (TextView) itemView.findViewById(R.id.card_aula_name);
            membroSince = (TextView) itemView.findViewById(R.id.data_membro_desde);
            img = (CircleImageView) itemView.findViewById(R.id.img);



        }
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios.addAll(usuarios);
    }

    public void add(User item) {
        usuarios.add(item);
        notifyItemInserted(usuarios.size() - 1);
        // notifyDataSetChanged();
    }

    public void remove(User item) {
        int position = usuarios.indexOf(item);
        usuarios.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        usuarios.clear();
        notifyDataSetChanged();
    }

    public void update(User item) {
        int position = usuarios.indexOf(item);
        usuarios.set(position, item);
        notifyDataSetChanged();
    }
}