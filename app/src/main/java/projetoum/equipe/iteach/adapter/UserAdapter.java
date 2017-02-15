package projetoum.equipe.iteach.adapter;

/**
 * Created by treinamento-asus on 02/02/2017.
 */

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> usuarios;
    private DAO dao;

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(Context ctx) {
        dao = DAO.getInstace(ctx);

        this.usuarios =  new ArrayList<>();
        this.usuarios.addAll(dao.getUsuarios());
    }


    // Create new views (invoked by the layout manager)
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_professor, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        holder.nomeUser.setText(usuarios.get(position).getName());
        holder.numAulas.setText(String.valueOf(new Random().nextInt(100)));
        holder.bio.setText(usuarios.get(position).getBio());

        Calendar cal = Calendar.getInstance();
        holder.membroSince.setText(cal.get(Calendar.MONTH) + " de " + cal.get(Calendar.YEAR));
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
        private TextView nomeUser;
        private TextView numAulas;
        private TextView bio;
        private TextView membroSince;
        CardView cv;

        UserViewHolder(View v) {
            super(v);

            cv = (CardView)itemView.findViewById(R.id.card_professor);

            cv.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.transparent));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                cv.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.cardback));
            }
            nomeUser = (TextView) v.findViewById(R.id.prof_name);
            numAulas = (TextView) v.findViewById(R.id.num_aulas);
            bio = (TextView) v.findViewById(R.id.card_aula_desc);
            membroSince = (TextView) v.findViewById(R.id.data_membro_desde);
        }
    }
    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios){
        this.usuarios.addAll(usuarios);
    }

    public void add(User item) {
        usuarios.add(item);
//        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void remove(User item) {
        int position = usuarios.indexOf(item);
        usuarios.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll(){
        usuarios.clear();
        notifyDataSetChanged();
    }
}