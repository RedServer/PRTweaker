package ru.redserver.prtweaker.asm.handler;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.Opcodes;
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
public final class InstancedBlockHandler implements IClassHandler {

	private static boolean patchApplied = false;

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
		insn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/redserver/prtweaker/util/Helpers", "isClientSide", "()Z", false));
		insn.add(new JumpInsnNode(Opcodes.IFNE, label));
		insn.add(new InsnNode(Opcodes.RETURN));
		insn.add(label);
		method.instructions.insert(insn);

		LogHelper.info("Added packet side check for InstancedBlockTile.");
		patchApplied = true;
		return true;
	}

	/**
	 * Используется для проверки успешной установки фикса
	 */
	public static void check() {
		try {
			Class.forName("mrtjp.core.block.InstancedBlockTile"); // Загружаем класс, если не был загружен ранее
			if(!patchApplied) throw new RuntimeException("InstancedBlockTile not patched!");
		} catch (ClassNotFoundException ex) {
			LogHelper.log(Level.FATAL, "Class not found!", ex);
		}
	}

}
