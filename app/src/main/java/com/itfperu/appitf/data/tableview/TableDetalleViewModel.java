package com.itfperu.appitf.data.tableview;


import com.itfperu.appitf.data.local.model.ProgramacionPerfilDetalle;
import com.itfperu.appitf.data.tableview.model.CellModel;
import com.itfperu.appitf.data.tableview.model.ColumnHeaderModel;
import com.itfperu.appitf.data.tableview.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 4.02.2018.
 */

class TableDetalleViewModel {
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

    private List<ColumnHeaderModel> createColumnHeaderModelList(ProgramacionPerfilDetalle p) {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        list.add(new ColumnHeaderModel(p.getMercadoProducto()));
        list.add(new ColumnHeaderModel(p.getDoceUltimosMeses()));
        list.add(new ColumnHeaderModel(p.getTresUltimosMeses()));
        list.add(new ColumnHeaderModel(p.getUltimoMeses()));
        return list;
    }

    private List<List<CellModel>> createCellModelList(List<ProgramacionPerfilDetalle> userList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from User list for Cell Items
        // In this example, User list is populated from web service
        int total12 = 0;
        int total3 = 0;
        int total1 = 0;

        for (int i = 1; i < userList.size(); i++) {

            ProgramacionPerfilDetalle p = userList.get(i);
            List<CellModel> list = new ArrayList<>();
            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, p.getMercadoProducto()));
            list.add(new CellModel("2-" + i, p.getDoceUltimosMeses()));
            list.add(new CellModel("3-" + i, p.getTresUltimosMeses()));
            list.add(new CellModel("4-" + i, p.getUltimoMeses()));

            total12 = Integer.parseInt(p.getDoceUltimosMeses()) + total12;
            total3 = Integer.parseInt(p.getTresUltimosMeses()) + total3;
            total1 = Integer.parseInt(p.getUltimoMeses()) + total1;
            lists.add(list);
        }

        List<CellModel> list2 = new ArrayList<>();
        list2.add(new CellModel("1-" + userList.size(), "Total"));
        list2.add(new CellModel("2-" + userList.size(), total12));
        list2.add(new CellModel("3-" + userList.size(), total3));
        list2.add(new CellModel("4-" + userList.size(), total1));
        lists.add(list2);

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

    void generateListForTableView(List<ProgramacionPerfilDetalle> users) {
        mColumnHeaderModelList = createColumnHeaderModelList(users.get(0));
        mCellModelList = createCellModelList(users);
        mRowHeaderModelList = createRowHeaderList(users.size());
    }
}