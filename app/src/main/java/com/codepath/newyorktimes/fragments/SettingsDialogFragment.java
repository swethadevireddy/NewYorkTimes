package com.codepath.newyorktimes.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.codepath.newyorktimes.activities.R;
import com.codepath.newyorktimes.activities.databinding.FragmentSettingsBinding;
import com.codepath.newyorktimes.model.SearchFilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by sdevired on 10/21/16.
 * Diaglog Fragment for settings
 */
public class SettingsDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    //Databinding
    private FragmentSettingsBinding binding;
    //model holding searchsettings
    private SearchFilter searchFilter;
    private SettingsDialogListener listener;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.sort_order, android.R.layout.simple_spinner_dropdown_item);

        binding.sortOrder.setAdapter(adapter);
        //set the sortorder if present
        if (searchFilter.getSortOrder() != null) {
            int selectionPosition = adapter.getPosition(searchFilter.getSortOrder());
            binding.sortOrder.setSelection(selectionPosition);
        }
        //set checkbox if present
        binding.arts.setChecked(searchFilter.getArts());
        binding.sports.setChecked(searchFilter.getSports());
        binding.fashion.setChecked(searchFilter.getFashionStyle());

        //register onclick listener for save button
        binding.btnSave.setOnClickListener(v -> {
            searchFilter.setBeginDate(binding.beginDate.getText().toString());
            searchFilter.setSortOrder(binding.sortOrder.getSelectedItem().toString());
            searchFilter.setArts(binding.arts.isChecked());
            searchFilter.setFashionStyle(binding.fashion.isChecked());
            searchFilter.setSports(binding.sports.isChecked());
            listener.onFinishSettingFilters(searchFilter);
            dismiss();

        });
        //setup datepicker

        addDatePicker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //setup binding object
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        return  binding.getRoot();
    }


    public void setSearchFilter(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

    public void addDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        if(searchFilter.getBeginDate() != null && !searchFilter.getBeginDate().isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = dateFormat.parse(searchFilter.getBeginDate());
                binding.beginDate.setText(searchFilter.getBeginDate());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(binding.beginDate.getContext(), this, year, month, day);
        //register listener when editext is clicked.
        binding.beginDate.setOnClickListener(v -> datePickerDialog.show());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // Set Date to Text View
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = dateFormat.format(calendar.getTime());
        binding.beginDate.setText(formatted);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (SettingsDialogListener)activity;

    }

    // Defines the listener interface with a method passing back data result.
    public interface SettingsDialogListener{
        void onFinishSettingFilters(SearchFilter searchFilter);
    }
}