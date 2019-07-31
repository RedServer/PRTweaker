package ru.redserver.prtweaker.asm.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.redserver.prtweaker.asm.ASMHelper;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Фикс уязвимости InstancedBlockTile (обработка сервером клиентских пакетов)
 * @author TheAndrey
 */
public final class InstancedBlockTile implements IClassHandler {

	public static boolean patchApplied = false;

	@Override
	public boolean accept(String name) {
		return name.equals("mrtjp.core.block.InstancedBlockTile");
	}

	@Override
	public boolean transform(ClassNode node) {
		MethodNode method = ASMHelper.findMethod(node, "handleDescriptionPacket", "(Lcodechicken/lib/packet/PacketCustom;)V");

		// Запрещаем серверу обрабатывать DescriptionPacket - они предназначены для обновления блоков в КЛИЕНТСКОМ мире
		LabelNode label = new LabelNode();
		InsnList insn = new InsnList();
		insn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(InstancedBlockTile.class), "isClientSide", "()Z", false));
		insn.add(new JumpInsnNode(Opcodes.IFNE, label));
		insn.add(new InsnNode(Opcodes.RETURN));
		insn.add(label);
		method.instructions.insert(insn);

		LogHelper.info("Added packet side check for InstancedBlockTile.");
		patchApplied = true;
		return true;
	}

	public static boolean isClientSide() {
		return FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}

}
