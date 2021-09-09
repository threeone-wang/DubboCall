package com.yitulin.dubbocall.infrastructure.analysis;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

/**
 * @Author: ⚡️
 * @Description:
 * @Date: Created in 2021/7/18 19:33
 * Modified By:
 */
public class PsiClassUtil {

    private static final Set<String> PASS_KEY = Sets.newHashSet("serialVersionUID");

    public static RussianDollMap getDefaultValue(PsiClass psiClass) {
        RussianDollMap russianDollMap = RussianDollMap.create();
        if (Objects.isNull(psiClass)){
            return russianDollMap;
        }

        for (PsiField field : psiClass.getAllFields()) {
            PsiType fieldType = field.getType();
            String name = field.getName();
            if (PASS_KEY.contains(name)){
                continue;
            }
            Object defaultValue = PsiTypeUtil.getDefaultValue(fieldType);
            russianDollMap.set(name,defaultValue);
        }
        return russianDollMap;
    }

}
