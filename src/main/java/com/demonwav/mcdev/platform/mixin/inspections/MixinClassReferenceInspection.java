package com.demonwav.mcdev.platform.mixin.inspections;

import com.demonwav.mcdev.platform.MinecraftModule;
import com.demonwav.mcdev.platform.mixin.MixinConstants;
import com.demonwav.mcdev.platform.mixin.MixinModuleType;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.util.PsiTypesUtil;
import com.siyeh.ig.BaseInspection;
import com.siyeh.ig.BaseInspectionVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MixinClassReferenceInspection extends BaseInspection {

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Illegal Mixin Class Reference";
    }

    @NotNull
    @Override
    protected String buildErrorString(Object... infos) {
        return infos[0] + " cannot be referenced anywhere as it is a Mixin class.";
    }

    @Nullable
    @Override
    public String getStaticDescription() {
        return "A Mixin class may not reference the class it is mixing.";
    }

    @Override
    public BaseInspectionVisitor buildVisitor() {
        return new BaseInspectionVisitor() {
            @Override
            public void visitTypeElement(PsiTypeElement typeElement) {
                final Module module = ModuleUtilCore.findModuleForPsiElement(typeElement);
                if (module == null) {
                    return;
                }

                final MinecraftModule instance = MinecraftModule.getInstance(module);
                if (instance == null) {
                    return;
                }

                if (!instance.isOfType(MixinModuleType.getInstance())) {
                    return;
                }

                final PsiClass psiClass = PsiTypesUtil.getPsiClass(typeElement.getType());
                if (psiClass == null) {
                    return;
                }

                final PsiModifierList modifierList = psiClass.getModifierList();
                if (modifierList == null) {
                    return;
                }

                // Check if the class that is being referenced is a mixin class
                if (modifierList.findAnnotation(MixinConstants.MIXIN_ANNOTATION) == null) {
                    return;
                }

                registerError(typeElement.getParent(), psiClass.getName());
            }
        };
    }
}
