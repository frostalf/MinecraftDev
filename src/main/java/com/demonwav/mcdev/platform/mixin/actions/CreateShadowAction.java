package com.demonwav.mcdev.platform.mixin.actions;

import com.demonwav.mcdev.platform.MinecraftModule;
import com.demonwav.mcdev.util.McPsiUtil;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.execution.configurations.SearchScopeProvider;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtilCore;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackageStatement;
import com.intellij.psi.PsiResolveHelper;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.tree.JavaElementType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.search.PsiSearchRequest;
import com.intellij.psi.search.PsiSearchScopeUtil;
import com.intellij.psi.search.SearchRequestCollector;
import com.intellij.psi.search.SearchSession;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CreateShadowAction extends CodeInsightAction {

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        final Module module = ModuleUtilCore.findModuleForPsiElement(file);
        if (module == null) {
            return false;
        }

        final MinecraftModule instance = MinecraftModule.getInstance(module);
        if (instance == null) {
            return false;
        }

        final ASTNode node = file.getNode().findChildByType(JavaElementType.PACKAGE_STATEMENT);
        if (node == null) {
            return false;
        }

        PsiElement psi = node.getPsi();
        if (psi == null) {
            return false;
        }

        if (!(psi instanceof PsiPackageStatement)) {
            return false;
        }

        final PsiPackageStatement packageSt = (PsiPackageStatement) psi;

        return !packageSt.getPackageName().startsWith("java");
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return new CreateShadowHandler();
    }
}

class CreateShadowHandler implements CodeInsightActionHandler {
    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        final PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        if (element == null) {
            return;
        }

        final PsiField field = PsiTreeUtil.getParentOfType(element, PsiField.class);
        if (field == null) {
            return;
        }

        final PsiClass parent = McPsiUtil.getClassOfElement(field);
        if (parent == null) {
            return;
        }

        
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }
}