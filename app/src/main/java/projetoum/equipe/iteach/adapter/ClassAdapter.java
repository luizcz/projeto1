package projetoum.equipe.iteach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.models.ClassObject;

/**
 * Created by Erik on 19/01/2017.
 */

public class ClassAdapter extends BaseAdapter {

    private List<ClassObject> classes;
    private Context context;

    public ClassAdapter(List<ClassObject> classes, Context context) {
        this.classes = classes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Object getItem(int position) {
        return classes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_search, null);
        ClassObject class_object = (ClassObject) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.class_text_view_adapter);
        textView.setText(class_object.getName());
        return view;
    }

    public void addClass(ClassObject classObject) {
        classes.add(classObject);
        notifyDataSetChanged();
    }
}
