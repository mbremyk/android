package com.example.sudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class NumberInputField extends AppCompatEditText {
    private int value;
    private int x, y;
    private OnTextChangeListener mListener;

    public NumberInputField(Context context) {
        super(context);
        init();
    }

    public NumberInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NumberInputField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public NumberInputField(Context context, int x, int y, OnTextChangeListener mListener) {
        super(context);
        this.x = x;
        this.y = y;
        this.mListener = mListener;
        init();
    }

    private void init() {
        this.setText("");
        this.setMaxLength(1);
        this.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    value = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    value = 0;
                }
                mListener.onTextChange(s.toString(), x, y);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.NumberInputField);

        ta.recycle();
    }

    public void setMaxLength(int length) {
        InputFilter[] editFilters = this.getFilters();
        if (editFilters != null) {
            for (int i = 0; i < editFilters.length; ++i) {
                if (editFilters[i] instanceof InputFilter.LengthFilter) {
                    editFilters[i] = new InputFilter.LengthFilter(length);
                    return;
                }
            }
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(length);
            this.setFilters(newFilters);
        } else {
            this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.setText(value > 0 ? Integer.toString(value) : "");
    }

    public interface OnTextChangeListener {
        public void onTextChange(String s, int x, int y);
    }
}
