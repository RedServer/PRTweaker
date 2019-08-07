package ru.redserver.prtweaker.asm.handler;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.Item;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import static ru.redserver.prtweaker.ModPRTweaker.itemRoutingChip;
import ru.redserver.prtweaker.asm.ASMHelper;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Фикс уязвимости обработчика пакетов
 * @author TheAndrey
 */
public final class TransportationSPHHandler implements IClassHandler {

	private static final String METHOD_DESC = "(Lcodechicken/lib/packet/PacketCustom;Lnet/minecraft/entity/player/EntityPlayerMP;)V";
	private static boolean patchApplied = false;

	@Override
	public boolean accept(String name) {
		return name.equals("mrtjp.projectred.transportation.TransportationSPH$");
	}

	@Override
	public boolean transform(ClassNode node) {
		MethodNode method = ASMHelper.findMethod(node, "setChipNBT", METHOD_DESC);
		method.instructions.clear();
		method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1)); // #1 arg - packet
		method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2)); // #2 arg - player
		method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/redserver/prtweaker/util/Helpers", "setChipNBTSafe", METHOD_DESC, false));
		method.instructions.add(new InsnNode(Opcodes.RETURN));

		LogHelper.info("TransportationSPH fix applied!");
		patchApplied = true;
		return true;
	}

	/**
	 * Используется для проверки успешной установки фикса
	 */
	public static void check() {
		if(!Loader.isModLoaded("ProjRed|Transportation")) return;
		itemRoutingChip = (Item)Item.itemRegistry.getObject("ProjRed|Transportation:projectred.transportation.routingchip");
		if(itemRoutingChip == null) throw new RuntimeException("Can't get routing chip item!");
		if(!patchApplied) throw new RuntimeException("ProjRed|Transportation patch not installied!");
	}

}
