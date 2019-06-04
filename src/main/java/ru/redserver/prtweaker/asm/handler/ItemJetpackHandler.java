package ru.redserver.prtweaker.asm.handler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.redserver.prtweaker.asm.ASMHelper;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Фикс краша ItemJetpack
 * @author TheAndrey
 */
public final class ItemJetpackHandler implements IClassHandler {

	@Override
	public boolean accept(String name) {
		return name.equals("mrtjp.projectred.expansion.ItemJetpack$");
	}

	@Override
	public boolean transform(ClassNode node) {
		MethodNode method = ASMHelper.findMethod(node, "onRenderTick", "(Lcpw/mods/fml/common/gameevent/TickEvent$ClientTickEvent;)V");

		LabelNode label = new LabelNode();
		InsnList list = new InsnList();
		// Получение клиентского мира
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "func_71410_x", "()Lnet/minecraft/client/Minecraft;", false));
		list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "field_71441_e", "Lnet/minecraft/client/multiplayer/WorldClient;"));
		// Проверка на null
		list.add(new JumpInsnNode(Opcodes.IFNONNULL, label));
		list.add(new InsnNode(Opcodes.RETURN));
		list.add(label);
		list.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null)); // Фикс `Expecting a stackmap frame`

		method.instructions.insert(list);

		LogHelper.info("ItemJetpack patched.");
		return true;
	}

}
