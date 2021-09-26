package com.yitulin.dubbocall.infrastructure.analysis;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.compiled.ClsClassImpl;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.yitulin.dubbocall.domain.request.entity.MethodDetailEntity;
import com.yitulin.dubbocall.infrastructure.exception.DubboCallException;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/7/18 19:00
 * Modified By:
 */
public class MethodAnalysisUtil {

    public static MethodDetailEntity analysis(PsiFile psiFile, PsiMethod psiMethod){
        MethodDetailEntity methodDetailEntity = MethodDetailEntity.builder().build();
        StringBuilder methodSignature=new StringBuilder();

        if (psiMethod.getParent() instanceof PsiClassImpl){
            methodDetailEntity.setPackagePath(((PsiJavaFile)psiFile.getContainingFile()).getPackageName());
            methodDetailEntity.setInterfaceName(((PsiClassImpl) psiMethod.getParent()).getName());
        }else if (psiMethod.getParent() instanceof ClsClassImpl){
            String interfaceQualifiedName = ((ClsClassImpl) psiMethod.getParent()).getQualifiedName();
            methodDetailEntity.setPackagePath(interfaceQualifiedName.substring(0,interfaceQualifiedName.lastIndexOf(".")));
            methodDetailEntity.setInterfaceName(interfaceQualifiedName.substring(interfaceQualifiedName.lastIndexOf(".")+1));
        }else {
            throw new DubboCallException("不支持的方法位置");
        }
        methodDetailEntity.setMethodName(psiMethod.getName());

        methodSignature.append(methodDetailEntity.getPackagePath()).append(".").append(methodDetailEntity.getInterfaceName()).append(".");
        methodSignature.append(methodDetailEntity.getMethodName()).append("(");

        boolean firstParameter=true;

        PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();
        List<Object> paramsList= Lists.newArrayList();
        for (PsiParameter psiParameter : psiParameters) {
            PsiType psiParameterType = psiParameter.getType();
            if (firstParameter){
                firstParameter=false;
                methodSignature.append(psiParameterType.getPresentableText());
            }else {
                methodSignature.append(",").append(psiParameterType.getPresentableText());
            }
            paramsList.add(PsiTypeUtil.getDefaultValue(psiParameterType));
            // String canonicalText = psiParameterType.getCanonicalText();
            // @NotNull PsiFile[] filesByName = FilenameIndex.getFilesByName(project, canonicalText+".java", GlobalSearchScope.EMPTY_SCOPE);
        }
        methodSignature.append(")");
        methodDetailEntity.setMethodSignature(methodSignature.toString());
        methodDetailEntity.setParamsJsonStr(JSON.toJSONString(paramsList));
        return methodDetailEntity;
    }

}
