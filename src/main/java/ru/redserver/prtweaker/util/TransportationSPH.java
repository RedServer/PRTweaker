package ru.redserver.prtweaker.util;

import codechicken.lib.packet.PacketCustom;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.redserver.prtweaker.ModPRTweaker;

/**
 * Исправленный обработчик пакетов
 * @author TheAndrey
 */
public final class TransportationSPH {

	/**
	 * Безопасная реализация обработки пакета
	 * @param packet
	 * @param player
	 */
	public static void setChipNBT(PacketCustom packet, EntityPlayerMP player) {
		int slot = packet.readUByte();
		ItemStack stack = packet.readItemStack();

		if(slot >= 0 && slot < player.inventory.getSizeInventory() && stack != null) {
			ItemStack current = player.inventory.getStackInSlot(slot);
			if(current != null && current.isItemEqual(stack) && current.getItem().equals(ModPRTweaker.itemRoutingChip)) {

				// Безопасный перенос NBT
				if(stack.stackTagCompound == null) {
					current.stackTagCompound = null;
				} else {
					if(current.stackTagCompound == null) current.stackTagCompound = new NBTTagCompound();
					NBTTagCompound tag = stack.stackTagCompound.getCompoundTag("chipROM");
					if(tag != null) current.stackTagCompound.setTag("chipROM", tag);
				}
				player.inventory.markDirty();

			} else {

				// Оповещение о попытке взлома
				LogHelper.warn(String.format("Player '%s' trying to give item '%s' using packet.", player.getCommandSenderName(), stack.toString()));
				player.playerNetServerHandler.kickPlayerFromServer("Извини дружище, эту лазейку прикрыли D:");
			}
		}
	}

}
