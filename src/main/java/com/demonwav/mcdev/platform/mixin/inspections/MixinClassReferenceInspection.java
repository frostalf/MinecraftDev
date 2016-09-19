package com.demonwav.mcdev.platform.mixin.inspections;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
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
        return infos[0] + " cannot be referenced anywhere as it is a Mixin class and will not exist at runtime.";
    }

    @Nullable
    @Override
    public String getStaticDescription() {
        return "A Mixin class can never be referenced anywhere, as it will not exist at runtime.";
    }

    @Override
    public BaseInspectionVisitor buildVisitor() {
        return new BaseInspectionVisitor() {
            @Override
            public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
                if (MixinUtils.getMinecraftModule(reference) == null) {
                    return;
                }

                final PsiElement resolve = reference.resolve();
                if (resolve == null) {
                    return;
                }

                if (!(resolve instanceof PsiClass)) {
                    return;
                }

                final Pair<PsiClass, PsiAnnotation> pair = MixinUtils.getMixinAnnotation((PsiClass) resolve);
                if (pair == null) {
                    return;
                }

                // Mixin classes can extend Mixin classes
                if (
                    reference.getNode().getTreeParent().findChildByType(JavaTokenType.EXTENDS_KEYWORD) != null ||
                    reference.getNode().getTreeParent().findChildByType(JavaTokenType.IMPLEMENTS_KEYWORD) != null
                ) {
                    // Make sure the class that is doing the extending is also a Mixin class
                    if (MixinUtils.getContainingMixinAnnotation(reference) != null) {
                        return;
                    }
                }

                registerError(reference, pair.getFirst().getName());
            }
        };
    }
}
