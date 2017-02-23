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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.activities.VisualizarAulaActivity;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassObject> classes = new ArrayList<>();
    private DAO dao;
    private Context mContext;

    public ClassAdapter(Context ctx) {
        mContext = ctx;
        dao = DAO.getInstace(ctx);

        this.classes = new ArrayList<>();
    }


    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_aula, parent, false);
        return new ClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ClassViewHolder holder, final int position) {

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VisualizarAulaActivity.class);
                intent.putExtra("aula_id", classes.get(position).getId());
                intent.putExtra("position", position);
                v.getContext().startActivity(intent);
            }
        });

        holder.aula_dist.setText(String.valueOf((new Random()).nextInt(500)));
        dao.findUserById(classes.get(position).getTeacherId(), new ICallback() {
            @Override
            public void execute(Object param) {
                User user = (User) param;
                holder.aula_prof_name.setText(user.getName());
            }
        });

        holder.aula_desc.setText(classes.get(position).getDescription());

        String valor = classes.get(position).getValorFormatado();
        if (valor.equals("0")){
            holder.aula_valor.setText(R.string.free);
        } else {
            holder.aula_valor.setText(valor);
        }
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
        ImageView card_aula_img;
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
            card_aula_img = (ImageView) itemView.findViewById(R.id.card_aula_img);
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
        notifyItemInserted(classes.size()-1);
        //notifyDataSetChanged();
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

    public void update(ClassObject item){
        int position = classes.indexOf(item);
        classes.set(position,item);
        notifyDataSetChanged();
    }
}
