/*
 * Created by JFormDesigner on Fri Aug 20 10:14:29 CST 2021
 */

package com.yitulin.dubbocall.ui.setting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsContexts;
import com.yitulin.dubbocall.domain.request.entity.SettingAggregate;
import com.yitulin.dubbocall.domain.request.entity.VariableEntity;
import com.yitulin.dubbocall.infrastructure.config.CompatibleSettingService;
import com.yitulin.dubbocall.infrastructure.config.SettingService;
import com.yitulin.dubbocall.infrastructure.config.Settings;
import com.yitulin.dubbocall.infrastructure.enums.ErrorCode;
import com.yitulin.dubbocall.infrastructure.utils.JsonFileUtil;
import com.yitulin.dubbocall.infrastructure.utils.MessageDialogUtils;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @author unknown
 */
public class GlobalConfigSetting extends JPanel implements Configurable,Configurable.Composite {

    private static final Log LOG = LogFactory.get();
    
    private static final Integer KEY_COLUMN_INDEX=1;

    private Settings settings=Settings.getInstance();
    private SettingService settingService = SettingService.getInstance();
    private CompatibleSettingService compatibleSettingService = CompatibleSettingService.getInstance();

    public GlobalConfigSetting() {
        initComponents();
        if (settingService.configFileExists()){
            configFilePathLabel.setText(settingService.getConfigFilePath());
        }
        initConfigTableData();
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "DubboCall";
    }

    @Override
    public Configurable @NotNull [] getConfigurables() {
        Configurable httpRequestTemplateSetting=HttpRequestTemplateSetting.getInstance();
        return new Configurable[]{httpRequestTemplateSetting};
    }

    @Override
    public @Nullable JComponent createComponent() {
        return this;
    }

    @Override
    public boolean isModified() {
        List<VariableEntity> variableEntityList = collectConfigTableData();
        return !variableEntityList.equals(getSettingsVariableEntityList());
    }

    @Override
    public void apply() throws ConfigurationException {
        List<VariableEntity> variableEntityList = collectConfigTableData();
        Set<String> keySet = variableEntityList.stream().map(VariableEntity::getKey).collect(Collectors.toSet());
        if (keySet.size()<variableEntityList.size()){
            Messages.showErrorDialog("自定义变量Key重复，无法保存配置数据", ErrorCode.GLOBAL_CONFIG_ERROR.getTitle());
            return;
        }
        compatibleSettingService.writeVariable(variableEntityList);
    }

    private void exportAllButtonActionPerformed(ActionEvent e) {
        LOG.info("点击了导出当前配置按钮");
        JFileChooser jFileChooser=new JFileChooser();
        jFileChooser.setCurrentDirectory(new File(""));
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setMultiSelectionEnabled(false);
        int openDialog = jFileChooser.showOpenDialog(this);
        if (openDialog!=JFileChooser.APPROVE_OPTION){
            return;
        }
        File selectedFile = jFileChooser.getSelectedFile();
        LOG.info("选中的文件夹是：[{}]",selectedFile.getPath());
        boolean yesNo = MessageDialogUtils.yesNo("确认导出所有配置到文件夹[" + selectedFile.getPath() + "]嘛");
        if (!yesNo){
            return;
        }
        SettingAggregate settingAggregate = SettingAggregate.builder()
                .defaultTemplateName(settings.getDefaultTemplateName())
                .variableEntityList(Lists.newArrayList(settings.getVariableEntityMap().values()))
                .httpRequestTemplateEntityList(Lists.newArrayList(settings.getHttpRequestTemplateEntityMap().values()))
                .build();
        JsonFileUtil.overrideWrite(selectedFile.getPath()+"/dubbo-call-setting-export.json",JSONUtil.parse(settingAggregate));
        JsonFileUtil.overrideWrite(selectedFile.getPath()+"/dubbo-call-log.json",JSONUtil.parse(settings.getRequestCallLogEntityMap()));
    }

    private void selectConfigFileButtonActionPerformed(ActionEvent e) {
        LOG.info("点击了选择配置文件按钮");
        JFileChooser jFileChooser=new JFileChooser();
        jFileChooser.setCurrentDirectory(new File(""));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileFilter(new FileNameExtensionFilter("json","json"));
        int openDialog = jFileChooser.showOpenDialog(this);
        if (openDialog!=JFileChooser.APPROVE_OPTION){
            return;
        }
        File selectedFile = jFileChooser.getSelectedFile();
        LOG.info("选中的文件是：[{}]",selectedFile.getPath());
        boolean yesNo = MessageDialogUtils.yesNo("确认将[" + selectedFile.getName() + "]作为配置文件嘛");
        if (!yesNo){
            return;
        }
        configFilePathLabel.setText(selectedFile.getPath());
        this.settingService.resetConfigFile(selectedFile);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel2 = new JPanel();
        descLabel = new JLabel();
        configFilePathLabel = new JLabel();
        selectConfigFileButton = new JButton();
        scrollPane1 = new JScrollPane();
        configTable = new JTable();
        panel1 = new JPanel();
        importAllButton = new JButton();
        addRow = new JButton();
        deleteRow = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder(
        0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder
        . BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color.
        red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
        beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {34, 0, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {0.01, 0.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.01, 0.01, 1.0E-4};

        //======== panel2 ========
        {
            panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

            //---- descLabel ----
            descLabel.setText("\u5f53\u524d\u914d\u7f6e\u6587\u4ef6\u8def\u5f84\uff1a");
            panel2.add(descLabel);
            panel2.add(configFilePathLabel);
        }
        add(panel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- selectConfigFileButton ----
        selectConfigFileButton.setText("\u9009\u62e9\u914d\u7f6e\u6587\u4ef6");
        selectConfigFileButton.addActionListener(e -> selectConfigFileButtonActionPerformed(e));
        add(selectConfigFileButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //======== scrollPane1 ========
        {

            //---- configTable ----
            configTable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                    "\u7c7b\u578b", "Key", "\u503c"
                }
            ) {
                Class<?>[] columnTypes = new Class<?>[] {
                    String.class, String.class, String.class
                };
                boolean[] columnEditable = new boolean[] {
                    false, true, true
                };
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            {
                TableColumnModel cm = configTable.getColumnModel();
                cm.getColumn(0).setCellEditor(new DefaultCellEditor(
                    new JComboBox(new DefaultComboBoxModel(new String[] {
                        "\u81ea\u5b9a\u4e49\u53d8\u91cf",
                        "\u7cfb\u7edf\u53d8\u91cf"
                    }))));
            }
            configTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            configTable.addPropertyChangeListener(e -> configTablePropertyChange(e));
            scrollPane1.setViewportView(configTable);
        }
        add(scrollPane1, new GridBagConstraints(0, 1, 1, 1, 9.0, 9.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //======== panel1 ========
        {
            panel1.setLayout(new VerticalLayout(5));

            //---- importAllButton ----
            importAllButton.setText("\u5bfc\u51fa\u5f53\u524d\u914d\u7f6e");
            importAllButton.addActionListener(e -> exportAllButtonActionPerformed(e));
            panel1.add(importAllButton);

            //---- addRow ----
            addRow.setText("\u2795");
            addRow.addActionListener(e -> addRowActionPerformed(e));
            panel1.add(addRow);

            //---- deleteRow ----
            deleteRow.setText("\u274c");
            deleteRow.addActionListener(e -> {
			deleteRowActionPerformed(e);
			deleteRowActionPerformed(e);
		});
            panel1.add(deleteRow);
        }
        add(panel1, new GridBagConstraints(1, 1, 1, 1, 1.0, 3.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private List<VariableEntity> getSettingsVariableEntityList(){
        return compatibleSettingService.readVariableList();
    }

    private List<VariableEntity> collectConfigTableData(){
        DefaultTableModel configTableModel = (DefaultTableModel) configTable.getModel();
        Vector<Vector> dataVector = configTableModel.getDataVector();
        return dataVector.stream().map(vector -> {
            return tableVectorToEntity(vector);
        }).collect(Collectors.toList());
    }

    private VariableEntity tableVectorToEntity(Vector vector){
        return VariableEntity.builder()
                .type(String.valueOf(vector.get(0)))
                .key(String.valueOf(vector.get(1)))
                .value(String.valueOf(vector.get(2)))
                .build();
    }

    public void initConfigTableData(){
        LOG.info("初始化变量配置表数据");
        configTable.removeAll();
        List<VariableEntity> variableEntityList=compatibleSettingService.readVariableList();
        DefaultTableModel configTableModel = (DefaultTableModel) configTable.getModel();
        while (configTableModel.getRowCount()>0){
            configTableModel.removeRow(0);
        }
        variableEntityList.stream().forEach(variableEntity -> {
            configTableModel.addRow(new Object[]{variableEntity.getType(),variableEntity.getKey(),variableEntity.getValue()});
        });
    }

    private void addRowActionPerformed(ActionEvent e) {
        DefaultTableModel configTableModel = (DefaultTableModel) configTable.getModel();
        configTableModel.addRow(new Object[]{"自定义","customKey","customValue"});
    }

    private void deleteRowActionPerformed(ActionEvent e) {
        int[] selectRows = configTable.getSelectedRows();
        if (selectRows == null || selectRows.length == 0) {
            return;
        }
        if (!MessageDialogUtils.yesNo("确认要删除嘛？")) {
            return;
        }
        // 从后面往前面移除，防止下标错位问题。
        DefaultTableModel configTableModel = (DefaultTableModel) configTable.getModel();
        for (int i = selectRows.length - 1; i >= 0; i--) {
            configTableModel.removeRow(selectRows[i]);
        }
    }

    private void configTablePropertyChange(PropertyChangeEvent e) {
        if (!"tableCellEditor".equals(e.getPropertyName())){
            return;
        }
        int editingRow = configTable.getEditingRow();
        int editingColumn = configTable.getEditingColumn();
        if (editingRow<0 || editingRow>=configTable.getRowCount()){
            return;
        }
        if (editingColumn<0 || editingColumn>=configTable.getColumnCount()){
            return;
        }
        if (editingColumn!=KEY_COLUMN_INDEX){
            return;
        }
        Object valueAt = configTable.getModel().getValueAt(editingRow, editingColumn);
        for (int i = 0; i < configTable.getRowCount(); i++) {
            if (i==editingRow){
                continue;
            }
            if (valueAt.equals(configTable.getModel().getValueAt(i,KEY_COLUMN_INDEX))){
                Messages.showErrorDialog("自定义变量Key冲突",ErrorCode.GLOBAL_CONFIG_ERROR.getTitle());
                return;
            }
        }
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel2;
    private JLabel descLabel;
    private JLabel configFilePathLabel;
    private JButton selectConfigFileButton;
    private JScrollPane scrollPane1;
    private JTable configTable;
    private JPanel panel1;
    private JButton importAllButton;
    private JButton addRow;
    private JButton deleteRow;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
