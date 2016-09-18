package com.demonwav.mcdev.platform.mixin.inspections;

import com.demonwav.mcdev.platform.MinecraftModule;
import com.demonwav.mcdev.platform.mixin.MixinConstants;
import com.demonwav.mcdev.platform.mixin.MixinModuleType;
import com.demonwav.mcdev.util.McPsiUtil;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.util.PsiTypesUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class MixinUtils {

    /**
     * Get the {@link MinecraftModule} for the particular {@link PsiElement} if and only if the module the given {@link PsiElement} belongs
     * to is a {@link MixinModuleType}. If it does not meet that case, or is null, or the module lookup fails, null will be returned.
     *
     * @param element The element that is to be used to find the {@link MinecraftModule} for it's {@link Module}.
     * @return The {@link MinecraftModule} instance, or null if not a {@link MixinModuleType} {@link Module}.
     */
    @Nullable
    @Contract(value = "null -> null", pure = true)
    public static MinecraftModule getMinecraftModule(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        }

        final Module module = ModuleUtilCore.findModuleForPsiElement(element);
        if (module == null) {
            return null;
        }

        final MinecraftModule instance = MinecraftModule.getInstance(module);
        if (instance == null) {
            return null;
        }

        if (instance.isOfType(MixinModuleType.getInstance())) {
            return instance;
        }

        return null;
    }

    /***
     * Get the Mixin annotation for the given {@link PsiClass}, or null if it is not a Mixin class.
     *
     * @param psiClass The class to check.
     * @return The Mixin {@link PsiClass} and {@link PsiAnnotation} pair, or null if this class is not a Mixin class.
     */
    @Nullable
    @Contract(value = "null -> null", pure = true)
    public static Pair<PsiClass, PsiAnnotation> getMixinAnnotation(@Nullable final PsiClass psiClass) {
        if (psiClass == null) {
            return null;
        }

        final PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList == null) {
            return null;
        }

        return new Pair<>(psiClass, modifierList.findAnnotation(MixinConstants.MIXIN_ANNOTATION));
    }

    /**
     * Get the Mixin annotation for the given {@link PsiType}, or null if the type is not a Mixin class.
     *
     * @param type The type to check.
     * @return The Mixin {@link PsiClass} and {@link PsiAnnotation} pair, or null if this type does not represent a Mixin class.
     */
    @Nullable
    @Contract(value = "null -> null", pure = true)
    public static Pair<PsiClass, PsiAnnotation> getMixinAnnotation(@Nullable final PsiType type) {
        if (type == null) {
            return null;
        }

        final PsiClass psiClass = PsiTypesUtil.getPsiClass(type);
        return getMixinAnnotation(psiClass);
    }

    /**
     * Get the Mixin annotation for the given {@link PsiTypeElement}, or null if the type element is not a Mixin class.
     *
     * @param typeElement The type element to check.
     * @return The Mixin {@link PsiClass} and {@link PsiAnnotation} pair, or null if this type element does not represent a Mixin class.
     */
    @Nullable
    @Contract(value = "null -> null", pure = true)
    public static Pair<PsiClass, PsiAnnotation> getMixinAnnotation(@Nullable final PsiTypeElement typeElement) {
        if (typeElement == null) {
            return null;
        }

        return getMixinAnnotation(typeElement.getType());
    }

    /**
     * Get the Mixin annotation for the class that contains the given {@link PsiElement}, or null if the containing class is not a Mixin
     * class.
     *
     * @param element The element whose containing class is to be checked.
     * @return The Mixin {@link PsiClass} and {@link PsiAnnotation} pair, or null if this element's containing class is not a Mixin class.
     */
    @Nullable
    @Contract(value = "null -> null", pure = true)
    public static Pair<PsiClass, PsiAnnotation> getMixinAnnotation(@Nullable final PsiElement element) {
        if (element == null) {
            return null;
        }

        final PsiClass classOfElement = McPsiUtil.getClassOfElement(element);
        return getMixinAnnotation(classOfElement);
    }
}
