package projetoum.equipe.iteach.adapter;

/**
 * Created by treinamento-asus on 02/02/2017.
 */

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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> usuarios = new ArrayList<>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(List<User> usuarios) {
        this.usuarios = usuarios;
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

//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(UserViewHolder holder, final int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//
//        holder.nomeUser.setText(usuarios.get(position).getName());
//
//
//            //Log.d("test",mDataset.get(position).getNomeCursivo()+ " image id "+mDataset.get(position).getResId());
//    }


    // Return the size of your dataset (invoked by the layout manager)
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

        UserViewHolder(View v) {
            super(v);

            nomeUser = (TextView) v.findViewById(R.id.prof_name);
            numAulas = (TextView) v.findViewById(R.id.num_aulas);
            bio = (TextView) v.findViewById(R.id.aula_desc);
            membroSince = (TextView) v.findViewById(R.id.data_membro_desde);
        }
    }
    public List<User> getUsuarios() {
        return usuarios;
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