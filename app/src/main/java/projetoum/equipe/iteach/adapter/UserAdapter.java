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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.activities.PerfilActivity;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;
import projetoum.equipe.iteach.utils.Sort;

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
    public void onBindViewHolder(final UserViewHolder holder, final int position) {

        holder.nomeUser.setText(usuarios.get(position).getName());
/*            @Override
            public void execute(User param) {
                if (param!= null && param.myClasses != null) holder.numAulas.setText(param.myClasses.size());
            }
        });*/
       /* final ArrayList<ClassObject> classes = new ArrayList<>();
        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                Log.e("teste",usuarios.get(position).userId);
                dao.findClassesByUser(usuarios.get(position).userId, new ICallback<List<String>>() {
                    @Override
                    public void execute(List<String> param) {
                        for (String classId: param){
                            dao.findClassById(classId, new ICallback<ClassObject>() {
                                @Override
                                public void execute(ClassObject param) {
                                    classes.add(param);
                                }
                            });
                        }
                    }
                });
            }
        });
        */
       if(usuarios.get(position).getMyClasses() != null) {
           holder.numAulas.setText(Integer.toString(usuarios.get(position).getMyClasses().size()));
       }else{
           holder.numAulas.setText("0");
       }
        holder.bio.setText(usuarios.get(position).getBio());


        holder.membroSince.setText(usuarios.get(position).getCreationDate());
        if (usuarios.get(position).getNotas() != null && usuarios.get(position).getNotas().size() > 0) {
            holder.ratingProfessor.setRating(calcularMedia(usuarios.get(position).getNotas()).floatValue());
            holder.ratingProfessor.setVisibility(View.VISIBLE);
            if (usuarios.get(position).getNotas().size() == 1) {
                holder.numAval.setText(String.valueOf(usuarios.get(position).getNotas().size()) + " Avaliação");
            } else {
                holder.numAval.setText(String.valueOf(usuarios.get(position).getNotas().size()) + " Avaliações");
            }
        } else {
            holder.ratingProfessor.setVisibility(View.GONE);
            holder.numAval.setText(R.string.sem_avaliacoes);
        }

        if (usuarios.get(position).getLowResURI() != null && !usuarios.get(position).getLowResURI().isEmpty())
            Picasso.with(mContext).load(usuarios.get(position).getLowResURI()).fit().centerCrop().into(holder.img);


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PerfilActivity.class).putExtra("id", usuarios.get(position).getUserId()));
            }
        });

    }

    private Double calcularMedia(List<Double> l) {
        Double soma = 0d;
        for (Double a : l) {
            if (a != null) {
                soma += a;
            }
        }
        return soma / l.size();
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    @Override
    public void onViewRecycled(UserViewHolder holder) {
        holder.nomeUser.setText("");
        holder.numAulas.setText("-");
        holder.ratingProfessor.setVisibility(View.GONE);
        holder.numAval.setText(R.string.sem_avaliacoes);
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
        TextView numAval;
        RatingBar ratingProfessor;
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
            numAval = (TextView) itemView.findViewById(R.id.prof_num_aval);
            ratingProfessor = (RatingBar) itemView.findViewById(R.id.aula_rating);


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

    public void sort(Sort type) {
        switch (type) {
            case ALPHA:
                Collections.sort(usuarios, new SortByName());
                notifyDataSetChanged();
                break;
//            case RATING:
//                Collections.sort(classes, new SortByRating());
//                notifyDataSetChanged();
//                break;
//            case PRICE:
//                Collections.sort(classes, new ClassAdapter.SortByPrice());
//                notifyDataSetChanged();
//                break;
//            case DISTANCE:
//                Collections.sort(classes, new ClassAdapter.SortByDistance());
//                notifyDataSetChanged();
//                break;
            default:

        }
    }

    private class SortByName implements Comparator<User> {

        @Override
        public int compare(User user1, User user2) {
            return user1.getName().toLowerCase().trim().
                    compareTo(user2.getName().toLowerCase().trim());
        }
    }

//    public class SortByCreationDate implements Comparator {
//
//        @Override
//        public int compare(Object o1, Object o2) {
//            User c1 = (User) o1;
//            User c2 = (User) o2;
//
//            return c1.getCreationDate().compareTo(c2.getCreationDate());
//        }
//    }
}