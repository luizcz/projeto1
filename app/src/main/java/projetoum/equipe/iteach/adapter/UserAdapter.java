package projetoum.equipe.iteach.adapter;

/**
 * Created by treinamento-asus on 02/02/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

/**
 * Created by Victor on 4/9/2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> usuarios;
    private DAO dao;
    private Context ctx;

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(Context ctx) {
        dao = DAO.getInstace(ctx);

        usuarios = new ArrayList<>();
        usuarios.addAll(dao.getUsuarios());

        this.ctx = ctx;
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


    // Create new views (invoked by the layout manager)
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_professor, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        holder.nomeUser.setText(usuarios.get(position).getName());
    }

//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
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
    public void onViewRecycled(ViewHolder holder) {
        holder.nomeUser.setText("");
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView nomeUser;

        public ViewHolder(View v) {
            super(v);

            nomeUser = (TextView) v.findViewById(R.id.prof_name);
        }
    }

    public List<User> getUsuarios() {
        return usuarios;
    }
}