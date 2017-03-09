package projetoum.equipe.iteach.adapter;

/**
 * Created by treinamento-asus on 02/02/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.activities.PreferenciasActivity;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.StringViewHolder> {
    private List<String> mDataset;
    private DAO dao;
    private Context mContext;
    private String newtag;
    private Boolean isClass = false;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TagAdapter(Context ctx, List<String> mDataset) {
        init(ctx, mDataset);
    }

    public TagAdapter(Context ctx, List<String> mDataset, boolean isClass) {
        init(ctx, mDataset);
        this.isClass = isClass;


    }


    private void init(Context ctx, List<String> mDataset) {
        dao = DAO.getInstace(ctx);
        mContext = ctx;
        this.mDataset = mDataset;
        newtag = ctx.getResources().getString(R.string.nova_tag);
        this.mDataset.add(newtag);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);

        return new StringViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StringViewHolder holder, final int position) {


        if (mDataset.get(position).equals(newtag)) {
            holder.label.setVisibility(View.GONE);
            holder.btDelete.setVisibility(View.GONE);

            holder.edt.setVisibility(View.VISIBLE);
            holder.btAdd.setVisibility(View.VISIBLE);

            holder.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add(holder.edt.getText().toString());
                }
            });

            holder.edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    switch (actionId) {

                        case EditorInfo.IME_ACTION_GO:
                            holder.edt.setText(v.getText());
                            add(v.getText().toString());
                            return true;

                        default:
                            return false;
                    }
                }
            });

        } else {
            holder.label.setVisibility(View.VISIBLE);
            holder.btDelete.setVisibility(View.VISIBLE);

            holder.edt.setVisibility(View.GONE);
            holder.btAdd.setVisibility(View.GONE);

            holder.label.setText(mDataset.get(position));
            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos = holder.getAdapterPosition();
                        remove(mDataset.get(pos));
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, mDataset.size());
                    } catch (IndexOutOfBoundsException e) {

                    }
                }
            });
        }

        /*if(mDataset.size()-1 == position && !mDataset.contains(newtag)){
            add(newtag);
        }*/
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(StringViewHolder holder) {
        holder.label.setText("");
        holder.btDelete.setText("X");
        holder.edt.setText("");
        holder.btAdd.setText("+");
        super.onViewRecycled(holder);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class StringViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView btDelete;
        TextView label;
        TextView btAdd;
        EditText edt;

        StringViewHolder(View itemView) {
            super(itemView);

            btDelete = (TextView) itemView.findViewById(R.id.bt_delete);
            label = (TextView) itemView.findViewById(R.id.label);

            btAdd = (TextView) itemView.findViewById(R.id.bt_add);
            edt = (EditText) itemView.findViewById(R.id.edt);


        }
    }

    public List<String> getDataset() {
        return mDataset;
    }

    public void setDataset(List<String> mDataset) {
        this.mDataset.addAll(mDataset);
        mDataset.add(newtag);
    }

    public void add(String item) {

        mDataset.add(0, item);
        notifyItemInserted(0);
        //notifyDataSetChanged();
        if(isClass){}
        else{
        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                param.tags = mDataset;
                param.tags.remove(newtag);
                dao.updateUser(param, new ICallback() {
                    @Override
                    public void execute(Object param) {

                        // mDataset.add(newtag);
                    }
                });
            }
        });
    }
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(item);

        //notifyDataSetChanged();
        //notifyItemRemoved(position);
        if(isClass){}
        else{
        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                param.tags = mDataset;
                param.tags.remove(newtag);
                dao.updateUser(param, new ICallback() {
                    @Override
                    public void execute(Object param) {

                    }
                });
            }
        });
    }
    }

    public void removeAll() {
        mDataset.clear();
        notifyDataSetChanged();
        if(isClass){}
        else{
        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                param.tags = mDataset;
                param.tags.remove(newtag);
                dao.updateUser(param, new ICallback() {
                    @Override
                    public void execute(Object param) {

                    }
                });
            }
        });
    }
    }

    public void update(String item) {
        int position = mDataset.indexOf(item);
        mDataset.set(position, item);
        notifyDataSetChanged();
        if(isClass){
        }
        else {
            dao.getCurrentUser(new ICallback<User>() {
                @Override
                public void execute(User param) {
                    param.tags = mDataset;
                    param.tags.remove(newtag);
                    dao.updateUser(param, new ICallback() {
                        @Override
                        public void execute(Object param) {

                        }
                    });
                }
            });
        }
    }

}