package se.mickelus.tetra;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import se.mickelus.tetra.blocks.forged.chthonic.ExtractorProjectileEntity;
import se.mickelus.tetra.blocks.forged.chthonic.ExtractorProjectileRenderer;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerBlockEntity;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerRenderer;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPistonBlockEntity;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPistonRenderer;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlockEntity;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseRenderer;
import se.mickelus.tetra.blocks.forged.hammer.HammerHeadBlockEntity;
import se.mickelus.tetra.blocks.forged.hammer.HammerHeadRenderer;
import se.mickelus.tetra.blocks.geode.particle.SparkleParticle;
import se.mickelus.tetra.blocks.geode.particle.SparkleParticleType;
import se.mickelus.tetra.blocks.multischematic.MultiblockSchematicGui;
import se.mickelus.tetra.blocks.scroll.ScrollRenderer;
import se.mickelus.tetra.blocks.scroll.ScrollTile;
import se.mickelus.tetra.blocks.workbench.WorkbenchContainer;
import se.mickelus.tetra.blocks.workbench.WorkbenchTESR;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchScreen;
import se.mickelus.tetra.client.ItemAbilityIconStore;
import se.mickelus.tetra.client.keymap.TetraKeyMappings;
import se.mickelus.tetra.client.model.ModularModelLoader;
import se.mickelus.tetra.client.particle.SweepingStrikeParticle;
import se.mickelus.tetra.client.particle.SweepingStrikeParticleType;
import se.mickelus.tetra.effect.gui.AbilityOverlays;
import se.mickelus.tetra.effect.howling.HowlingOverlay;
import se.mickelus.tetra.gui.stats.data.StatBarStore;
import se.mickelus.tetra.gui.stats.data.StatIndicatorStore;
import se.mickelus.tetra.gui.stats.data.StatRegistry;
import se.mickelus.tetra.gui.stats.data.StatSorterStore;
import se.mickelus.tetra.interactions.SecondaryInteractionOverlay;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;
import se.mickelus.tetra.items.modular.ThrownModularItemRenderer;
import se.mickelus.tetra.items.modular.impl.BlockProgressOverlay;
import se.mickelus.tetra.items.modular.impl.bow.RangedProgressOverlay;
import se.mickelus.tetra.items.modular.impl.crossbow.CrossbowOverlay;
import se.mickelus.tetra.items.modular.impl.holo.gui.scan.ScannerOverlayGui;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldBannerModel;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldModel;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldRenderer;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltContainer;
import se.mickelus.tetra.items.modular.impl.toolbelt.booster.OverlayBooster;
import se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay.ToolbeltOverlay;
import se.mickelus.tetra.items.modular.impl.toolbelt.gui.screen.ToolbeltScreen;

public class ClientSetup {
    public static void init(IEventBus modBus) {
        modBus.register(ClientSetup.class);
        NeoForge.EVENT_BUS.register(ClientSetup.class);

        StatRegistry.init();
        new StatIndicatorStore();
        new StatBarStore();
        new StatSorterStore();

        // todo: seems to cause issues during datagen
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(new ItemAbilityIconStore());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // enqueueWork swallows exceptions without logging
            try {
//                MenuScreens.register(WorkbenchContainer.containerType.get(), WorkbenchScreen::new);
                ModularModelLoader.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void menuScreenSetup(RegisterMenuScreensEvent event) {
    	try {
    		event.register(WorkbenchContainer.containerType.get(), WorkbenchScreen::new);
    	        event.register(ToolbeltContainer.type.get(), ToolbeltScreen::new);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SparkleParticleType.instance.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(SweepingStrikeParticleType.instance.get(), SweepingStrikeParticle.Provider::new);
    }

    @SubscribeEvent
    public static void modelRegistryReady(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "modular_loader"), new ModularModelLoader());
    }

    @SubscribeEvent
    public static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ForgedContainerRenderer.layer, ForgedContainerRenderer::createLayer);
        event.registerLayerDefinition(HammerBaseRenderer.layer, HammerBaseRenderer::createLayer);

        event.registerLayerDefinition(ScrollRenderer.layer, ScrollRenderer::createLayer);
        event.registerLayerDefinition(ModularShieldRenderer.layer, ModularShieldModel::createLayer);
        event.registerLayerDefinition(ModularShieldRenderer.bannerLayer, ModularShieldBannerModel::createLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ExtractorProjectileEntity.type.get(), ExtractorProjectileRenderer::new);
        event.registerEntityRenderer(ThrownModularItemEntity.type.get(), ThrownModularItemRenderer::new);

        event.registerBlockEntityRenderer(WorkbenchTile.type.get(), WorkbenchTESR::new);
        event.registerBlockEntityRenderer(ScrollTile.type.get(), ScrollRenderer::new);

        event.registerBlockEntityRenderer(ForgedContainerBlockEntity.type.get(), ForgedContainerRenderer::new);
        event.registerBlockEntityRenderer(CoreExtractorPistonBlockEntity.type.get(), CoreExtractorPistonRenderer::new);
        event.registerBlockEntityRenderer(HammerBaseBlockEntity.type.get(), HammerBaseRenderer::new);
        event.registerBlockEntityRenderer(HammerHeadBlockEntity.type.get(), HammerHeadRenderer::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
        var mc = Minecraft.getInstance();
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "howling"), new HowlingOverlay(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "ability_overlays"), new AbilityOverlays(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "toolbelt"), new ToolbeltOverlay(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "secondary_interaction"), new SecondaryInteractionOverlay(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "booster"), new OverlayBooster(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "block_progresss"), new BlockProgressOverlay(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "ranged_progresss"), new RangedProgressOverlay(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "crossbow"), new CrossbowOverlay(mc));
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "scanner"), new ScannerOverlayGui());
        registerOverlay(event, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "multiblock_schematic"), new MultiblockSchematicGui(mc));
    }

    private static void registerOverlay(RegisterGuiLayersEvent event, ResourceLocation id, LayeredDraw.Layer overlay) {
        event.registerBelowAll(id, overlay);
        NeoForge.EVENT_BUS.register(overlay);
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TetraKeyMappings.accessBinding);
        event.register(TetraKeyMappings.restockBinding);
        event.register(TetraKeyMappings.openBinding);
        event.register(TetraKeyMappings.secondaryUseBinding);
    }
}
