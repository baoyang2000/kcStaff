package com.jh.widget.t9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jh.widget.R;


/**
 * Created by Administrator on 2016/12/20.
 */
public class T9TelephoneDialpadView extends LinearLayout implements
        View.OnClickListener, View.OnLongClickListener {
    private static final char DIAL_X_SECOND_MEANING = ',';
    private static final char DIAL_0_SECOND_MEANING = '+';
    private static final char DIAL_J_SECOND_MEANING = ';';

    /**
     * Interface definition for a callback to be invoked when a
     * T9TelephoneDialpadView is operated.
     */
    public interface OnT9TelephoneDialpadView {
        void onCall();

        void addContacts();
    }

    private Context mContext;
    /**
     * Inflate Custom T9 phone dialpad View hierarchy from the specified xml
     * resource.
     */
    private View mDialpadView; // this Custom View As the T9TelephoneDialpadView
    // of children
    private Button addContacts, call;
    private Button mDialDeleteBtn;
    private EditText mT9InputEt;
    private OnT9TelephoneDialpadView mOnT9TelephoneDialpadView = null;

    public T9TelephoneDialpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initData();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialpadView = inflater.inflate(R.layout.t9_telephone_dialpad_layout,
                this);
        if (!isInEditMode()) {
            call = (Button) mDialpadView
                    .findViewById(R.id.call);
            addContacts = (Button) mDialpadView
                    .findViewById(R.id.add_contacts);
            mDialDeleteBtn = (Button) mDialpadView
                    .findViewById(R.id.delete);
        }
    }

    private void initListener() {
        if (!isInEditMode()) {
            call.setOnClickListener(this);
            addContacts.setOnClickListener(this);
            mDialDeleteBtn.setOnClickListener(this);
            mDialDeleteBtn.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteAllDialCharacter();
                    return true;
                }
            });

            /**
             * set click listener for button("0-9",'*','#')
             */
            for (int i = 0; i < 12; i++) {
                View v = mDialpadView.findViewById(R.id.dialNum1 + i);
                v.setOnClickListener(this);
            }

            /**
             * set long click listener for button('*','0','#')
             * */
            View viewX = mDialpadView.findViewById(R.id.dialx);
            viewX.setOnLongClickListener(this);

            View viewO = mDialpadView.findViewById(R.id.dialNum0);
            viewO.setOnLongClickListener(this);

            View viewJ = mDialpadView.findViewById(R.id.dialj);
            viewJ.setOnLongClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.add_contacts) {
            if (null != mOnT9TelephoneDialpadView) {
                mOnT9TelephoneDialpadView.addContacts();
            }
        } else if (i == R.id.delete) {
            deleteSingleDialCharacter();
        } else if (i == R.id.dialNum0 || i == R.id.dialNum1 || i == R.id.dialNum2 || i == R.id.dialNum3 || i == R.id.dialNum4 || i == R.id.dialNum5 || i == R.id.dialNum6 || i == R.id.dialNum7 || i == R.id.dialNum8 || i == R.id.dialNum9 || i == R.id.dialx || i == R.id.dialj) {
            addSingleDialCharacter(v.getTag().toString());

        } else if (i == R.id.call) {
            if (null != mOnT9TelephoneDialpadView) {
                mOnT9TelephoneDialpadView.onCall();
            }
        }

    }

    @Override
    public boolean onLongClick(View v) {
        int i = v.getId();
        if (i == R.id.dialx) {
            addSingleDialCharacter(String.valueOf(DIAL_X_SECOND_MEANING));
        } else if (i == R.id.dialNum0) {
            addSingleDialCharacter(String.valueOf(DIAL_0_SECOND_MEANING));
        } else if (i == R.id.dialj) {
            addSingleDialCharacter(String.valueOf(DIAL_J_SECOND_MEANING));
        } else {
        }
        return true;
    }

    public OnT9TelephoneDialpadView getOnT9TelephoneDialpadView() {
        return mOnT9TelephoneDialpadView;
    }

    public void setOnT9TelephoneDialpadView(
            OnT9TelephoneDialpadView onT9TelephoneDialpadView, EditText editText) {

        this.mT9InputEt = editText;
        mT9InputEt.setCursorVisible(false);
        mT9InputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (null != mOnT9TelephoneDialpadView) {
                    mT9InputEt.setSelection(s.toString().length());
                }
                if (length > 0) {
                    if (addContacts.getVisibility() == View.INVISIBLE)
                        addContacts.setVisibility(View.VISIBLE);
                    if (mDialDeleteBtn.getVisibility() == View.INVISIBLE)
                        mDialDeleteBtn.setVisibility(View.VISIBLE);
                } else {
                    if (addContacts.getVisibility() == View.VISIBLE)
                        addContacts.setVisibility(View.INVISIBLE);
                    if (mDialDeleteBtn.getVisibility() == View.VISIBLE)
                        mDialDeleteBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        mT9InputEt.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // In order to prevent the soft keyboard pops up,but also can
                // not make EditText get focus.
                return true; // the listener has consumed the event
            }
        });

        mOnT9TelephoneDialpadView = onT9TelephoneDialpadView;
    }

    private void deleteSingleDialCharacter() {
        String curInputStr = mT9InputEt.getText().toString();
        if (curInputStr.length() > 0) {
            mT9InputEt.setText(curInputStr.substring(0,
                    curInputStr.length() - 1));
            mT9InputEt.setSelection(mT9InputEt.getText().length());
        }
    }

    private void deleteAllDialCharacter() {
        String curInputStr = mT9InputEt.getText().toString();
        if (curInputStr.length() > 0) {
            mT9InputEt.setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    private void addSingleDialCharacter(String addCharacter) {
        String preInputStr = mT9InputEt.getText().toString();
        if (!TextUtils.isEmpty(addCharacter)) {
            mT9InputEt.setText(preInputStr + addCharacter);
            mT9InputEt.setSelection(mT9InputEt.getText().length());
        }
    }

    public void showT9TelephoneDialpadView() {
        if (this.getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
    }

    public void hideT9TelephoneDialpadView() {
        if (this.getVisibility() != View.GONE) {
            this.setVisibility(View.GONE);
        }
    }

    public int getT9TelephoneDialpadViewVisibility() {
        return this.getVisibility();
    }

	/*
     * public void switchT9TelephoneDialpadViewVisibility(){ switch
	 * (this.getVisibility()) { case View.VISIBLE:
	 * this.setVisibility(View.GONE); break; case View.GONE:
	 * this.setVisibility(View.VISIBLE); break; default:
	 * this.setVisibility(View.VISIBLE); break; } }
	 */

    public String getT9Input() {
        return mT9InputEt.getText().toString();
    }

    public void clearT9Input() {
        mT9InputEt.setText("");
    }
}
