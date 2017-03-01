package com.melkir.libraries.modules;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melkir.libraries.R;
import com.melkir.libraries.activity.DetailActivity;
import com.melkir.libraries.cards.CardsAdapter;
import com.melkir.libraries.cards.CardsFilterType;
import com.melkir.libraries.model.Module;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class ModulesFragment extends Fragment implements ModulesContract.View {
    private static final String TAG = ModulesFragment.class.getSimpleName();

    @BindView(R.id.recycler_view) RecyclerView mModulesView;
    @BindView(R.id.noModules) TextView mNoModulesView;

    private ModulesContract.Presenter mPresenter;
    private CardsAdapter mCardsAdapter;

    public ModulesFragment() {
        // Requires empty public constructor
    }

    public static ModulesFragment newInstance() {
        return new ModulesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardsAdapter = new CardsAdapter(new ArrayList<Module>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ModulesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, root);

        handleOrientationBehaviour(mModulesView);
        mModulesView.setAdapter(mCardsAdapter);
        mModulesView.setHasFixedSize(true);

        return root;
    }

    @Override
    public void showModules(List<Module> modules) {
        mCardsAdapter.replaceData(modules);

//        mModulesView.setVisibility(View.VISIBLE);
//        mNoModulesView.setVisibility(View.GONE);
    }

    @Override
    public void showNoModules() {
//        mModulesView.setVisibility(View.GONE);
//        mNoModulesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showModuleDetailsUi(Module module) {
        final Context context = getContext();
        final Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.MODULE, module);
        context.startActivity(intent);
    }

    @Override
    public void filter(CardsFilterType requestType) {
        mCardsAdapter.getFilter().filter(requestType.getValue());
    }

    private final ModuleItemListener mItemListener = new ModuleItemListener() {
        @Override
        public void onModuleClick(Module clickedModule) {
            mPresenter.openModuleDetails(clickedModule);
        }
    };

    private void handleOrientationBehaviour(RecyclerView recyclerView) {
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }

    public interface ModuleItemListener {
        void onModuleClick(Module clickedModule);
    }
}
