package ru.redserver.prtweaker.asm.handler;

import java.util.ListIterator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.redserver.prtweaker.asm.ASMHelper;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Исправляет краш TTimerGateLogic (Fabrication)
 * @author TheAndrey
 */
public final class TimerGateLogicHandler implements IClassHandler {

	@Override
	public boolean accept(String name) {
		return name.equals("mrtjp.projectred.fabrication.TTimerGateLogic$class");
	}

	@Override
	public boolean transform(ClassNode node) {
		MethodNode getTotalTime = ASMHelper.findMethod(node, "getTotalTime", "(Lmrtjp/projectred/fabrication/TTimerGateLogic;)J");

		// Ищем, где идёт получение мира
		ListIterator<AbstractInsnNode> it = getTotalTime.instructions.iterator();
		MethodInsnNode getWorldcall = null;
		while(it.hasNext()) {
			AbstractInsnNode insn = it.next();

			if(insn.getType() == AbstractInsnNode.METHOD_INSN) {
				MethodInsnNode mcall = (MethodInsnNode)insn;
				if(mcall.owner.equals("codechicken/multipart/handler/MultipartSaveLoad$") && mcall.name.equals("loadingWorld")) {
					getWorldcall = mcall;
					break;
				}
			}
		}

		if(getWorldcall == null) {
			LogHelper.warn("Unable to find MultipartSaveLoad.loadingWorld! Skipping fix.");
			return false;
		}

		// Внедряем условие, которое будет возвращать saveTime(), когда world == null
		int localIndex = getTotalTime.maxLocals + 1;
		InsnList list = new InsnList();
		LabelNode label = new LabelNode();
		list.add(new VarInsnNode(Opcodes.ASTORE, localIndex)); // Сохраняем результат в переменную
		list.add(new VarInsnNode(Opcodes.ALOAD, localIndex));
		list.add(new JumpInsnNode(Opcodes.IFNONNULL, label));
		list.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
		list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "mrtjp/projectred/fabrication/TTimerGateLogic", "saveTime", "()J", true));
		list.add(new InsnNode(Opcodes.LRETURN));
		list.add(label);
		list.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null)); // Фикс `Expecting a stackmap frame`
		list.add(new VarInsnNode(Opcodes.ALOAD, localIndex)); // Помещаем ранее сохранённую переменную обратно в стек
		getTotalTime.instructions.insert(getWorldcall, list);
		getTotalTime.maxLocals += 2;

		LogHelper.info("TTimerGateLogic patched.");
		return true;
	}

}
