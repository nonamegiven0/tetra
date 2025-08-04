package se.mickelus.tetra.gui.stats.data;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.ForgeRegistries;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.StatGetterAdd;
import se.mickelus.tetra.gui.stats.getter.StatGetterAnd;
import se.mickelus.tetra.gui.stats.getter.StatGetterAttribute;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectEfficiency;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterEnchantmentLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterMultiply;
import se.mickelus.tetra.gui.stats.getter.StatGetterOr;

public class StatGetterDeserializers {
    public static IStatGetter andGetter(JsonElement json) {
        AndData data = StatRegistry.gson.fromJson(json, AndData.class);
        return new StatGetterAnd(data.stats);
    }

    record AndData(IStatGetter[] stats) {
    }

    public static IStatGetter orGetter(JsonElement json) {
        OrData data = StatRegistry.gson.fromJson(json, OrData.class);
        return new StatGetterOr(data.stats);
    }

    record OrData(IStatGetter[] stats) {
    }

    public static IStatGetter sumGetter(JsonElement json) {
        SumData data = StatRegistry.gson.fromJson(json, SumData.class);
        return new StatGetterAdd(data.offset != null ? data.offset : 0, data.stats);
    }

    record SumData(IStatGetter[] stats, Double offset) {
    }

    public static IStatGetter multiplyGetter(JsonElement json) {
        MultiplyData data = StatRegistry.gson.fromJson(json, MultiplyData.class);
        return new StatGetterMultiply(data.factor != null ? data.factor : 1, data.stats);
    }

    record MultiplyData(IStatGetter[] stats, Double factor) {
    }

    public static IStatGetter attributeGetter(JsonElement json) {
        AttributeData data = StatRegistry.gson.fromJson(json, AttributeData.class);
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.parse(data.attribute));
        if (attribute == null) {
            throw new JsonParseException("Failed to parse attribute stat getter, unknown attribute: " + data.attribute);
        }

        return new StatGetterAttribute(
                attribute,
                data.ignoreBase != null ? data.ignoreBase : false,
                data.ignoreBonuses != null ? data.ignoreBonuses : false);
    }

    record AttributeData(String attribute, @Nullable Boolean ignoreBase, @Nullable Boolean ignoreBonuses) {
    }

    public static IStatGetter effectEfficiencyGetter(JsonElement json) {
        EffectEfficiencyData data = StatRegistry.gson.fromJson(json, EffectEfficiencyData.class);
        if (data.effect == null) {
            throw new JsonParseException("Failed to parse effect efficiency stat getter, missing field 'effect'");
        }
        return new StatGetterEffectEfficiency(ItemEffect.get(data.effect));
    }

    record EffectEfficiencyData(String effect) {
    }

    public static IStatGetter effectLevelGetter(JsonElement json) {
        EffectLevelData data = StatRegistry.gson.fromJson(json, EffectLevelData.class);
        if (data.effect == null) {
            throw new JsonParseException("Failed to parse effect level stat getter, missing field 'effect'");
        }
        return new StatGetterEffectLevel(ItemEffect.get(data.effect));
    }

    record EffectLevelData(String effect) {
    }
    @OnlyIn(Dist.CLIENT)
    public static IStatGetter enchantmentGetter(JsonElement json) {
        EnchantmentData data = StatRegistry.gson.fromJson(json, EnchantmentData.class);
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.parse(data.enchantment));
        if (enchantment == null) {
            throw new JsonParseException("Failed to parse enchantment stat getter, unknown enchantment: " + data.enchantment);
        }

        return new StatGetterEnchantmentLevel(enchantment);
    }

    record EnchantmentData(String enchantment) {
    }
}
