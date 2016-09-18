package com.demonwav.mcdev.platform.mixin.inspections;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiTypeElement;
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
                if (MixinUtils.getMinecraftModule(typeElement) == null) {
                    return;
                }

                final Pair<PsiClass, PsiAnnotation> pair = MixinUtils.getMixinAnnotation(typeElement);
                if (pair == null) {
                    return;
                }

                registerError(typeElement.getParent(), pair.getFirst().getName());
            }
        };
    }
}
