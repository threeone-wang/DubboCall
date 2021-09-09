/*
 * Created by JFormDesigner on Wed Aug 25 21:50:47 CST 2021
 */

package com.yitulin.dubbocall.ui.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.yitulin.dubbocall.domain.request.entity.HttpRequestCallEntity;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestCallLogEntity;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestTemplateEntity;
import com.yitulin.dubbocall.domain.request.entity.MethodDetailEntity;
import com.yitulin.dubbocall.domain.request.entity.VariableEntity;
import com.yitulin.dubbocall.infrastructure.config.CompatibleSettingService;
import com.yitulin.dubbocall.infrastructure.enums.ConfigTypeEnum;
import com.yitulin.dubbocall.infrastructure.utils.JacksonUtil;
import com.yitulin.dubbocall.infrastructure.utils.JsonUtil;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @author unknown
 */
public class HttpRequestCallDialog extends JDialog {

    private static final Log LOG = LogFactory.get();

    private CompatibleSettingService compatibleSettingService = CompatibleSettingService.getInstance();

    private HttpRequestCallEntity httpRequestCallEntity=HttpRequestCallEntity.builder().build();

    public HttpRequestCallDialog(MethodDetailEntity methodDetailEntity) {
        LOG.info("初始化请求调用对话框");
        initComponents();
        // ImageIcon icon= new ImageIcon(getClass().getResource("/images/\u53d1\u9001.png"));
        // Image img = icon.getImage();
        // Image newimg = img.getScaledInstance(sendRequestButton.getWidth()>0?sendRequestButton.getWidth():78, sendRequestButton.getHeight()>0?sendRequestButton.getHeight():30,
        //         java.awt.Image.SCALE_SMOOTH);
        // icon = new ImageIcon(newimg);
        // sendRequestButton.setIcon(icon);
        initData(methodDetailEntity);
    }

    private void initData(MethodDetailEntity methodDetailEntity){
        LOG.info("请求调用对话框初始化数据,params:[{}]",methodDetailEntity);
        httpRequestCallEntity.setMethodDetailEntity(methodDetailEntity);

        // 获取所有模板配置数据
        compatibleSettingService.readTemplateList().stream().forEach(item->templateNameComboBox.addItem(item.getName()));
        // 设定默认模板
        templateNameComboBox.setSelectedItem(compatibleSettingService.readDefaultTemplateName());

        // 模板数据解析，变量替换
        generateHttpRequestCallEntity(compatibleSettingService.readDefaultTemplateName());

        // 如果有请求记录，根据记录取数据展示
        if (refreshUiByLog(compatibleSettingService.readDefaultTemplateName())){
            return;
        }

        // 刷新ui数据
        refreshUi();
    }

    private void generateHttpRequestCallEntity(String templateName){
        Optional<HttpRequestTemplateEntity> requestTemplate = compatibleSettingService.readTemplateList().stream().filter(item -> templateName.equals(item.getName())).findFirst();
        if (requestTemplate.isEmpty()){
            return;
        }
        // 获取默认模板数据
        HttpRequestTemplateEntity templateEntity = requestTemplate.get();
        httpRequestCallEntity.setUrl(templateEntity.getUrl());
        httpRequestCallEntity.setHeaderJson(templateEntity.getHeaderJson());
        httpRequestCallEntity.setBodyJson(templateEntity.getBodyJson());

        // 获取全局配置数据
        List<VariableEntity> variableEntities = compatibleSettingService.readVariableList();

        // 根据模板，方法详情信息，全剧配置构建url信息、header信息、body信息
        Map<String, Object> methodDetailMap=JacksonUtil.object2Map(httpRequestCallEntity.getMethodDetailEntity());
        variableEntities.stream().forEach(item->{
            String key = item.getKey();
            String variableKey=String.format("${%s}",key);
            String variableValue=item.getValue();
            if (ConfigTypeEnum.SYSTEM.getType().equals(item.getType())){
                variableValue= String.valueOf(methodDetailMap.get(item.getKey()));
            }
            httpRequestCallEntity.setUrl(httpRequestCallEntity.getUrl().replace(variableKey,variableValue));
            httpRequestCallEntity.setHeaderJson(httpRequestCallEntity.getHeaderJson().replace(variableKey,variableValue));
            httpRequestCallEntity.setBodyJson(httpRequestCallEntity.getBodyJson().replace(variableKey,variableValue));
        });
    }

    private void refreshUi(){
        // 组件设置数据
        urlTextArea.setText(httpRequestCallEntity.getUrl());
        headerTextArea.setText(JsonUtil.prettyJsonStr(httpRequestCallEntity.getHeaderJson()));
        bodyTextArea.setText(JsonUtil.prettyJsonStr(httpRequestCallEntity.getBodyJson()));
    }

    private boolean refreshUiByLog(String templateName){
        // 如果有请求记录，根据记录取数据展示
        if (compatibleSettingService.readLogMap().containsKey(httpRequestCallEntity.concatLogKey(templateName))){
            refreshUiByLog(compatibleSettingService.readLogMap().get(httpRequestCallEntity.concatLogKey(templateName)));
            return true;
        }
        return false;
    }

    private void refreshUiByLog(HttpRequestCallLogEntity logEntity){
        // 组件设置数据
        urlTextArea.setText(logEntity.getUrl());
        headerTextArea.setText(JsonUtil.prettyJsonStr(logEntity.getHeaderJson()));
        bodyTextArea.setText(JsonUtil.prettyJsonStr(logEntity.getBodyJson()));
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    private void cleanResultButtonActionPerformed(ActionEvent e) {
        resultTextArea.setText("");
    }

    private void templateNameComboBoxActionPerformed(ActionEvent e) {
        String selectedTemplateName = templateNameComboBox.getSelectedItem().toString();
        // 如果有请求记录，根据记录取数据展示
        if (refreshUiByLog(selectedTemplateName)){
            return;
        }

        // 如果没有记录，根据模板设置生成默认的数据展示
        generateHttpRequestCallEntity(selectedTemplateName);
        refreshUi();
    }

    private void sendRequestButtonActionPerformed(ActionEvent e) {
        LOG.info("点击了发送请求按钮");
        HttpRequestCallEntity callEntity = HttpRequestCallEntity.builder()
                .methodDetailEntity(httpRequestCallEntity.getMethodDetailEntity())
                .url(urlTextArea.getText())
                .headerJson(JsonUtil.tightJsonStr(headerTextArea.getText()))
                .bodyJson(JsonUtil.tightJsonStr(bodyTextArea.getText()))
                .build();
        String httpResponseString = callEntity.sendHttpPost();
        httpResponseString = JSONUtil.formatJsonStr(httpResponseString);
        resultTextArea.setText(httpResponseString);

        String selectedTemplateName = templateNameComboBox.getSelectedItem().toString();
        HttpRequestCallLogEntity logEntity=HttpRequestCallLogEntity.builder()
                .methodSignature(callEntity.concatLogKey(selectedTemplateName))
                .url(callEntity.getUrl())
                .headerJson(callEntity.getHeaderJson())
                .bodyJson(callEntity.getBodyJson())
                .requestTime(LocalDateTime.now())
                .build();
        compatibleSettingService.writeLog(logEntity);
    }

    private void resetUrlButtonActionPerformed(ActionEvent e) {
        urlTextArea.setText(httpRequestCallEntity.getUrl());
    }

    private void resetHeaderButtonActionPerformed(ActionEvent e) {
        headerTextArea.setText(JsonUtil.prettyJsonStr(httpRequestCallEntity.getHeaderJson()));
    }

    private void resetBodyButtonActionPerformed(ActionEvent e) {
        bodyTextArea.setText(JsonUtil.prettyJsonStr(httpRequestCallEntity.getBodyJson()));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        templateNameComboBox = new JComboBox();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        urlTextArea = new JTextArea();
        resetUrlButton = new JButton();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        headerTextArea = new JTextArea();
        resetHeaderButton = new JButton();
        label4 = new JLabel();
        scrollPane3 = new JScrollPane();
        bodyTextArea = new JTextArea();
        resetBodyButton = new JButton();
        separator1 = new JSeparator();
        label5 = new JLabel();
        scrollPane4 = new JScrollPane();
        resultTextArea = new JTextArea();
        cleanResultButton = new JButton();
        buttonBar = new JPanel();
        sendRequestButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("\u53d1\u8d77\u8bf7\u6c42");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder(
                    0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder
                    . BOTTOM, new java .awt .Font ("Dialo\u0067" ,java .awt .Font .BOLD ,12 ), java. awt. Color.
                    red) ,dialogPane. getBorder( )) ); dialogPane. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
                beans .PropertyChangeEvent e) {if ("borde\u0072" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                label1.setText("\u6a21\u677f\uff1a");
                contentPanel.add(label1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- templateNameComboBox ----
                templateNameComboBox.addActionListener(e -> templateNameComboBoxActionPerformed(e));
                contentPanel.add(templateNameComboBox, new GridBagConstraints(1, 0, 1, 1, 9.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- label2 ----
                label2.setText("url\uff1a");
                contentPanel.add(label2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane1 ========
                {

                    //---- urlTextArea ----
                    urlTextArea.setColumns(50);
                    urlTextArea.setRows(3);
                    urlTextArea.setLineWrap(true);
                    scrollPane1.setViewportView(urlTextArea);
                }
                contentPanel.add(scrollPane1, new GridBagConstraints(1, 1, 1, 1, 9.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- resetUrlButton ----
                resetUrlButton.setText("\u91cd\u7f6eurl");
                resetUrlButton.addActionListener(e -> resetUrlButtonActionPerformed(e));
                contentPanel.add(resetUrlButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- label3 ----
                label3.setText("header\uff1a");
                contentPanel.add(label3, new GridBagConstraints(0, 2, 1, 1, 1.0, 2.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane2 ========
                {

                    //---- headerTextArea ----
                    headerTextArea.setRows(6);
                    headerTextArea.setColumns(50);
                    headerTextArea.setLineWrap(true);
                    scrollPane2.setViewportView(headerTextArea);
                }
                contentPanel.add(scrollPane2, new GridBagConstraints(1, 2, 1, 1, 9.0, 2.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- resetHeaderButton ----
                resetHeaderButton.setText("\u91cd\u7f6e\u8bf7\u6c42\u5934");
                resetHeaderButton.addActionListener(e -> resetHeaderButtonActionPerformed(e));
                contentPanel.add(resetHeaderButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- label4 ----
                label4.setText("body\uff1a");
                contentPanel.add(label4, new GridBagConstraints(0, 3, 1, 1, 1.0, 3.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane3 ========
                {

                    //---- bodyTextArea ----
                    bodyTextArea.setColumns(50);
                    bodyTextArea.setRows(7);
                    bodyTextArea.setLineWrap(true);
                    scrollPane3.setViewportView(bodyTextArea);
                }
                contentPanel.add(scrollPane3, new GridBagConstraints(1, 3, 1, 1, 9.0, 2.9,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- resetBodyButton ----
                resetBodyButton.setText("\u91cd\u7f6e\u8bf7\u6c42\u4f53");
                resetBodyButton.addActionListener(e -> resetBodyButtonActionPerformed(e));
                contentPanel.add(resetBodyButton, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- separator1 ----
                separator1.setForeground(Color.red);
                separator1.setBackground(Color.red);
                contentPanel.add(separator1, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- label5 ----
                label5.setText("\u8bf7\u6c42\u7ed3\u679c\uff1a");
                contentPanel.add(label5, new GridBagConstraints(0, 5, 1, 1, 1.0, 3.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 5), 0, 0));

                //======== scrollPane4 ========
                {

                    //---- resultTextArea ----
                    resultTextArea.setRows(13);
                    resultTextArea.setLineWrap(true);
                    scrollPane4.setViewportView(resultTextArea);
                }
                contentPanel.add(scrollPane4, new GridBagConstraints(1, 5, 1, 1, 9.0, 3.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- cleanResultButton ----
                cleanResultButton.setText("\u6e05\u7a7a\u7ed3\u679c");
                cleanResultButton.addActionListener(e -> cleanResultButtonActionPerformed(e));
                contentPanel.add(cleanResultButton, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- sendRequestButton ----
                sendRequestButton.setText("\u53d1\u9001");
                sendRequestButton.setHorizontalTextPosition(SwingConstants.CENTER);
                sendRequestButton.setBackground(Color.red);
                sendRequestButton.addActionListener(e -> sendRequestButtonActionPerformed(e));
                buttonBar.add(sendRequestButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("\u5173\u95ed");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(650, 600);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JComboBox templateNameComboBox;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTextArea urlTextArea;
    private JButton resetUrlButton;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JTextArea headerTextArea;
    private JButton resetHeaderButton;
    private JLabel label4;
    private JScrollPane scrollPane3;
    private JTextArea bodyTextArea;
    private JButton resetBodyButton;
    private JSeparator separator1;
    private JLabel label5;
    private JScrollPane scrollPane4;
    private JTextArea resultTextArea;
    private JButton cleanResultButton;
    private JPanel buttonBar;
    private JButton sendRequestButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
