package com.yitulin.dubbocall.infrastructure.utils;

import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/24 13:57
 * Modified By:
 */
public class JTableDataUtils {

    public static <T> T collectTableDataByRow(JTable jTable,int row,Class<T> cls, Function<Vector,T> vectorToEntity){
        DefaultTableModel configTableModel = (DefaultTableModel) jTable.getModel();
        Vector<Vector> dataVector = configTableModel.getDataVector();
        if (dataVector.size()<=row){
            throw new RuntimeException("行标从0开始，不可超出表格数据行数");
        }
        return vectorToEntity.apply(dataVector.get(row));
    }

    public static <T> List<T> collectTableData(JTable jTable,Class<T> cls, Function<Vector,T> vectorToEntity){
        DefaultTableModel configTableModel = (DefaultTableModel) jTable.getModel();
        Vector<Vector> dataVector = configTableModel.getDataVector();
        return dataVector.stream().map(vector -> {
            return vectorToEntity.apply(vector);
        }).collect(Collectors.toList());
    }

}
