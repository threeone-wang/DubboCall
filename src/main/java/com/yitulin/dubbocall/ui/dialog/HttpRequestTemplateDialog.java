/*
 * Created by JFormDesigner on Wed Aug 25 10:39:04 CST 2021
 */

package com.yitulin.dubbocall.ui.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.common.collect.Lists;
import com.intellij.openapi.ui.Messages;
import com.yitulin.dubbocall.domain.request.entity.HttpRequestTemplateEntity;
import com.yitulin.dubbocall.infrastructure.enums.ErrorCode;
import com.yitulin.dubbocall.infrastructure.utils.JsonUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author unknown
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HttpRequestTemplateDialog extends JDialog {
    
    private boolean clickOkButtonTag=false;
    private boolean editMode=false;
    private String oldTemplateName;
    private List<String> templateNameList = Lists.newArrayList();

    public HttpRequestTemplateDialog(Window owner,List<String> templateNameList) {
        super(owner);
        initComponents();
        this.templateNameList = templateNameList;
    }

    public void initEditInfo(HttpRequestTemplateEntity templateEntity){
        initEditInfo(templateEntity,true);
    }

    public void initEditInfo(HttpRequestTemplateEntity templateEntity,boolean editMode){
        this.editMode=editMode;
        this.oldTemplateName=templateEntity.getName();
        this.templateNameField.setText(templateEntity.getName());
        this.urlTextArea.setText(JsonUtil.prettyJsonStr(templateEntity.getUrl()));
        this.headerTextArea.setText(JsonUtil.prettyJsonStr(templateEntity.getHeaderJson()));
        this.bodyTextArea.setText(JsonUtil.prettyJsonStr(templateEntity.getBodyJson()));
    }

    public HttpRequestTemplateEntity collectTemplateEntity(){
        HttpRequestTemplateEntity templateEntity = HttpRequestTemplateEntity.builder()
                .name(templateNameField.getText())
                .url(JsonUtil.tightJsonStr(urlTextArea.getText()))
                .headerJson(JsonUtil.tightJsonStr(headerTextArea.getText()))
                .bodyJson(JsonUtil.tightJsonStr(bodyTextArea.getText()))
                .build();
        return templateEntity;
    }

    private void okButtonActionPerformed(ActionEvent e) {
        if (!this.editMode && this.templateNameList.contains(templateNameField.getText())){
            Messages.showErrorDialog("模板名称与已有模板冲突", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        if (this.editMode && !this.oldTemplateName.equals(templateNameField.getText()) && this.templateNameList.contains(templateNameField.getText())){
            Messages.showErrorDialog("模板名称与已有模板冲突", ErrorCode.HTTP_REQUEST_TEMPLATE_ERROR.getTitle());
            return;
        }
        this.clickOkButtonTag=true;
        this.setVisible(false);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        templateNameLabel = new JLabel();
        templateNameField = new JTextField();
        urlLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        urlTextArea = new JTextArea();
        headerLabel = new JLabel();
        scrollPane2 = new JScrollPane();
        headerTextArea = new JTextArea();
        bodyLabel = new JLabel();
        scrollPane3 = new JScrollPane();
        bodyTextArea = new JTextArea();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setModal(true);
        setTitle("\u6a21\u677f\u8bbe\u7f6e");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border
            .EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing.border.TitledBorder.CENTER,javax
            .swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,
            12),java.awt.Color.red),dialogPane. getBorder()));dialogPane. addPropertyChangeListener(new java.beans
            .PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.
            getPropertyName()))throw new RuntimeException();}});
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.01, 0.01, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.01, 0.01, 0.01, 0.01, 1.0E-4};

                //---- templateNameLabel ----
                templateNameLabel.setText("\u540d\u79f0\uff1a");
                contentPanel.add(templateNameLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 5, 10), 0, 0));

                //---- templateNameField ----
                templateNameField.setColumns(50);
                contentPanel.add(templateNameField, new GridBagConstraints(1, 0, 1, 1, 9.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- urlLabel ----
                urlLabel.setText("url\uff1a");
                contentPanel.add(urlLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 5, 10), 0, 0));

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
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- headerLabel ----
                headerLabel.setText("header\uff1a");
                contentPanel.add(headerLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 3.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 5, 10), 0, 0));

                //======== scrollPane2 ========
                {

                    //---- headerTextArea ----
                    headerTextArea.setColumns(50);
                    headerTextArea.setRows(6);
                    headerTextArea.setLineWrap(true);
                    scrollPane2.setViewportView(headerTextArea);
                }
                contentPanel.add(scrollPane2, new GridBagConstraints(1, 2, 1, 1, 9.0, 3.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- bodyLabel ----
                bodyLabel.setText("body\uff1a");
                contentPanel.add(bodyLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 5.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 10), 0, 0));

                //======== scrollPane3 ========
                {

                    //---- bodyTextArea ----
                    bodyTextArea.setColumns(50);
                    bodyTextArea.setRows(13);
                    bodyTextArea.setLineWrap(true);
                    scrollPane3.setViewportView(bodyTextArea);
                }
                contentPanel.add(scrollPane3, new GridBagConstraints(1, 3, 1, 1, 9.0, 5.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(650, 600);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel templateNameLabel;
    private JTextField templateNameField;
    private JLabel urlLabel;
    private JScrollPane scrollPane1;
    private JTextArea urlTextArea;
    private JLabel headerLabel;
    private JScrollPane scrollPane2;
    private JTextArea headerTextArea;
    private JLabel bodyLabel;
    private JScrollPane scrollPane3;
    private JTextArea bodyTextArea;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
