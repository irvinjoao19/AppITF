package com.itfperu.appitf.data.tableview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.itfperu.appitf.R;
import com.itfperu.appitf.data.local.model.ProgramacionPerfilDetalle;
import com.itfperu.appitf.data.tableview.holder.CellViewHolder;
import com.itfperu.appitf.data.tableview.holder.ColumnHeaderViewHolder;
import com.itfperu.appitf.data.tableview.holder.RowHeaderViewHolder;
import com.itfperu.appitf.data.tableview.model.CellModel;
import com.itfperu.appitf.data.tableview.model.ColumnHeaderModel;
import com.itfperu.appitf.data.tableview.model.RowHeaderModel;

import java.util.List;

/**
 * Created by evrencoskun on 27.11.2017.
 */

public class TableDetalleAdapter extends AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel,
        CellModel> {

    private TableDetalleViewModel tableDetalleViewModel;

    public TableDetalleAdapter(Context p_jContext) {
        super(p_jContext);

        this.tableDetalleViewModel = new TableDetalleViewModel();
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout;

        layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_cell_layout,
                parent, false);

        // Create a Cell ViewHolder
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition, int p_nYPosition) {
        CellModel cell = (CellModel) p_jValue;

        if (holder instanceof CellViewHolder) {
            // Get the holder to update cell item text
            ((CellViewHolder) holder).setCellModel(cell, p_nXPosition,null);
        }
    }

    @Override
    public AbstractSorterViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout
                .tableview_column_header_layout, parent, false);

        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition) {
        ColumnHeaderModel columnHeader = (ColumnHeaderModel) p_jValue;
        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;

        columnHeaderViewHolder.setColumnHeaderModel(columnHeader, p_nXPosition);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Row Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_row_header_layout,
                parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nYPosition) {

        RowHeaderModel rowHeaderModel = (RowHeaderModel) p_jValue;

        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeaderModel.getData());

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null, false);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return tableDetalleViewModel.getCellItemViewType(position);
    }

    public void setUserList(List<ProgramacionPerfilDetalle> userList) {
        tableDetalleViewModel.generateListForTableView(userList);

        setAllItems(tableDetalleViewModel.getColumHeaderModeList(), tableDetalleViewModel
                .getRowHeaderModelList(), tableDetalleViewModel.getCellModelList());
    }
}