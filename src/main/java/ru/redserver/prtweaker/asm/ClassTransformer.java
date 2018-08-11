package ru.redserver.prtweaker.asm;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public final class ClassTransformer implements IClassTransformer {

	public static boolean patched = false;

	@Override
	public byte[] transform(String origName, String name, byte[] bytes) {
		if(name.equals("mrtjp.projectred.transportation.TransportationSPH$")) {
			ClassNode node = ASMHelper.readClass(bytes);
			processTransportationSPH(node);
			bytes = ASMHelper.writeClass(node, 0);
			FMLRelaunchLog.info(name + "patched!");
		}
		return bytes;
	}

	private void processTransportationSPH(ClassNode node) {
		MethodNode method = ASMHelper.findMethod(node, "setChipNBT", "(Lcodechicken/lib/packet/PacketCustom;Lnet/minecraft/entity/player/EntityPlayerMP;)V");
		method.instructions.clear();
		method.instructions.add(new InsnNode(Opcodes.RETURN));

		patched = true;
	}

}
