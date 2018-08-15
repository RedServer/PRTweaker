package ru.redserver.prtweaker.asm.handler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import ru.redserver.prtweaker.asm.ASMHelper;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Отключает BlockUpdateHandler
 * @author TheAndrey
 */
public final class BlockUpdateHandler implements IClassHandler {

	public static final String CONFIG_CLASS = "ru.redserver.prtweaker.TweakerConfig".replace(".", "/");

	@Override
	public boolean accept(String name) {
		return name.equals("mrtjp.core.world.BlockUpdateHandler$");
	}

	@Override
	public boolean transform(ClassNode node) {
		MethodNode register = ASMHelper.findMethod(node, "register", "(Lmrtjp/core/world/IBlockEventHandler;)V");

		InsnList list = new InsnList();
		LabelNode label = new LabelNode();
		list.add(new FieldInsnNode(Opcodes.GETSTATIC, CONFIG_CLASS, "enableBlockUpdateHandler", "Z"));
		list.add(new JumpInsnNode(Opcodes.IFNE, label));
		list.add(new InsnNode(Opcodes.RETURN));
		list.add(label);
		register.instructions.insert(list);

		LogHelper.info("BlockUpdateHandler patched.");
		return true;
	}

}
