package com.example.dietapp.ui.Food;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietapp.DownloadService;
import com.example.dietapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        registerServiceReceiver();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    CheckBox vegetarian,vegan,pesce,keto,lactov,ovov,paleo,primal;
    Chip eu,br,fr,ge,ch,am,in,it,gr,ir,ko,sp,je,th,vi,mex,med,no,af,caj,car;
    CheckBox main,dess,appet,salad,breakfast,soup,sauce,snack,drink;
    Chip dairy,egg,gluten,grain,pea,sea,shell,soy,tree,wheat;

    EditText query_input;
    ImageView pipa;
    ServiceReciver reciver;
    ProgressBar progressBar;
    TextView goright;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab =getView().findViewById(R.id.fab);
        fab.setOnClickListener(this);

        query_input=getView().findViewById(R.id.search_name);
        vegetarian=getView().findViewById(R.id.search_veget);
        vegan=getView().findViewById(R.id.search_vegan);

        keto=getView().findViewById(R.id.search_keto);
        lactov=getView().findViewById(R.id.search_lactovega);
        ovov=getView().findViewById(R.id.search_ovovega);
        pesce=getView().findViewById(R.id.search_pescetarian);
        paleo=getView().findViewById(R.id.search_paleo);
        primal=getView().findViewById(R.id.search_primal);

        goright=getView().findViewById(R.id.goright);
        goright.setVisibility(View.INVISIBLE);

        eu=getView().findViewById(R.id.chip_eu);
        br=getView().findViewById(R.id.chip_gb);
        fr=getView().findViewById(R.id.chip_fr);
        ge=getView().findViewById(R.id.chip_ger);
        ch=getView().findViewById(R.id.chip_chi);
        am=getView().findViewById(R.id.chip_american);
        in=getView().findViewById(R.id.chip_indian);
        it=getView().findViewById(R.id.chip_italian);
        gr=getView().findViewById(R.id.chip_greek);
        ir=getView().findViewById(R.id.chip_irish);
        ko=getView().findViewById(R.id.chip_kor);
        sp=getView().findViewById(R.id.chip_spa);
        je=getView().findViewById(R.id.chip_jew);
        th=getView().findViewById(R.id.chip_thai);
        vi=getView().findViewById(R.id.chip_viet);
        mex=getView().findViewById(R.id.chip_mex);
        med=getView().findViewById(R.id.chip_med);

        no=getView().findViewById(R.id.chip_nord);
        af=getView().findViewById(R.id.chip_afr);

        caj=getView().findViewById(R.id.chip_cajun);

        car=getView().findViewById(R.id.chip_carr);
        main=getView().findViewById(R.id.maincourse);
        dess=getView().findViewById(R.id.dessert);
        appet=getView().findViewById(R.id.appetizer);
        salad=getView().findViewById(R.id.salad);
        breakfast=getView().findViewById(R.id.breakfast);
        soup=getView().findViewById(R.id.soup);
        sauce=getView().findViewById(R.id.sauce);
        snack=getView().findViewById(R.id.snack);
        drink=getView().findViewById(R.id.drink);
        dairy=getView().findViewById(R.id.chip_dairy);
        egg=getView().findViewById(R.id.chip_egg);
        gluten=getView().findViewById(R.id.chip_gluten);
        grain=getView().findViewById(R.id.chip_grain);
        pea=getView().findViewById(R.id.chip_peanut);
        sea=getView().findViewById(R.id.chip_sea);

        shell=getView().findViewById(R.id.chip_shell);
        soy=getView().findViewById(R.id.chip_soy);

        tree=getView().findViewById(R.id.chip_tree);
        wheat=getView().findViewById(R.id.chip_wheat);

        progressBar=getView().findViewById(R.id.progressBar2);
        pipa=getView().findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        if (isOnline()) {
            Bundle query_datas = new Bundle();
            query_datas.putBoolean("vegetarian", vegetarian.isChecked());
            query_datas.putBoolean("vegan", vegan.isChecked());

            query_datas.putBoolean("keto", keto.isChecked());
            query_datas.putBoolean("lactov", lactov.isChecked());
            query_datas.putBoolean("ovov", ovov.isChecked());
            query_datas.putBoolean("pesce", pesce.isChecked());
            query_datas.putBoolean("paleo", paleo.isChecked());
            query_datas.putBoolean("primal", primal.isChecked());

            query_datas.putBoolean("eu", eu.isChecked());
            query_datas.putBoolean("br", br.isChecked());
            query_datas.putBoolean("fr", fr.isChecked());
            query_datas.putBoolean("ge", ge.isChecked());
            query_datas.putBoolean("ch", ch.isChecked());
            query_datas.putBoolean("am", am.isChecked());
            query_datas.putBoolean("in", in.isChecked());
            query_datas.putBoolean("it", it.isChecked());
            query_datas.putBoolean("gr", gr.isChecked());
            query_datas.putBoolean("ir", ir.isChecked());
            query_datas.putBoolean("ko", ko.isChecked());
            query_datas.putBoolean("sp", sp.isChecked());
            query_datas.putBoolean("je", je.isChecked());
            query_datas.putBoolean("th", th.isChecked());
            query_datas.putBoolean("vi", vi.isChecked());
            query_datas.putBoolean("mex", mex.isChecked());
            query_datas.putBoolean("med", med.isChecked());

            query_datas.putBoolean("no", no.isChecked());
            query_datas.putBoolean("af", af.isChecked());

            query_datas.putBoolean("caj", caj.isChecked());

            query_datas.putBoolean("car", car.isChecked());
            query_datas.putBoolean("main", main.isChecked());
            query_datas.putBoolean("dess", dess.isChecked());
            query_datas.putBoolean("appet", appet.isChecked());
            query_datas.putBoolean("salad", salad.isChecked());
            query_datas.putBoolean("breakfast", breakfast.isChecked());
            query_datas.putBoolean("soup", soup.isChecked());
            query_datas.putBoolean("sauce", sauce.isChecked());
            query_datas.putBoolean("snack", snack.isChecked());
            query_datas.putBoolean("drink", drink.isChecked());
            query_datas.putBoolean("dairy", dairy.isChecked());
            query_datas.putBoolean("egg", egg.isChecked());
            query_datas.putBoolean("wheat", wheat.isChecked());
            query_datas.putBoolean("gluten", gluten.isChecked());
            query_datas.putBoolean("grain", grain.isChecked());
            query_datas.putBoolean("pea", pea.isChecked());
            query_datas.putBoolean("sea", sea.isChecked());

            query_datas.putBoolean("shell", shell.isChecked());
            query_datas.putBoolean("soy", soy.isChecked());

            query_datas.putBoolean("tree", tree.isChecked());
            query_datas.putString("query", String.valueOf(query_input.getText()));

            DownloadService.startActionFood(getActivity(), query_datas);
            progressBar.setVisibility(View.VISIBLE);
            pipa.setVisibility(View.INVISIBLE);
        } else Toast.makeText(getActivity(),"No internet connection!",Toast.LENGTH_SHORT).show();

    }

    private void registerServiceReceiver(){
        reciver = new ServiceReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.RESULT_INFO);
        getActivity().registerReceiver(reciver,intentFilter);

    }

    private class ServiceReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.INVISIBLE);
            pipa.setVisibility(View.VISIBLE);
            goright.setVisibility(View.VISIBLE);

        }
    }

    public  boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}


