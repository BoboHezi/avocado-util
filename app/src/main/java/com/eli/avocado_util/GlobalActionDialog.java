package com.eli.avocado_util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalActionDialog extends Dialog {

    private static final String TAG = "GlobalActionDialog";

    private ViewGroup mInitPage;

    private ViewGroup mConfirmPage;
    final Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation, boolean isReverse) {
        }

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {
            mInitPage.setVisibility(View.GONE);
            mConfirmPage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    private RecyclerView mActionList;
    private ActionListAdapter mAdapter;
    private List<Action> mActions;

    public GlobalActionDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyle);
    }

    public GlobalActionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        window.getAttributes().systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        setContentView(R.layout.dialog_global_action);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.dialog_anim);

        initData(context);
        initView(context);
    }

    private void initData(Context context) {
        mActions = new ArrayList<>();
        mActions.add(new PowerAction(context));
        mActions.add(new AirplaneAction(context));
        mActions.add(new RebootAction(context));
        mActions.add(new SoundAction(context));
        //fadeIn.addListener(animatorListener);
    }

    private void initView(Context context) {
        mInitPage = findViewById(R.id.init_page);
        mConfirmPage = findViewById(R.id.confirm_page);
        mInitPage.setVisibility(View.VISIBLE);
        mConfirmPage.setVisibility(View.INVISIBLE);
        mActionList = findViewById(R.id.action_list);
        mAdapter = new ActionListAdapter();
        mActionList.setAdapter(mAdapter);
        mActionList.setLayoutManager(new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false));
    }

    private void showConfirm(boolean animate) {
        if (animate) {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(mInitPage, "alpha", 1, 0)
                    .setDuration(500);
            fadeOut.addListener(animatorListener);
            mConfirmPage.setVisibility(View.VISIBLE);
            fadeOut.start();
        } else {
            mInitPage.setVisibility(View.GONE);
            mConfirmPage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (mConfirmPage.getVisibility() == View.VISIBLE
                && mInitPage.getVisibility() == View.GONE) {
            ObjectAnimator fadeOut = ObjectAnimator
                    .ofFloat(mConfirmPage, "alpha", 1, 0)
                    .setDuration(500);
            ObjectAnimator fadeIn = ObjectAnimator
                    .ofFloat(mInitPage, "alpha", 0, 1)
                    .setDuration(500);
            fadeIn.start();
            fadeOut.start();
        } else {
            super.onBackPressed();
        }*/
    }

    private interface Action {
        CharSequence getLabel();

        Drawable getIcon();

        void onPress(View v);

        boolean showDuringKeyguard();

        boolean isEnabled();

        int getType();
    }

    /**
     * An action that also supports long press.
     */
    private interface LongPressAction extends Action {
        boolean onLongPress();
    }

    private static abstract class SinglePressAction implements Action {
        private final Drawable mIcon;
        private final CharSequence mMessage;

        protected SinglePressAction(Context context, int iconResId, int messageResId) {
            mMessage = context.getString(messageResId);
            mIcon = context.getResources().getDrawable(iconResId);
        }

        protected SinglePressAction(Drawable icon, CharSequence message) {
            mMessage = message;
            mIcon = icon;
        }

        @Override
        public CharSequence getLabel() {
            return mMessage;
        }

        @Override
        public Drawable getIcon() {
            return mIcon;
        }
    }

    class ActionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        final int TYPE_NORMAL = 0;

        final int TYPE_SOUND = 1;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_NORMAL) {
                return new ActionItemHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.item_normal_view, parent, false));
            } else if (viewType == TYPE_SOUND) {
                return new ActionSoundHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.item_sound_view, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder == null) {
                return;
            }
            Action action = mActions.get(position);
            if (getItemViewType(position) == TYPE_NORMAL) {
                ActionItemHolder actionHolder = (ActionItemHolder) holder;
                actionHolder.icon.setImageDrawable(action.getIcon());
                actionHolder.message.setText(action.getLabel());
                actionHolder.itemView.setOnClickListener(v -> action.onPress(v));
            } else if (getItemViewType(position) == TYPE_SOUND) {
                ActionSoundHolder soundHolder = (ActionSoundHolder) holder;
                View.OnClickListener listener = v -> {
                    List<ViewGroup> inactive = new ArrayList<>(
                            Arrays.asList(soundHolder.mute, soundHolder.vibrate, soundHolder.ring));
                    inactive.remove(v);
                    ((ViewGroup) v).getChildAt(1).setVisibility(View.VISIBLE);
                    for (ViewGroup group : inactive) {
                        group.getChildAt(1).setVisibility(View.INVISIBLE);
                    }
                };
                soundHolder.mute.setOnClickListener(listener);
                soundHolder.vibrate.setOnClickListener(listener);
                soundHolder.ring.setOnClickListener(listener);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mActions.get(position).getType();
        }

        @Override
        public int getItemCount() {
            return mActions.size();
        }

        class ActionItemHolder extends RecyclerView.ViewHolder {
            private ImageView icon;
            private TextView message;

            public ActionItemHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(android.R.id.icon);
                message = itemView.findViewById(android.R.id.message);
            }
        }

        class ActionSoundHolder extends RecyclerView.ViewHolder {
            private ViewGroup mute;
            private ViewGroup vibrate;
            private ViewGroup ring;

            public ActionSoundHolder(@NonNull View itemView) {
                super(itemView);
                mute = itemView.findViewById(R.id.sound_mute);
                vibrate = itemView.findViewById(R.id.sound_vibrate);
                ring = itemView.findViewById(R.id.sound_ring);
            }
        }
    }

    private class PowerAction extends SinglePressAction {

        protected PowerAction(Context context) {
            super(context, R.drawable.ic_action_poweroff, R.string.global_action_power_off);
        }

        @Override
        public void onPress(View v) {
            showConfirm(true);
        }

        @Override
        public boolean showDuringKeyguard() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public int getType() {
            return mAdapter.TYPE_NORMAL;
        }
    }

    private class AirplaneAction extends SinglePressAction {

        protected AirplaneAction(Context context) {
            super(context, R.drawable.ic_action_airplane, R.string.global_action_airplane);
        }

        @Override
        public void onPress(View v) {
            showConfirm(true);
        }

        @Override
        public boolean showDuringKeyguard() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public int getType() {
            return mAdapter.TYPE_NORMAL;
        }
    }

    private class RebootAction extends SinglePressAction {

        protected RebootAction(Context context) {
            super(context, R.drawable.ic_action_reboot, R.string.global_action_reboot);
        }

        @Override
        public void onPress(View v) {
            showConfirm(true);
        }

        @Override
        public boolean showDuringKeyguard() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public int getType() {
            return mAdapter.TYPE_NORMAL;
        }
    }

    private class SoundAction extends SinglePressAction {

        protected SoundAction(Context context) {
            super(null, null);
        }

        @Override
        public void onPress(View v) {

        }

        @Override
        public boolean showDuringKeyguard() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public int getType() {
            return mAdapter.TYPE_SOUND;
        }
    }
}
