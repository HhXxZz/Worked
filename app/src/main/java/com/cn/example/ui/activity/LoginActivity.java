package com.cn.example.ui.activity;

import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.example.R;
import com.cn.example.base.BaseActivity;
import com.cn.example.mvp.contract.LoginContract;
import com.cn.example.mvp.presenter.LoginPresenterImpl;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/19.
 */

public class LoginActivity extends BaseActivity<LoginPresenterImpl> implements LoginContract.LoginView {

    @BindView(R.id.user)
    EditText etUser;
    @BindView(R.id.pwd)
    EditText etPwd;
    @BindView(R.id.code)
    EditText etCode;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.textview)
    TextView textView;

    private LoginPresenterImpl loginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //presenter.login(getUserName(),getPwd());
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
            }
        });

        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //presenter.getCode();
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });

    }

    @Override
    protected LoginPresenterImpl getPresenter() {
        loginPresenter = new LoginPresenterImpl();
        return loginPresenter;
    }

    @Override
    public String getUserName() {
        return etUser.getText().toString();
    }

    @Override
    public String getPwd() {
        return etPwd.getText().toString();
    }

    @Override
    public void loginSuccess() {
        textView.setText(presenter.getList());
    }

    @Override
    public void loginFail(String failMsg) {
        Toast.makeText(this,"账号密码不正确",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCodeSuccess(String code) {
        etCode.setText(code);
    }

    @Override
    public void getCodeFail() {
        Toast.makeText(this,"获取失败",Toast.LENGTH_SHORT).show();
    }


    public boolean checkNull() {
        boolean isNull = false;
        if (TextUtils.isEmpty(getUserName())) {
            //("账号不能为空");
            Toast.makeText(this,"user is null",Toast.LENGTH_SHORT).show();
            isNull = true;
        } else if (TextUtils.isEmpty(getPwd())) {
            //inputPassword.setError("密码不能为空");
            Toast.makeText(this,"pwd is null",Toast.LENGTH_SHORT).show();
            isNull = true;
        }
        return isNull;
    }

}
