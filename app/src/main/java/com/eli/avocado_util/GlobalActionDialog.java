package com.eli.avocado_util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.Log;
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

public class GlobalActionDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "GlobalActionDialog";

    private static final int ANIMATION_DELAY = 300;

    private static final int STATUS_INIT = 1;
    private static final int STATUS_CONFIRM = 2;
    private static final int STATUS_TO_INIT = 3;
    private static final int STATUS_TO_CONFIRM = 4;

    private int mCurrentStatus = STATUS_INIT;

    private ViewGroup mInitPage;

    private ViewGroup mConfirmPage;

    private TextView mConfirmTitle;
    private TextView mConfirmSubTitle;

    private String mConfirmLabel;
    private String mConfirmSummary;

    final Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation, boolean isReverse) {
            if (mCurrentStatus == STATUS_TO_INIT) {
                mInitPage.setVisibility(View.VISIBLE);
            } else if (mCurrentStatus == STATUS_TO_CONFIRM) {
                mConfirmTitle.setText(mConfirmLabel);
                mConfirmSubTitle.setText(mConfirmSummary);
                mConfirmPage.setVisibility(View.VISIBLE);
                mConfirmPage.setAlpha(1);
            } else if (mCurrentStatus == STATUS_INIT) {
            } else if (mCurrentStatus == STATUS_CONFIRM) {
            }
        }

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {
            if (mCurrentStatus == STATUS_TO_CONFIRM) {
                mCurrentStatus = STATUS_CONFIRM;
                switchConfirm(true);
            } else if (mCurrentStatus == STATUS_TO_INIT) {
                mCurrentStatus = STATUS_INIT;
                switchConfirm(false);
            } else if (mCurrentStatus == STATUS_INIT) {
            } else if (mCurrentStatus == STATUS_CONFIRM) {
            }
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

        mConfirmTitle = mConfirmPage.findViewById(R.id.title);
        mConfirmSubTitle = mConfirmPage.findViewById(R.id.sub_title);

        mConfirmPage.findViewById(R.id.negative).setOnClickListener(this);
        mConfirmPage.findViewById(R.id.positive).setOnClickListener(this);
    }

    private void showConfirm(boolean animate) {
        if (animate) {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(mInitPage, "alpha", 1, 0)
                    .setDuration(ANIMATION_DELAY);
            mCurrentStatus = STATUS_TO_CONFIRM;
            fadeOut.addListener(animatorListener);
            fadeOut.start();
        } else {
            mCurrentStatus = STATUS_CONFIRM;
            switchConfirm(true);
        }
    }

    private void showInit(boolean animate) {
        if (animate) {
            mCurrentStatus = STATUS_TO_INIT;
            ObjectAnimator fadeOut = ObjectAnimator
                    .ofFloat(mConfirmPage, "alpha", 1, 0)
                    .setDuration(ANIMATION_DELAY);
            ObjectAnimator fadeIn = ObjectAnimator
                    .ofFloat(mInitPage, "alpha", 0, 1)
                    .setDuration(ANIMATION_DELAY);
            fadeOut.addListener(animatorListener);
            fadeIn.start();
            fadeOut.start();
        } else {
            mCurrentStatus = STATUS_INIT;
            switchConfirm(false);
        }
    }

    private void switchConfirm(boolean showConfirm) {
        mInitPage.setVisibility(showConfirm ? View.GONE : View.VISIBLE);
        mConfirmPage.setVisibility(!showConfirm ? View.GONE : View.VISIBLE);
        (showConfirm ? mConfirmPage : mInitPage).setAlpha(1);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentStatus == STATUS_CONFIRM) {
            showInit(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: " + (view.getId() == R.id.positive ? "positive" : "negative"));
        dismiss();
    }

    private interface Action {
        CharSequence getLabel();

        Drawable getIcon();

        void onPress();

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

    private static abstract class ToggleAction implements Action {

        protected final Drawable mEnabledIcon;
        protected final CharSequence mEnabledMessage;
        protected final Drawable mDisabledIcon;
        protected final CharSequence mDisabledMessage;
        protected State mState = State.Off;
        protected ToggleAction(Context context, int enabledIconResId, int enabledMessageResId,
                               int disabledIconResId, int disabledMessageResId) {
            this(context.getResources().getDrawable(enabledIconResId),
                    context.getResources().getString(enabledMessageResId),
                    context.getResources().getDrawable(disabledIconResId),
                    context.getResources().getString(disabledMessageResId));
        }

        protected ToggleAction(Drawable enabledIcon, String enabledMessage,
                               Drawable disabledIcon, String disabledMessage) {
            mEnabledIcon = enabledIcon;
            mEnabledMessage = enabledMessage;
            mDisabledIcon = disabledIcon;
            mDisabledMessage = disabledMessage;
        }

        @Override
        public final void onPress() {
            if (mState.inTransition()) {
                Log.w(TAG, "shouldn't be able to toggle when in transition");
                return;
            }
            final boolean nowOn = !(mState == State.On);
            onToggle(nowOn);
            changeStateFromPress(nowOn);
        }

        /**
         * Implementations may override this if their state can be in on of the intermediate
         * states until some notification is received (e.g airplane mode is 'turning off' until
         * we know the wireless connections are back online
         *
         * @param buttonOn Whether the button was turned on or off
         */
        protected void changeStateFromPress(boolean buttonOn) {
            mState = buttonOn ? State.On : State.Off;
        }

        abstract void onToggle(boolean on);

        public void updateState(State state) {
            mState = state;
        }

        enum State {
            Off(false),
            TurningOn(true),
            TurningOff(true),
            On(false);

            private final boolean inTransition;

            State(boolean intermediate) {
                inTransition = intermediate;
            }

            public boolean inTransition() {
                return inTransition;
            }
        }
    }

    class ActionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        final int TYPE_MULTISTAGE = 0;

        final int TYPE_SOUND = 1;

        final int TYPE_DISABLE = 2;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_MULTISTAGE) {
                return new ActionItemHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.item_normal_view, parent, false));
            } else if (viewType == TYPE_SOUND) {
                return new ActionSoundHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.item_sound_view, parent, false));
            }
            return new EmptyHolder(new View(getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder == null) {
                return;
            }
            Action action = mActions.get(position);
            int type = getItemViewType(position);
            if (type == TYPE_MULTISTAGE) {
                ActionItemHolder actionHolder = (ActionItemHolder) holder;
                actionHolder.icon.setImageDrawable(action.getIcon());
                actionHolder.message.setText(action.getLabel());
                actionHolder.itemView.setOnClickListener(v -> action.onPress());
            } else if (type == TYPE_SOUND) {
                SoundAction soundAction = (SoundAction) action;
                ActionSoundHolder soundHolder = (ActionSoundHolder) holder;
                switchSoundView(soundHolder, soundAction.getSoundMode());
                View.OnClickListener listener = v -> {
                    int sound = v == soundHolder.mute ? SoundAction.SOUND_MUTE :
                            v == soundHolder.vibrate ? SoundAction.SOUND_VIBRATE : SoundAction.SOUND_RING;
                    switchSoundView(soundHolder, sound);
                    soundAction.setSoundMode(sound);
                };
                soundHolder.mute.setOnClickListener(listener);
                soundHolder.ring.setOnClickListener(listener);
                if (!soundAction.hasVibrator()) {
                    soundHolder.vibrate.setVisibility(View.GONE);
                } else {
                    soundHolder.vibrate.setOnClickListener(listener);
                }
            }
        }

        private void switchSoundView(ActionSoundHolder soundHolder, int sound) {
            List<ViewGroup> inactive = new ArrayList<>(
                    Arrays.asList(soundHolder.mute, soundHolder.vibrate, soundHolder.ring));
            View active = sound == SoundAction.SOUND_MUTE ? soundHolder.mute :
                    sound == SoundAction.SOUND_VIBRATE ? soundHolder.vibrate : soundHolder.ring;
            inactive.remove(active);
            ((ViewGroup) active).getChildAt(1).setVisibility(View.VISIBLE);
            for (ViewGroup group : inactive) {
                group.getChildAt(1).setVisibility(View.INVISIBLE);
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
            private final ImageView icon;
            private final TextView message;

            public ActionItemHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(android.R.id.icon);
                message = itemView.findViewById(android.R.id.message);
            }
        }

        class ActionSoundHolder extends RecyclerView.ViewHolder {
            private final ViewGroup mute;
            private final ViewGroup vibrate;
            private final ViewGroup ring;

            public ActionSoundHolder(@NonNull View itemView) {
                super(itemView);
                mute = itemView.findViewById(R.id.sound_mute);
                vibrate = itemView.findViewById(R.id.sound_vibrate);
                ring = itemView.findViewById(R.id.sound_ring);
            }
        }

        class EmptyHolder extends RecyclerView.ViewHolder {
            public EmptyHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    private class PowerAction extends SinglePressAction {

        protected PowerAction(Context context) {
            super(context, R.drawable.ic_action_poweroff, R.string.global_action_power_off);
        }

        @Override
        public void onPress() {
            mConfirmLabel = getContext().getString(R.string.global_action_power_off);
            mConfirmSummary = getContext().getString(R.string.global_action_power_off_summary);
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
            return !isEnabled() ? mAdapter.TYPE_DISABLE : mAdapter.TYPE_MULTISTAGE;
        }
    }

    private class AirplaneAction extends ToggleAction {

        protected AirplaneAction(Context context) {
            super(context, R.drawable.ic_action_airplane, R.string.global_action_airplane
                    , R.drawable.ic_action_airplane_disabled, R.string.global_action_airplane);
            // default state
            mState = State.Off;
        }

        @Override
        public CharSequence getLabel() {
            if (mState == State.On || mState == State.TurningOff) {
                return mEnabledMessage;
            } else {
                return mDisabledMessage;
            }
        }

        @Override
        public Drawable getIcon() {
            if (mState == State.On || mState == State.TurningOff) {
                return mEnabledIcon;
            } else {
                return mDisabledIcon;
            }
        }

        @Override
        void onToggle(boolean on) {
            Log.i(TAG, "onToggle on: " + on);
        }

        @Override
        protected void changeStateFromPress(boolean buttonOn) {
            Log.i(TAG, "changeStateFromPress buttonOn: " + buttonOn);
            super.changeStateFromPress(buttonOn);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateState(State state) {
            Log.i(TAG, "updateState state: " + state);
            super.updateState(state);
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
            return !isEnabled() ? mAdapter.TYPE_DISABLE : mAdapter.TYPE_MULTISTAGE;
        }
    }

    private class RebootAction extends SinglePressAction {

        protected RebootAction(Context context) {
            super(context, R.drawable.ic_action_reboot, R.string.global_action_reboot);
        }

        @Override
        public void onPress() {
            mConfirmLabel = getContext().getString(R.string.global_action_reboot);
            mConfirmSummary = getContext().getString(R.string.global_action_reboot_summary);
            showConfirm(true);
        }

        @Override
        public boolean showDuringKeyguard() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public int getType() {
            return !isEnabled() ? mAdapter.TYPE_DISABLE : mAdapter.TYPE_MULTISTAGE;
        }
    }

    private class SoundAction extends SinglePressAction {

        final static int SOUND_MUTE = 1;
        final static int SOUND_VIBRATE = 2;
        final static int SOUND_RING = 3;

        private int mSoundMode;

        private final Context mContext;

        protected SoundAction(Context context) {
            super(null, null);
            mContext = context;
            // default mode
            mSoundMode = SOUND_RING;
        }

        public int getSoundMode() {
            return mSoundMode;
        }

        public void setSoundMode(int action) {
            mSoundMode = action;
        }

        @Override
        public void onPress() {

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
            return !isEnabled() ? mAdapter.TYPE_DISABLE : mAdapter.TYPE_SOUND;
        }

        public boolean hasVibrator() {
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            return vibrator != null && vibrator.hasVibrator();
        }
    }
}
