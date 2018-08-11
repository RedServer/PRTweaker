package ru.redserver.prtweaker.asm;

import java.util.ArrayList;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.ClassNode;
import ru.redserver.prtweaker.asm.handler.*;

public final class ClassTransformer implements IClassTransformer {

	public static boolean DUMP_CLASSES = false;
	private final ArrayList<IClassHandler> handlers = new ArrayList<>();

	public ClassTransformer() {
		handlers.add(new TransportationSPHHandler());
	}

	@Override
	public byte[] transform(String origName, String name, byte[] bytes) {
		if(bytes != null && bytes.length > 0) {
			ClassNode node = null;
			boolean transformed = false;

			// Обработка
			for(IClassHandler handler : handlers) {
				if(handler.accept(name)) {
					if(node == null) node = ASMHelper.readClass(bytes);
					transformed |= handler.transform(node);
				}
			}

			// Результат
			if(node != null && transformed) {
				bytes = ASMHelper.writeClass(node, 0);
				if(DUMP_CLASSES) ASMHelper.saveDump(name, bytes);
			}
		}
		return bytes;
	}

}
