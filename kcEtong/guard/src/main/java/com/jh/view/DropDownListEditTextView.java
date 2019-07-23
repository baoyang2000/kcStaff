package com.jh.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.jh.R;

import java.util.List;

public class DropDownListEditTextView extends LinearLayout implements AdapterView.OnItemClickListener {
    private EditText editText;
    private LinearLayout btn;
    private PopupWindow popupWindow = null;
    private OnItemClickListener onItemClickListener;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    public DropDownListEditTextView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public DropDownListEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public DropDownListEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView();
    }

    public void initView() {
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getContext().getSystemService(infServie);
        View view = layoutInflater.inflate(R.layout.dropdownlist_edittext, this, true);
        editText = (EditText) view.findViewById(R.id.text);
        btn = (LinearLayout) view.findViewById(R.id.btn);

        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView = new ListView(getContext());
        listView.setDividerHeight(1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (popupWindow == null) {
                    showPopWindow();
                } else if (popupWindow.isShowing()) {
                    closePopWindow();
                } else {
                    showPopWindow();
                }
            }
        });
    }

    public void setItemsData(String[] strings) {
        arrayAdapter.clear();
        if (strings != null && strings.length > 0) {
            editText.setText(strings[0]);
            arrayAdapter.addAll(strings);
        } else {
            editText.setText("");
        }
    }

    public void setItemsData(List<String> strings) {
        arrayAdapter.clear();
        if (strings != null && strings.size() > 0) {
            editText.setText(strings.get(0));
            arrayAdapter.addAll(strings);
        } else {
            editText.setText("");
        }
    }

    public void setInputType(int inputType) {
        editText.setInputType(inputType);
    }

    public void setDefaultsValue(String s) {
        if (editText != null) {
            editText.setText(s);
        }
    }

    public String getContent() {
        return editText.getText().toString();
    }

    public void setEnabled(boolean b){
        btn.setEnabled(b);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String item = (String) adapterView.getItemAtPosition(position);
        editText.setText(item);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(position);
        }
        closePopWindow();
    }

    /**
     * 打开下拉列表弹窗
     */
    private void showPopWindow() {
        // 加载popupWindow的布局文件
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this.getWidth(), LayoutParams.WRAP_CONTENT);
            popupWindow.setContentView(listView);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
        popupWindow.showAsDropDown(this);

    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        popupWindow.dismiss();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
