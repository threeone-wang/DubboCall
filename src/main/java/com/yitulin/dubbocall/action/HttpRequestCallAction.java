package com.yitulin.dubbocall.action;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.yitulin.dubbocall.domain.request.entity.MethodDetailEntity;
import com.yitulin.dubbocall.infrastructure.analysis.MethodAnalysisUtil;
import com.yitulin.dubbocall.infrastructure.enums.ErrorCode;
import com.yitulin.dubbocall.infrastructure.exception.DubboCallException;
import com.yitulin.dubbocall.infrastructure.utils.AlertMessageUtil;
import com.yitulin.dubbocall.ui.dialog.HttpRequestCallDialog;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/8/25 21:47
 * Modified By:
 */
public class HttpRequestCallAction extends AnAction {

    private static final Log LOG =LogFactory.get();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info("唤起了http请求弹窗:[{}]",e);
        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        if (!(psiElement instanceof PsiMethod)){
            Messages.showErrorDialog("仅支持对方法使用","不适用的目标");
            return;
        }

        Project project = e.getProject();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        PsiMethod psiMethod= (PsiMethod) psiElement;
        try {
            MethodDetailEntity methodDetailEntity = MethodAnalysisUtil.analysis(psiFile, psiMethod);
            methodDetailEntity.setProjectName(project.getName());
            HttpRequestCallDialog requestCallDialog=new HttpRequestCallDialog(methodDetailEntity);
            requestCallDialog.setVisible(true);
        }catch (DubboCallException exception){
            exception.printStackTrace();
            AlertMessageUtil.alertErrorInfo(exception.getMessage(), ErrorCode.HTTP_REQUEST_CALL_ERROR.getTitle());
        }
    }
}
