package com.itfperu.appitf.data.tableview;


import com.itfperu.appitf.data.local.model.ProgramacionReja;
import com.itfperu.appitf.data.tableview.model.CellModel;
import com.itfperu.appitf.data.tableview.model.ColumnHeaderModel;
import com.itfperu.appitf.data.tableview.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 4.02.2018.
 */

class TableRejaViewModel {
    // View Types
    private static final int GENDER_TYPE = 1;
    private static final int MONEY_TYPE = 2;

    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    int getCellItemViewType(int column) {
        switch (column) {
            case 5:
                // 5. column header is gender.
                return GENDER_TYPE;
            case 8:
                // 8. column header is Salary.
                return MONEY_TYPE;
            default:
                return 0;
        }
    }

    private List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        list.add(new ColumnHeaderModel("PRODUCTO"));
        list.add(new ColumnHeaderModel("MATERIAL"));
        return list;
    }

    private List<List<CellModel>> createCellModelList(List<ProgramacionReja> userList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from User list for Cell Items
        // In this example, User list is populated from web service

        for (int i = 0; i < userList.size(); i++) {
            ProgramacionReja p = userList.get(i);
            List<CellModel> list = new ArrayList<>();
            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, p.getProducto()));
            list.add(new CellModel("2-" + i, p.getMaterial()));
            lists.add(list);
        }
        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(int size) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }


    List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }

    void generateListForTableView(List<ProgramacionReja> users) {
        mColumnHeaderModelList = createColumnHeaderModelList();
        mCellModelList = createCellModelList(users);
        mRowHeaderModelList = createRowHeaderList(users.size());
    }
}