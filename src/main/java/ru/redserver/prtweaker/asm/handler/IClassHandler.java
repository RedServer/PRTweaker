package ru.redserver.prtweaker.asm.handler;

import org.objectweb.asm.tree.ClassNode;

public interface IClassHandler {

	/**
	 * Проверяет, подходит ли класс (чтобы не парсить ненужные)
	 * @param name Имя класса
	 * @return Результат
	 */
	public boolean accept(String name);

	/**
	 * Выполняет процессинг класса
	 * @param node
	 * @return Был ли класс изменён
	 */
	public boolean transform(ClassNode node);

}
