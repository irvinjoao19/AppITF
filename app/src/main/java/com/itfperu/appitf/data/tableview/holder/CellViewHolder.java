package com.itfperu.appitf.data.tableview.holder;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.itfperu.appitf.R;
import com.itfperu.appitf.data.tableview.model.CellModel;
import com.itfperu.appitf.ui.listeners.OnItemClickListener;

/**
 * Created by evrencoskun on 1.12.2017.
 */

public class CellViewHolder extends AbstractViewHolder {
    private final TextView cell_textview;
    private final LinearLayout cell_container;

    public CellViewHolder(View itemView) {
        super(itemView);
        cell_textview = itemView.findViewById(R.id.cell_data);
        cell_container = itemView.findViewById(R.id.cell_container);
    }

    public void setCellModel(CellModel p_jModel, int pColumnPosition, OnItemClickListener.ProgramacionPerfilListener listener) {

        // Change textView align by column
        cell_textview.setGravity(ColumnHeaderViewHolder.COLUMN_TEXT_ALIGNS[pColumnPosition] |
                Gravity.START | Gravity.CENTER_VERTICAL);

        // Set text
        cell_textview.setText(String.valueOf(p_jModel.getData()));

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cell_textview.requestLayout();

        if (listener != null) {
            cell_textview.setOnClickListener(view -> listener.onItemClick(String.valueOf(p_jModel.getData())));
        }
    }

    @Override
    public void setSelected(SelectionState p_nSelectionState) {
        super.setSelected(p_nSelectionState);

//        if (p_nSelectionState == SelectionState.SELECTED) {
//            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color
//                    .selected_text_color));
//        } else {
//            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color
//                    .unselected_text_color));
//        }
    }
}
