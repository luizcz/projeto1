package projetoum.equipe.iteach.adapter;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.activities.VisualizarAulaActivity;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.utils.DAO;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassObject> classes = new ArrayList<>();
    private DAO dao;
    private Context mContext;

    public ClassAdapter(Context ctx) {
        mContext = ctx;
        dao = DAO.getInstace(ctx);

        this.classes = new ArrayList<>();
        this.classes.addAll(dao.getClasses());
    }


    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_aula, parent, false);
        return new ClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VisualizarAulaActivity.class);
                intent.putExtra("aula_id", "Nome da Aula");
                v.getContext().startActivity(intent);
            }
        });

        holder.aula_dist.setText(String.valueOf((new Random()).nextInt(500)));
        holder.aula_prof_name.setText(classes.get(position).getTeacherId());
        holder.aula_desc.setText(classes.get(position).getName());
        holder.aula_valor.setText("R$ " + String.valueOf((new Random()).nextInt(100)) + ",00");

    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

//    public void addClass(ClassObject classObject) {
//        classes.add(classObject);
//        notifyDataSetChanged();
//    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView aula_dist;
        TextView aula_prof_name;
        TextView aula_desc;
        TextView aula_valor;

        ClassViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.card_aula);

            cv.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.transparent));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                cv.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.cardback));
            }
            aula_dist = (TextView)itemView.findViewById(R.id.card_aula_dist);
            aula_prof_name = (TextView)itemView.findViewById(R.id.card_aula_prof_name);
            aula_desc = (TextView)itemView.findViewById(R.id.card_aula_desc);
            aula_valor = (TextView)itemView.findViewById(R.id.card_aula_valor);
        }
    }

    public void setClasses(List<ClassObject> classes){
        removeAll();
        this.classes.addAll(classes);
        notifyDataSetChanged();
    }

    public void add(ClassObject item) {
        classes.add(item);
//        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void remove(ClassObject item) {
        int position = classes.indexOf(item);
        classes.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll(){
        classes.clear();
        notifyDataSetChanged();
    }
}
