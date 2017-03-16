package projetoum.equipe.iteach.adapter;

/**
 * Created by treinamento-asus on 02/02/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.activities.VisualizarAulaActivity;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.FeedItem;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class FeedAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<FeedItem> mDataset;
    private DAO dao;
    private Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FeedAdapter(Context ctx, List<FeedItem> mDataset) {
        init(ctx, mDataset);
    }


    private void init(Context ctx, List<FeedItem> mDataset) {
        dao = DAO.getInstace(ctx);
        mContext = ctx;
        this.mDataset = mDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_aula_feed, parent, false);
                return new ClassViewHolder(v);
            case FeedItem.TYPE_FRIEND:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_aula_feed, parent, false);
                return new FriendViewHolder(v);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 0:
                ((ClassViewHolder) holder).cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), VisualizarAulaActivity.class);
                        intent.putExtra("aula_id", mDataset.get(holder.getAdapterPosition()).getAula().getId());
                        intent.putExtra("position", holder.getAdapterPosition());
                        v.getContext().startActivity(intent);
                    }
                });

                if (mDataset.get(position).getAula().getImagem() != null && !mDataset.get(position).getAula().getImagem().isEmpty())
                    Picasso.with(mContext).load(mDataset.get(position).getAula().getImagem()).fit().centerCrop().into(((ClassViewHolder) holder).card_aula_img);

                dao.getCurrentUser(new ICallback<User>() {
                    @Override
                    public void execute(User param) {
                        if (param.getLat() != null && param.getLon() != null && mDataset.get(position).getAula().getLat() != null && mDataset.get(position).getAula().getLon() != null) {
                            Location startPoint = new Location("user");
                            startPoint.setLatitude(param.getLat());
                            startPoint.setLongitude(param.getLon());

                            Location endPoint = new Location("class");
                            endPoint.setLatitude(mDataset.get(position).getAula().getLat());
                            endPoint.setLongitude(mDataset.get(position).getAula().getLon());

                            double distance = startPoint.distanceTo(endPoint);

                            DecimalFormat df = new DecimalFormat("#0.0");
                            ((ClassViewHolder) holder).aula_dist.setText(String.valueOf(df.format(distance / 1000)) + "Km");

                        } else {
                            ((ClassViewHolder) holder).aula_dist.setText("?");
                        }
                    }
                });

                dao.findUserById(mDataset.get(position).getAula().getTeacherId(), new ICallback() {
                    @Override
                    public void execute(Object param) {
                        User user = (User) param;
                        ((ClassViewHolder) holder).aula_prof_name.setText(user.getName());
                    }
                });

                ((ClassViewHolder) holder).aula_name.setText(mDataset.get(position).getAula().getName());
                ((ClassViewHolder) holder).aula_desc.setText(mDataset.get(position).getAula().getDescription());

                String valor = mDataset.get(position).getAula().getValorFormatado();
                if (valor.equals("0")) {
                    ((ClassViewHolder) holder).aula_valor.setText(R.string.free);
                } else {
                    ((ClassViewHolder) holder).aula_valor.setText(valor);
                }

                switch (mDataset.get(position).subtype) {
                    case FeedItem.TYPE_CLASS_SUBTYPE_SUBSCRIBE:
                        ((ClassViewHolder) holder).type_text.setText(mContext.getResources().getString(R.string.feed_card_class_sub));
                        ((ClassViewHolder) holder).type_img.setImageResource(R.drawable.ic_sub);
                        break;
                    case FeedItem.TYPE_CLASS_SUBTYPE_ANNOUNCE:
                        ((ClassViewHolder) holder).type_text.setText(mContext.getResources().getString(R.string.feed_card_class_announce));
                        ((ClassViewHolder) holder).type_img.setImageResource(R.drawable.ic_ad);
                        break;
                    case FeedItem.TYPE_CLASS_SUBTYPE_NOTIFY:
                        ((ClassViewHolder) holder).type_text.setText(mContext.getResources().getString(R.string.feed_card_class_alert));
                        ((ClassViewHolder) holder).type_img.setImageResource(R.drawable.ic_alert);
                        break;
                    case FeedItem.TYPE_CLASS_SUBTYPE_LOCATION:
                        ((ClassViewHolder) holder).type_text.setText(mContext.getResources().getString(R.string.feed_card_class_distance));
                        ((ClassViewHolder) holder).type_img.setImageResource(R.drawable.ic_pin_small);
                        break;
                }
                break;
            case FeedItem.TYPE_FRIEND:
                break;
        }


    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /*@Override
    public void onViewRecycled(ViewHolder holder) {
        switch (holder.getItemViewType()) {
            case FeedItem.TYPE_CLASS:
                ((ClassAdapter.ClassViewHolder) holder).aula_dist.setText("");
                ((ClassAdapter.ClassViewHolder) holder).aula_prof_name.setText("");
                ((ClassAdapter.ClassViewHolder) holder).aula_name.setText("");
                ((ClassAdapter.ClassViewHolder) holder).aula_desc.setText("");
                ((ClassAdapter.ClassViewHolder) holder).aula_valor.setText("");
                super.onViewRecycled(holder);
                break;
            case FeedItem.TYPE_FRIEND:
                break;
        }
    }*/

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).type;
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView card_aula_img;
        ImageView type_img;
        TextView type_text;
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
            type_img = (ImageView) itemView.findViewById(R.id.type_img);
            card_aula_img = (ImageView) itemView.findViewById(R.id.card_aula_img);
            aula_dist = (TextView) itemView.findViewById(R.id.card_aula_dist);
            aula_prof_name = (TextView) itemView.findViewById(R.id.card_aula_prof_name);
            aula_name = (TextView) itemView.findViewById(R.id.card_aula_name);
            aula_desc = (TextView) itemView.findViewById(R.id.card_aula_desc);
            aula_valor = (TextView) itemView.findViewById(R.id.card_aula_valor);
            type_text = (TextView) itemView.findViewById(R.id.type_text);


        }
    }


    public class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView btDelete;
        TextView label;
        TextView btAdd;
        EditText edt;

        FriendViewHolder(View itemView) {
            super(itemView);

           /* btDelete = (TextView) itemView.findViewById(R.id.bt_delete);
            label = (TextView) itemView.findViewById(R.id.label);

            btAdd = (TextView) itemView.findViewById(R.id.bt_add);
            edt = (EditText) itemView.findViewById(R.id.edt);*/


        }
    }


    public List<FeedItem> getDataset() {
        return mDataset;
    }

    public void setDataset(List<FeedItem> mDataset) {
        this.mDataset.addAll(mDataset);
    }

    public void add(FeedItem item) {

        mDataset.add(0, item);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, mDataset.size());
    }


    public void remove(FeedItem item, int pos) {
        int position = pos;
        if (pos == -1) position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, mDataset.size());
    }

    public void removeAll() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    public void update(FeedItem item) {
        int position = mDataset.indexOf(item);
        mDataset.set(position, item);
        notifyDataSetChanged();
        notifyItemRangeChanged(0, mDataset.size());
    }
}