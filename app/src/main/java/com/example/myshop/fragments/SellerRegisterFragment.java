package com.example.myshop.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myshop.R;
import com.example.myshop.activities.SellerHomePageActivity;
import com.example.myshop.dataBase.DataBaseHandler;
import com.example.myshop.model.Seller;

import java.util.List;

public class SellerRegisterFragment extends Fragment
{
    private AppCompatButton registerButton;
    public static Seller seller;
    private EditText email,password,passwordRepeat,phoneNumber,name;
    private TextView loginTextView,errorField;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_seller_register,container,false);
        passwordRepeat=view.findViewById(R.id.seller_register_password_repeat);
        registerButton=view.findViewById(R.id.seller_register_button);
        email=view.findViewById(R.id.seller_register_email);
        password=view.findViewById(R.id.seller_register_password);
        loginTextView=view.findViewById(R.id.seller_text_view_login);
        phoneNumber = view.findViewById(R.id.seller_register_phone_number);
        name = view.findViewById(R.id.seller_register_name);
        errorField = view.findViewById(R.id.text_view_seller_register_error);


        loginTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(view).navigate(R.id.action_sellerRegisterFragment_to_sellerLoginFragment);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (password.getText().toString().equals("") || email.getText().toString().equals("") || passwordRepeat.getText().toString().equals("") || name.getText().toString().equals("") || phoneNumber.getText().toString().equals("") ) {
                        errorField.setText("اطلاعات وارد شده کافی نیست!");
                    }

                    else if (!password.getText().toString().equals(passwordRepeat.getText().toString())) {
                        errorField.setText("رمز عبور مطابقت ندارد!");
                    }
                    else if (!isChecked(email.getText().toString(),phoneNumber.getText().toString()).equals(""))
                    {
                        errorField.setText(isChecked(email.getText().toString(),phoneNumber.getText().toString()));
                    }
                    else
                    {
                        DataBaseHandler db = new DataBaseHandler(getActivity());
                        seller = new Seller(name.getText().toString(), email.getText().toString(), password.getText().toString(), phoneNumber.getText().toString());
                        boolean success = db.addSeller(seller);
                        if (success) {
                            seller.setLoginCount(1);
                            Toast.makeText(getActivity(),"خوش آمدید",Toast.LENGTH_SHORT).show();
                            Seller.activeSeller=seller;
                            startActivity(new Intent(getActivity(), SellerHomePageActivity.class));
                        }
                        else {
                            errorField.setText("ثبت نام با خطا مواجه شده است!");
                        }
                    }
                } catch (Exception e) {
                    errorField.setText("ثبت نام با خطا مواجه شده است!");
                }
            }
        });
        return view;
    }

    public String isChecked(String email,String phoneNumber)
    {

        DataBaseHandler db = new DataBaseHandler(getActivity());
        List<Seller> sellers = db.getAllSellers();
        for (Seller seller1 : sellers) {
            if (seller1.getEmail().equalsIgnoreCase(email))
            {
                return "فروشنده ای با این ایمیل موجود است !";
            }
            if(seller1.getPhoneNumber().equalsIgnoreCase(phoneNumber))
            {
                return "فروشنده ای با این شماره موجود است !";
            }
        }
        return "";
    }

    public Seller getSeller() {
        return seller;
    }
}
