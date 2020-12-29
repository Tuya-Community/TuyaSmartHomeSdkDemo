package com.tuya.smart.android.demo.device;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.base.bean.ColorBean;
import com.tuya.smart.android.demo.base.presenter.LampPresenter;
import com.tuya.smart.android.demo.base.utils.AnimationUtil;
import com.tuya.smart.android.demo.base.view.ILampView;
import com.tuya.smart.android.demo.base.widget.ColorPicker;
import com.tuya.smart.android.demo.base.widget.LampView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LampActivity extends BaseActivity implements ILampView {

    private static final String TAG = "LampActivity";
    protected LampPresenter mLampPresenter;

    @BindView(R.id.picker)
    public LampView mLampView;

    @BindView(R.id.iv_lamp_close)
    public ImageView mLampSwitchButton;

    @BindView(R.id.iv_lamp_light)
    public ImageView mLampCloseLight;

    @BindView(R.id.tv_lamp_operationTip)
    public TextView mLampViewTip;

    @BindView(R.id.tv_lamp_color_mode)
    public TextView mLampModeViewTip;

    @BindView(R.id.fl_lamp_white_operation)//声明一个亮度/冷暖面板
    public View mOperationView;

    @BindView(R.id.fl_lamp_mode_operation)//声明一个模式操作面板@
    public View mModeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);

        initToolbar();
        initMenu();
        initView();
        initPresenter();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    protected void initPresenter() {
        mLampPresenter = new LampPresenter(this, this, getIntent().getStringExtra(SwitchActivity.INTENT_DEVID));
    }

    protected void initMenu() {
        setDisplayHomeAsUpEnabled();
    }

    @Override
    public void showLampView() {//当点击开灯时，会打开面板
        if (mLampView.getVisibility() != View.VISIBLE) {
            mLampView.setVisibility(View.VISIBLE);
        }
        mLampSwitchButton.setImageResource(R.drawable.transpant_bg);
        if (mLampCloseLight.getVisibility() != View.GONE) {
            mLampCloseLight.setVisibility(View.GONE);
        }
        if (mLampModeViewTip.getVisibility() != View.GONE) {
            mLampModeViewTip.setVisibility(View.GONE);
        }
        if (mLampViewTip.getVisibility() != View.VISIBLE) {
            mLampViewTip.setVisibility(View.VISIBLE);
        }

        //模式面板 关闭@
        if (mModeView.getVisibility() != View.GONE) {
            mModeView.setVisibility(View.GONE);
        }

        mLampViewTip.setText(R.string.lamp_close_tip);
    }

    @Override
    public void hideLampView() {//当点击关灯时，会隐藏面板
        if (mLampView.getVisibility() != View.GONE) {//关闭彩色面板
            mLampView.setVisibility(View.GONE);
        }
        mLampSwitchButton.setImageResource(R.drawable.ty_lamp_wick_line);
        mLampCloseLight.setVisibility(View.VISIBLE);
        mLampViewTip.setVisibility(View.VISIBLE);
        mLampViewTip.setText(R.string.lamp_open_tip);//显示 按下开灯
        if (mLampModeViewTip.getVisibility() != View.GONE) {
            mLampModeViewTip.setVisibility(View.GONE);
            mModeView.setVisibility(View.GONE);//不可视模式面板 @
        }
        mLampPresenter.hideOperation();
        mOperationView.setVisibility(View.GONE);//不可视冷暖面板
        mModeView.setVisibility(View.GONE);//不可视模式面板 @
    }


    @Override
    public int getLampColor() {
        return mLampView.getColor();
    }

    @Override
    public int getLampOriginalColor() {
        return mLampView.getOriginalColor();
    }

    @Override
    public void setLampColor(int color) {
        mLampView.setColor(color);
    }

    @Override
    public void setLampColorWithNoMove(int color) {
        mLampView.setColorWithNoAngle(color, ColorPicker.S_RESPONING);
    }

    @Override
    public void showOperationView() {//显示浮动面板
        mOperationView.setVisibility(View.VISIBLE);
        AnimationUtil.translateView(mOperationView, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f, 300, false, null);
    }


    @Override
    public void sendLampColor(ColorBean bean) {
        mLampPresenter.syncColorToLamp(bean);
    }


    @OnClick(R.id.iv_lamp_close)
    public void onLampClick() {
        mLampPresenter.onClickLampSwitch();
    }

    @OnClick(R.id.ll_lamp_bottom_operation)
    public void onClickArrawUp() {//下箭头的监听函数
        AnimationUtil.translateView(mOperationView, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, 300, false, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mOperationView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mModeView.setVisibility(View.VISIBLE);
                    mLampViewTip.setVisibility(View.VISIBLE);
                    //mLampPresenter.hideOperation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }
    @OnClick(R.id.ll_lamp_up_operation)//@
    public void onVClickArrawDown(){
        AnimationUtil.translateView(mOperationView, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, 300, false, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mModeView.setVisibility(View.GONE);//隐藏模式面板 @
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mOperationView.setVisibility(View.VISIBLE);
                        //mLampViewTip.setVisibility(View.VISIBLE);
                        //mLampPresenter.hideOperation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
    }

    @OnClick(R.id.ll_lamp_mode_goodnight)
    public void onClickGoodnight() {//晚安模式@
        mLampPresenter.LampGoodnightScene();
    }

    @OnClick(R.id.ll_lamp_mode_work)
    public void onClickWork() {//晚安模式@
        mLampPresenter.LampWorkScene();
    }

    @OnClick(R.id.ll_lamp_mode_read)
    public void onClickRead() {//晚安模式@
        mLampPresenter.LampReadScene();
    }

    @OnClick(R.id.ll_lamp_mode_casual)
    public void onClickCasual() {//晚安模式@
        mLampPresenter.LampCasualScene();
    }

    @OnClick(R.id.ll_lamp_work_white)
    public void onClickWhite() {//晚安模式@
        mLampPresenter.LampWhiteMode();
    }

    @OnClick(R.id.ll_lamp_work_color)
    public void onClickColor() { mLampPresenter.LampColorMode(); }

    @OnClick(R.id.ll_lamp_work_scene)
    public void onClickScene() {//晚安模式@
        mLampPresenter.LampSceneMode();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLampPresenter.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}

