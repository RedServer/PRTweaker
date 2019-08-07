package ru.redserver.prtweaker.asm.handler;

import java.util.HashMap;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import ru.redserver.prtweaker.util.LogHelper;

/**
 * Исправляет закрытие GUI, когда игрок далеко (дюп с отгрузкой чанка)
 * @author TheAndrey
 */
public final class ContainerHandler implements IClassHandler {

	private final HashMap<String, String[]> classes = new HashMap<>();

	public ContainerHandler() {
		classes.put("mrtjp.projectred.expansion.ContainerProjectBench", new String[]{"mrtjp$projectred$expansion$ContainerProjectBench$$tile", "Lmrtjp/projectred/expansion/TileProjectBench;"});
		classes.put("mrtjp.projectred.expansion.ContainerBlockPlacer", new String[]{"mrtjp$projectred$expansion$ContainerBlockPlacer$$tile", "Lmrtjp/projectred/expansion/TileBlockPlacer;"});
		classes.put("mrtjp.projectred.expansion.ContainerPoweredMachine", new String[]{"mrtjp$projectred$expansion$ContainerPoweredMachine$$tile", "Lmrtjp/projectred/expansion/TPoweredMachine;"});
		classes.put("mrtjp.projectred.fabrication.ContainerPrinter", new String[]{"mrtjp$projectred$fabrication$ContainerPrinter$$tile", "Lmrtjp/projectred/fabrication/TileICPrinter;"});
	}

	@Override
	public boolean accept(String name) {
		return classes.containsKey(name);
	}

	@Override
	public boolean transform(ClassNode node) {
		String name = node.name.replace("/", ".");
		String[] entry = classes.get(name);

		if(entry != null) {
			writeMethod(node, entry[0], entry[1]);
			LogHelper.info("Fixed container close: " + name);
			return true;
		}
		return false;
	}

	/**
	 * Создаёт метод в классе
	 * @param node Класс
	 * @param field Имя поля с тайлом
	 * @param desc Описание поля (тип) с тайлом
	 */
	private void writeMethod(ClassNode node, String field, String desc) {
		MethodVisitor mv = node.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "func_75145_c", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", null, null); // canInteractWith()

		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, node.name, field, desc);
		mv.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/tileentity/TileEntity");
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ru/redserver/prtweaker/util/Helpers", "isUseableByPlayer", "(Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/entity/player/EntityPlayer;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitEnd();
	}

}
