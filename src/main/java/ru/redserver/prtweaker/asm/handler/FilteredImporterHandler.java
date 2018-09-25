package ru.redserver.prtweaker.asm.handler;

import java.util.ListIterator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.redserver.prtweaker.asm.ASMHelper;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Фикс TileFilteredImporter
 * @author TheAndrey
 */
public final class FilteredImporterHandler implements IClassHandler {

	@Override
	public boolean accept(String name) {
		return name.equals("mrtjp.projectred.expansion.TileFilteredImporter");
	}

	@Override
	public boolean transform(ClassNode node) {
		MethodNode method = ASMHelper.findMethod(node, "func_94128_d", "(I)[I"); // getAccessibleSlotsFromSide()

		ListIterator<AbstractInsnNode> it = method.instructions.iterator();
		while(it.hasNext()) {
			AbstractInsnNode insn = it.next();
			if(insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode call = (MethodInsnNode)insn;
				if(call.owner.equals("scala/runtime/RichInt$") && call.name.equals("to$extension0")) {
					// Предыдущая инструкция с числом
					AbstractInsnNode prev = insn.getPrevious();
					if(prev.getType() == AbstractInsnNode.INT_INSN) {
						IntInsnNode var = (IntInsnNode)prev;
						if(var.operand == 9) {
							var.operand = 8; // Фикс максимального индекса слота
							LogHelper.info("Fixed TileFilteredImporter slots array length.");
							return true;
						}
					}
				}
			}
		}

		return false;
	}

}
