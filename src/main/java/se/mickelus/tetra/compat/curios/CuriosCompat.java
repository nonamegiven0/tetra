package se.mickelus.tetra.compat.curios;

import top.theillusivec4.curios.api.SlotTypeMessage;

import javax.annotation.ParametersAreNonnullByDefault;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

@ParametersAreNonnullByDefault
public class CuriosCompat {
    public static final String modId = "curios";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void enqueueIMC(InterModEnqueueEvent event) {
        if (CuriosCompat.isLoaded) {
            InterModComms.sendTo(modId, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("belt").size(1).build());
        }
    }
}
