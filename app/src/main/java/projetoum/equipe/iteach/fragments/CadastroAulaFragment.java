package projetoum.equipe.iteach.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import projetoum.equipe.iteach.R;

public class CadastroAulaFragment extends Fragment {

    private int layoutId;

    private OnFragmentInteractionListener mListener;
    private EditText tituloEd;
    private EditText assuntoEd;

    public CadastroAulaFragment() {
    }

    public static CadastroAulaFragment newInstance(int layoutId) {
        CadastroAulaFragment fragment = new CadastroAulaFragment();
        Bundle args = new Bundle();
        args.putInt("layoutId", layoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layoutId = getArguments().getInt("layoutId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        if (layoutId == 0 || layoutId == -1)
            v = inflater.inflate(R.layout.cadastro_aula_slide1, container, false);
        else
            v = inflater.inflate(layoutId, container, false);


//        tituloEd = (EditText) v.findViewById(R.id.edt_titulo);
//        assuntoEd = (EditText) v.findViewById(R.id.edt_assunto);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
