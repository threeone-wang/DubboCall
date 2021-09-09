/*
 * Created by JFormDesigner on Tue Aug 24 00:38:24 CST 2021
 */

package com.yitulin.dubbocall.ui.setting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsContexts;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestTemplateEntity;
import com.yitulin.dubbocall.infrastructure.config.SettingService;
import com.yitulin.dubbocall.infrastructure.config.Settings;
import com.yitulin.dubbocall.infrastructure.enums.ErrorCode;
import com.yitulin.dubbocall.infrastructure.utils.ClipboardUtil;
import com.yitulin.dubbocall.infrastructure.utils.JTableDataUtils;
import com.yitulin.dubbocall.infrastructure.utils.JacksonUtil;
import com.yitulin.dubbocall.infrastructure.utils.MessageDialogUtils;
import com.yitulin.dubbocall.ui.dialog.HttpRequestTemplateDialog;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @author unknown
 */
public class HttpRequestTemplateSetting extends JPanel implements Configurable {

    private static final Log LOG= LogFactory.get();

    private static final Integer KEY_COLUMN_INDEX=0;

    private Settings settings=Settings.getInstance();
    private SettingService settingService = SettingService.getInstance();

    private String defaultTemplateName;

    public static HttpRequestTemplateSetting getInstance(){
        Supplier<HttpRequestTemplateSetting> memoize = Suppliers.memoize(new Supplier<HttpRequestTemplateSetting>() {
            @Override
            public HttpRequestTemplateSetting get() {
                return new HttpRequestTemplateSetting();
            }
        });
        return memoize.get();
    }

    public HttpRequestTemplateSetting() {
        initComponents();
        if (settingService.configFileExists()){
            this.defaultTemplateName = settingService.getSettingAggregate().getDefaultTemplateName();
        }else {
            this.defaultTemplateName = settings.getDefaultTemplateName();
        }
        initTemplateTableList();
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "HTTP请求模板";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return this;
    }

    @Override
    public boolean isModified() {
        boolean modifiedFlag=false;
        if (settingService.configFileExists()){
            modifiedFlag =
                    !this.defaultTemplateName.equals(settingService.getSettingAggregate().getDefaultTemplateName())
                            || !collectTemplateTableData().equals(settingService.getSettingAggregate().getHttpRequestTemplateEntityList());
        }else {
            modifiedFlag =
                    !this.defaultTemplateName.equals(settings.getDefaultTemplateName())
                            || !buildTemplateMap(collectTemplateTableData()).equals(settings.getHttpRequestTemplateEntityMap());
        }
        return modifiedFlag;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (settingService.configFileExists()){
            List<HttpRequestTemplateEntity> httpRequestTemplateEntityList = collectTemplateTableData();
            settingService.getSettingAggregate().setDefaultTemplateName(this.defaultTemplateName);
            Map<String, HttpRequestTemplateEntity> httpRequestTemplateEntityMap = buildTemplateMap(httpRequestTemplateEntityList);
            settingService.getSettingAggregate().setHttpRequestTemplateEntityList(httpRequestTemplateEntityList);
            // 如果当前设定的默认模板不在模板表内，取第一个key作为默认模板
            if (!httpRequestTemplateEntityMap.containsKey(this.defaultTemplateName)){
                Object[] keyArray = httpRequestTemplateEntityMap.keySet().toArray();
                if (keyArray.length>0){
                    settingService.getSettingAggregate().setDefaultTemplateName(String.valueOf(keyArray[0]));
                }
            }
            settingService.overrideConfigFile();
        }else {
            List<HttpRequestTemplateEntity> httpRequestTemplateEntityList = collectTemplateTableData();
            settings.setDefaultTemplateName(this.defaultTemplateName);
            Map<String, HttpRequestTemplateEntity> httpRequestTemplateEntityMap = buildTemplateMap(httpRequestTemplateEntityList);
            settings.setHttpRequestTemplateEntityMap(httpRequestTemplateEntityMap);
            // 如果当前设定的默认模板不在模板表内，取第一个key作为默认模板
            if (!httpRequestTemplateEntityMap.containsKey(this.defaultTemplateName)){
                Object[] keyArray = httpRequestTemplateEntityMap.keySet().toArray();
                if (keyArray.length>0){
                    settings.setDefaultTemplateName(String.valueOf(keyArray[0]));
                }
            }
        }
    }

    public void initTemplateTableList(){
        LOG.info("初始化模板配置表数据");
        List<HttpRequestTemplateEntity> httpRequestTemplateEntityList = Lists.newArrayList();
        if (settingService.configFileExists()){
            httpRequestTemplateEntityList= settingService.getSettingAggregate().getHttpRequestTemplateEntityList();
        }else {
            httpRequestTemplateEntityList=settings.getHttpRequestTemplateEntityMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
        }
        DefaultTableModel templateTableModel = (DefaultTableModel) templateTable.getModel();
        while (templateTableModel.getRowCount()>0){
            templateTableModel.removeRow(0);
        }
        httpRequestTemplateEntityList.stream().forEach(httpRequestTemplateEntity -> {
            templateTableModel.addRow(new Object[]{httpRequestTemplateEntity.getName(),httpRequestTemplateEntity.getUrl(),httpRequestTemplateEntity.getHeaderJson(),httpRequestTemplateEntity.getBodyJson()});
        });
    }

    private List<HttpRequestTemplateEntity> collectTemplateTableData(){
        return JTableDataUtils.collectTableData(templateTable,HttpRequestTemplateEntity.class,(vector)->HttpRequestTemplateEntity.builder()
                .name(String.valueOf(vector.get(0)))
                .url(String.valueOf(vector.get(1)))
                .headerJson(String.valueOf(vector.get(2)))
                .bodyJson(String.valueOf(vector.get(3)))
                .build());
    }

    private List<String> collectTemplateNameFromTable(){
        return JTableDataUtils.collectTableData(templateTable,String.class,(vector)->String.valueOf(vector.get(0)));
    }

    private HttpRequestTemplateEntity collectTemplateTableDataByRow(int row){
        return JTableDataUtils.collectTableDataByRow(templateTable,row,HttpRequestTemplateEntity.class,(vector)->HttpRequestTemplateEntity.builder()
                .name(String.valueOf(vector.get(0)))
                .url(String.valueOf(vector.get(1)))
                .headerJson(String.valueOf(vector.get(2)))
                .bodyJson(String.valueOf(vector.get(3)))
                .build());
    }

    private Map<String,HttpRequestTemplateEntity> buildTemplateMap(List<HttpRequestTemplateEntity> httpRequestTemplateEntityList){
        if (CollectionUtil.isEmpty(httpRequestTemplateEntityList)){
            return Maps.newHashMap();
        }
        return httpRequestTemplateEntityList.stream().collect(Collectors.toMap(HttpRequestTemplateEntity::getName,item->item));
    }

    private void addButtonActionPerformed(ActionEvent e) {
        Window window = SwingUtilities.windowForComponent(this);
        HttpRequestTemplateDialog httpRequestTemplateDialog=new HttpRequestTemplateDialog(window,collectTemplateNameFromTable());
        httpRequestTemplateDialog.setVisible(true);
        if (!httpRequestTemplateDialog.isClickOkButtonTag()){
            return;
        }
        HttpRequestTemplateEntity templateEntity = httpRequestTemplateDialog.collectTemplateEntity();
        DefaultTableModel configTableModel = (DefaultTableModel) templateTable.getModel();
        configTableModel.addRow(new Object[]{ templateEntity.getName(), templateEntity.getUrl(),templateEntity.getHeaderJson(),templateEntity.getBodyJson()});
    }

    private void deleteButtonActionPerformed(ActionEvent e) {
        int[] selectRows = templateTable.getSelectedRows();
        if (selectRows == null || selectRows.length == 0) {
            return;
        }
        if (!MessageDialogUtils.yesNo("确认要删除嘛？")) {
            return;
        }
        // 从后面往前面移除，防止下标错位问题。
        DefaultTableModel templateNameTableModel = (DefaultTableModel) templateTable.getModel();
        for (int i = selectRows.length - 1; i >= 0; i--) {
            templateNameTableModel.removeRow(selectRows[i]);
        }
    }

    private void defaultButtonActionPerformed(ActionEvent e) {
        int[] selectedRows = templateTable.getSelectedRows();
        if (selectedRows.length==0){
            Messages.showErrorDialog("请至少选择一个模板", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (selectedRows.length>1){
            Messages.showErrorDialog("只能设定一个默认模板", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        Object selectedTemplateName = templateTable.getValueAt(selectedRows[0], KEY_COLUMN_INDEX);
        this.defaultTemplateName = String.valueOf(selectedTemplateName);
    }

    private void editButtonActionPerformed(ActionEvent e) {
        int[] selectedRows = templateTable.getSelectedRows();
        if (selectedRows.length==0){
            Messages.showErrorDialog("请至少选择一个模板进行编辑", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (selectedRows.length>1){
            Messages.showErrorDialog("只能选中一个模板进行编辑", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (selectedRows[0]>=templateTable.getRowCount()){
            Messages.showErrorDialog("选中的行标大于当前表格最大行数", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        HttpRequestTemplateEntity selectedTemplateEntity = collectTemplateTableDataByRow(selectedRows[0]);
        Window window = SwingUtilities.windowForComponent(this);
        HttpRequestTemplateDialog httpRequestTemplateDialog=new HttpRequestTemplateDialog(window,collectTemplateNameFromTable());
        httpRequestTemplateDialog.initEditInfo(selectedTemplateEntity);
        httpRequestTemplateDialog.setVisible(true);
        if (!httpRequestTemplateDialog.isClickOkButtonTag()){
            return;
        }
        HttpRequestTemplateEntity templateEntity = httpRequestTemplateDialog.collectTemplateEntity();
        DefaultTableModel configTableModel = (DefaultTableModel) templateTable.getModel();
        configTableModel.setValueAt(templateEntity.getName(),selectedRows[0],0);
        configTableModel.setValueAt(templateEntity.getUrl(),selectedRows[0],1);
        configTableModel.setValueAt(templateEntity.getHeaderJson(),selectedRows[0],2);
        configTableModel.setValueAt(templateEntity.getBodyJson(),selectedRows[0],3);
    }

    private void copyButtonActionPerformed(ActionEvent e) {
        int[] selectedRows = templateTable.getSelectedRows();
        if (selectedRows.length==0){
            Messages.showErrorDialog("请至少选择一个模板进行复制", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (selectedRows.length>1){
            Messages.showErrorDialog("只能选中一个模板进行复制", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (selectedRows[0]>=templateTable.getRowCount()){
            Messages.showErrorDialog("选中的行标大于当前表格最大行数", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        HttpRequestTemplateEntity selectedTemplateEntity = collectTemplateTableDataByRow(selectedRows[0]);
        Window window = SwingUtilities.windowForComponent(this);
        HttpRequestTemplateDialog httpRequestTemplateDialog=new HttpRequestTemplateDialog(window,collectTemplateNameFromTable());
        httpRequestTemplateDialog.initEditInfo(selectedTemplateEntity,false);
        httpRequestTemplateDialog.setVisible(true);
        if (!httpRequestTemplateDialog.isClickOkButtonTag()){
            return;
        }
        HttpRequestTemplateEntity templateEntity = httpRequestTemplateDialog.collectTemplateEntity();
        DefaultTableModel configTableModel = (DefaultTableModel) templateTable.getModel();
        configTableModel.addRow(new Object[]{ templateEntity.getName(), templateEntity.getUrl(),templateEntity.getHeaderJson(),templateEntity.getBodyJson()});
    }

    private void shareButtonActionPerformed(ActionEvent e) {
        int[] selectedRows = templateTable.getSelectedRows();
        if (selectedRows.length==0){
            Messages.showErrorDialog("请至少选择一个模板进行分享", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (selectedRows.length>1){
            Messages.showErrorDialog("只能选中一个模板进行分享", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (selectedRows[0]>=templateTable.getRowCount()){
            Messages.showErrorDialog("选中的行标大于当前表格最大行数", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        HttpRequestTemplateEntity selectedTemplateEntity = collectTemplateTableDataByRow(selectedRows[0]);
        ClipboardUtil.writeString(JSONUtil.toJsonStr(selectedTemplateEntity));
        Messages.showInfoMessage("模板已复制到剪贴板","HTTP请求模板设置");
    }

    private void importButtonActionPerformed(ActionEvent e) {
        String templateEntityJson = cn.hutool.core.swing.clipboard.ClipboardUtil.getStr();
        if (!JSONUtil.isJson(templateEntityJson)){
            Messages.showErrorDialog("剪贴板数据非合法的模板配置数据", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        HttpRequestTemplateEntity httpRequestTemplateEntity = JacksonUtil.string2Object(templateEntityJson, HttpRequestTemplateEntity.class);
        Window window = SwingUtilities.windowForComponent(this);
        HttpRequestTemplateDialog httpRequestTemplateDialog=new HttpRequestTemplateDialog(window,collectTemplateNameFromTable());
        httpRequestTemplateDialog.initEditInfo(httpRequestTemplateEntity,false);
        httpRequestTemplateDialog.setVisible(true);
        if (!httpRequestTemplateDialog.isClickOkButtonTag()){
            return;
        }
        HttpRequestTemplateEntity templateEntity = httpRequestTemplateDialog.collectTemplateEntity();
        DefaultTableModel configTableModel = (DefaultTableModel) templateTable.getModel();
        configTableModel.addRow(new Object[]{ templateEntity.getName(), templateEntity.getUrl(),templateEntity.getHeaderJson(),templateEntity.getBodyJson()});
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        scrollPane1 = new JScrollPane();
        templateTable = new JTable();
        panel1 = new JPanel();
        defaultButton = new JButton();
        shareButton = new JButton();
        addButton = new JButton();
        editButton = new JButton();
        copyButton = new JButton();
        importButton = new JButton();
        deleteButton = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing
        . border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder
        . CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dialo\u0067" ,java .
        awt .Font .BOLD ,12 ), java. awt. Color. red) , getBorder( )) )
        ;  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
        ) {if ("borde\u0072" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} )
        ;
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {153, 11, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {123, 214, 187, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {0.01, 0.01, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.01, 0.0, 0.0, 1.0E-4};

        //======== scrollPane1 ========
        {

            //---- templateTable ----
            templateTable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                    "\u6a21\u677f\u540d\u79f0", "URL", "Headers", "Body"
                }
            ) {
                Class<?>[] columnTypes = new Class<?>[] {
                    String.class, String.class, String.class, String.class
                };
                boolean[] columnEditable = new boolean[] {
                    false, false, false, false
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
            templateTable.setPreferredScrollableViewportSize(null);
            templateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane1.setViewportView(templateTable);
        }
        add(scrollPane1, new GridBagConstraints(0, 0, 1, 3, 9.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 10), 0, 0));

        //======== panel1 ========
        {
            panel1.setLayout(new VerticalLayout());

            //---- defaultButton ----
            defaultButton.setText("\u8bbe\u4e3a\u9ed8\u8ba4\u6a21\u677f");
            defaultButton.addActionListener(e -> defaultButtonActionPerformed(e));
            panel1.add(defaultButton);

            //---- shareButton ----
            shareButton.setText("\u5206\u4eab");
            shareButton.addActionListener(e -> shareButtonActionPerformed(e));
            panel1.add(shareButton);

            //---- addButton ----
            addButton.setText("\u65b0\u589e");
            addButton.addActionListener(e -> addButtonActionPerformed(e));
            panel1.add(addButton);

            //---- editButton ----
            editButton.setText("\u4fee\u6539");
            editButton.addActionListener(e -> editButtonActionPerformed(e));
            panel1.add(editButton);

            //---- copyButton ----
            copyButton.setText("\u590d\u5236");
            copyButton.addActionListener(e -> copyButtonActionPerformed(e));
            panel1.add(copyButton);

            //---- importButton ----
            importButton.setText("\u5bfc\u5165");
            importButton.addActionListener(e -> importButtonActionPerformed(e));
            panel1.add(importButton);

            //---- deleteButton ----
            deleteButton.setText("\u5220\u9664");
            deleteButton.addActionListener(e -> deleteButtonActionPerformed(e));
            panel1.add(deleteButton);
        }
        add(panel1, new GridBagConstraints(1, 0, 1, 2, 1.0, 5.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JScrollPane scrollPane1;
    private JTable templateTable;
    private JPanel panel1;
    private JButton defaultButton;
    private JButton shareButton;
    private JButton addButton;
    private JButton editButton;
    private JButton copyButton;
    private JButton importButton;
    private JButton deleteButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
