package projetoum.equipe.iteach.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.activities.VisualizarAulaActivity;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.Constants;
import projetoum.equipe.iteach.utils.DAO;
import projetoum.equipe.iteach.utils.Sort;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassObject> classes = new ArrayList<>();
    private DAO dao;
    private Context mContext;
    private Location mLocation = new Location("Me");


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

        if (classes.get(position).getImagem() != null && !classes.get(position).getImagem().isEmpty())
            Picasso.with(mContext).load(classes.get(position).getImagem()).fit().centerCrop().into(holder.card_aula_img);

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                if (param.getLat() != null && param.getLon() != null && classes.get(position).getLat() != null && classes.get(position).getLon() != null) {
                    mLocation.setLatitude(param.getLat());
                    mLocation.setLongitude(param.getLon());

                    Location startPoint = new Location("user");
                    startPoint.setLatitude(param.getLat());
                    startPoint.setLongitude(param.getLon());

                    Location endPoint = new Location("class");
                    endPoint.setLatitude(classes.get(position).getLat());
                    endPoint.setLongitude(classes.get(position).getLon());

                    double distanceInMeters = startPoint.distanceTo(endPoint) / 1000;
                    holder.aula_dist.setText(getFormatedDistance(distanceInMeters));

                } else {
                    holder.aula_dist.setText("?");
                }
            }
        });

        dao.findUserById(classes.get(position).getTeacherId(), new ICallback() {
            @Override
            public void execute(Object param) {
                User user = (User) param;
                holder.aula_prof_name.setText(user.getName());
                if(user.getUserId().equals(dao.getFireBaseUser().getUid())){
                    holder.aula_prof_name.setTextColor(Color.GREEN);
                    holder.logo_professor.setColorFilter(Color.GREEN);
                }else{
                    holder.aula_prof_name.setTextColor(Color.WHITE);
                    holder.logo_professor.setColorFilter(Color.WHITE);
                }
            }
        });

        holder.aula_name.setText(classes.get(position).getName());
        holder.aula_desc.setText(classes.get(position).getDescription());

        String valor = classes.get(position).getValorFormatado();
        if (valor.equals("0")) {
            holder.aula_valor.setText(R.string.free);
        } else {
            holder.aula_valor.setText(valor);
        }

    }

    private String getFormatedDistance(double distance) {
        DecimalFormat df;
        if (distance < Constants.KM_IN_METERS) {
            df = new DecimalFormat("#3");
            return String.valueOf(df.format(distance)) + "m";
        } else {
            df = new DecimalFormat("#0.0");
            return String.valueOf(df.format(distance / Constants.KM_IN_METERS)) + "Km";
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView card_aula_img;
        ImageView logo_professor;
        TextView aula_dist;
        TextView aula_prof_name;
        TextView aula_name;
        TextView aula_desc;
        TextView aula_valor;


        ClassViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.card_aula);

            cv.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.transparent));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                cv.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.cardback));
            }
            card_aula_img = (ImageView) itemView.findViewById(R.id.card_aula_img);
            aula_dist = (TextView) itemView.findViewById(R.id.card_aula_dist);
            aula_prof_name = (TextView) itemView.findViewById(R.id.card_aula_prof_name);
            aula_name = (TextView) itemView.findViewById(R.id.card_aula_name);
            aula_desc = (TextView) itemView.findViewById(R.id.card_aula_desc);
            aula_valor = (TextView) itemView.findViewById(R.id.card_aula_valor);
            logo_professor = (ImageView) itemView.findViewById(R.id.image_professor_card_aula);


        }
    }


    public List<ClassObject> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassObject> classes) {
        removeAll();
        this.classes.addAll(classes);
        notifyDataSetChanged();
    }

    public void add(ClassObject item) {
        classes.add(item);
        notifyItemInserted(classes.size() - 1);
        //notifyDataSetChanged();
    }

    public void remove(ClassObject item) {
        int position = classes.indexOf(item);
        classes.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        classes.clear();
        notifyDataSetChanged();
    }

    public void update(ClassObject item) {
        int position = classes.indexOf(item);
        classes.set(position, item);
        notifyDataSetChanged();
    }

    public void sort(Sort type) {
        switch (type) {
            case ALPHA:
                Collections.sort(classes, new SortByName());
                notifyDataSetChanged();
                break;
//            case RATING:
//                Collections.sort(classes, new SortByRating());
//                notifyDataSetChanged();
//                break;
            case PRICE:
                Collections.sort(classes, new SortByPrice());
                notifyDataSetChanged();
                break;
            case DISTANCE:
                Collections.sort(classes, new SortByDistance());
                notifyDataSetChanged();
                break;
            default:

        }
    }

    public class SortByName implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            ClassObject c1 = (ClassObject) o1;
            ClassObject c2 = (ClassObject) o2;

            return c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase());
        }
    }

//    public class SortByRating implements Comparator{
//
//        @Override
//        public int compare(Object o1, Object o2) {
//            ClassObject c1 = (ClassObject) o1;
//            ClassObject c2 = (ClassObject) o2;
//
//            return c1.getName().compareTo(c2.getName());
//        }
//    }

    public class SortByPrice implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            ClassObject c1 = (ClassObject) o1;
            ClassObject c2 = (ClassObject) o2;

            return c1.getValue().compareTo(c2.getValue());
        }
    }

    public class SortByDistance implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            ClassObject c1 = (ClassObject) o1;
            ClassObject c2 = (ClassObject) o2;

            if (Double.valueOf(mLocation.getLatitude()) == null ||
                    Double.valueOf(mLocation.getLongitude()) == null ||
                    c1.getLat() == null ||
                    c1.getLon() == null ||
                    c2.getLat() == null ||
                    c2.getLon() == null) {
                return -1;
            }

            Location locationMe = new Location("Me");
            locationMe.setLatitude(mLocation.getLatitude());
            locationMe.setLongitude(mLocation.getLongitude());

            Location locationA = new Location("A");
            locationA.setLatitude(c1.getLat());
            locationA.setLongitude(c1.getLon());

            Location locationB = new Location("B");
            locationB.setLatitude(c2.getLat());
            locationB.setLongitude(c2.getLon());


            Double distanceMeToClass1 = Double.valueOf(locationMe.distanceTo(locationA));
            Double distanceMeToClass2 = Double.valueOf(locationMe.distanceTo(locationB));

            return distanceMeToClass1.compareTo(distanceMeToClass2);

        }
    }
}
