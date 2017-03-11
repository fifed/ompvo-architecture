package com.fifed.architecture.app.mvp.managers_ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fifed.architecture.R;
import com.fifed.architecture.app.activities.interfaces.ActivityStateInterface;
import com.fifed.architecture.app.fragments.core.BaseFragment;
import com.fifed.architecture.app.fragments.utils.FragmentAnimUtils;
import com.fifed.architecture.app.mvp.managers_ui.interfaces.ManagerUIContentActivity;
import com.fifed.architecture.app.mvp.view_data_pack.core.DataPack;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fedir on 01.07.2016.
 */
public abstract class BaseContentActyvityManagerUI implements ManagerUIContentActivity {
    private DrawerLayout drawer;
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private ViewGroup toolbarContainer;

    public BaseContentActyvityManagerUI(AppCompatActivity activity) {
        this.activity = new WeakReference<>(activity).get();
        activity.setContentView(getActivityRootLayout());
        initUI();
        initToolbar();
        initToolbarContainer();
        setToolbarListener();
    }
    protected AppCompatActivity getActivity (){
        return activity;
    }

    protected abstract void initUI();

    protected abstract int getIdFragmentsContainer();

    protected void setDrawer(DrawerLayout drawer){
        this.drawer = drawer;
    }

    protected abstract int getToolbarContainerID();

    protected void initToolbarContainer(){
        if(toolbar != null) {
            toolbarContainer = (ViewGroup) toolbar.findViewById(getToolbarContainerID());
        }
    }


    protected void addFragmentToContainer(BaseFragment fragment, boolean toBackStack, @Nullable DataPack pack) {
        fragment.setDataPack(pack);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fragment.getClass() == getDashBoardFragmentClass()) {
            FragmentAnimUtils.revertAnim();
            BaseFragment dashboardFragment = (BaseFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getDashBoardFragmentClass().getSimpleName());
            dashboardFragment.setDataPack(pack);
            dashboardFragment.reloadedAsNewFragment(true);
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            if(dashboardFragment.isAdded()) {
                dashboardFragment.onReloadFragmentDataWithOutChangeState(pack);
            }
            return;
        }
        BaseFragment sameFragment = (BaseFragment) fm.findFragmentByTag(fragment.getClass().getSimpleName());
        if (sameFragment != null && sameFragment.isAdded()) {
            sameFragment.onReloadFragmentDataWithOutChangeState(pack);
            return;
        }
        boolean containsInBackStack = false;
        if(sameFragment != null) {
            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                if (sameFragment.getClass().getSimpleName().equals(fm.getBackStackEntryAt(i).getName())) {
                    containsInBackStack = true;
                    break;
                }
            }
        }
        if (sameFragment != null && containsInBackStack) {
            FragmentAnimUtils.revertAnim();
            sameFragment.setDataPack(pack);
            sameFragment.reloadedAsNewFragment(true);
            fm.popBackStackImmediate(sameFragment.getClass().getSimpleName(), 0);
        } else {
            if (toBackStack) {
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.fragment_animation_enter, R.anim.fragment_animation_exit,
                        R.anim.fragment_animation_pop_enter, R.anim.fragment_animation_pop_exit)
                        .addToBackStack(fragment.getClass().getSimpleName())
                        .replace(getIdFragmentsContainer(), fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
            } else {
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.fragment_animation_enter, R.anim.fragment_animation_exit,
                        R.anim.fragment_animation_pop_enter, R.anim.fragment_animation_pop_exit)
                        .replace(getIdFragmentsContainer(), fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
            }
        }
    }


    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
    public void setToolbar(Toolbar toolbar){
        this.toolbar = toolbar;
    }
    @Override
    public ViewGroup getToolbarContainer(){
        return toolbarContainer;
    }

    @Override
    public abstract void initToolbar();

    protected void setToolbarListener() {
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag
                            (getDashBoardFragmentClass().getSimpleName());
                    if (fragment != null && fragment.isVisible()) {
                        BaseContentActyvityManagerUI.this.getDrawer().openDrawer(GravityCompat.START);
                    } else activity.onBackPressed();
                }
            });
        }
    }

    protected abstract Class<?> getDashBoardFragmentClass();


    @Override
    public DrawerLayout getDrawer() {
        return drawer;
    }
    protected abstract int getToolbarNavigationIcon();

    @Override
    public void onDestroyActivity() {

    }

    protected List<Fragment> getCurrentAddedFragments(){
        List<Fragment> allFragments =  getActivity().getSupportFragmentManager().getFragments();
        List<Fragment> addedFragments = new ArrayList<>();
        if(allFragments != null) {
            for (Fragment fragment : allFragments) {
                if (fragment != null && fragment.isAdded()) {
                    addedFragments.add(fragment);
                }
            }
        }
        return addedFragments;
    }
    protected boolean isActivityRotated(){
        return ((ActivityStateInterface)getActivity()).isActivityRotated();
    }


    protected void setNavigBtnToolbarLogoGravity(Toolbar toolbar) {
        toolbar.setNavigationIcon(getToolbarNavigationIcon());
        try {
            Field field = toolbar.getClass().getDeclaredField("mNavButtonView");
            field.setAccessible(true);
            try {
                ImageButton button = (ImageButton) field.get(toolbar);
                Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                button.setLayoutParams(params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        toolbar.setNavigationIcon(null);
    }

}
