package projetoum.equipe.iteach;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

/**
 * Created by treinamento-asus on 09/02/2017.
 */

public class SearchAulasFragment extends android.support.v4.app.Fragment{
    private List<ClassObject> aulas;
    private RecyclerView rv;
    private View view;
    private DAO dao;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        FragmentActivity fragmentActivity = (FragmentActivity) super.getActivity();
        view = inflater.inflate(R.layout.content_search, container, false);


        rv=(RecyclerView)view.findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        mContext = container.getContext();
        dao = DAO.getInstace(mContext);

        aulas = new ArrayList<>();
        aulas = dao.getClasses();

        ClassAdapter adapter = new ClassAdapter(aulas);
        rv.setAdapter(adapter);

        return view;
    }





}
