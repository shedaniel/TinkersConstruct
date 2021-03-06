package slimeknights.tconstruct.plugin.jei.casting;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

import javax.annotation.Nonnull;

public class CastingRecipeHandler implements IRecipeWrapperFactory<CastingRecipeWrapper> {
    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull CastingRecipeWrapper recipe) {
        return recipe;
    }
}
