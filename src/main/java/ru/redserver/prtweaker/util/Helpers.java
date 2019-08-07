package ru.redserver.prtweaker.util;

import codechicken.lib.packet.PacketCustom;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import ru.redserver.prtweaker.ModPRTweaker;

public final class Helpers {

	private Helpers() {
	}

	/**
	 * Проверяет возможность использования тайла игроком. Используется для закрытия GUI.
	 * @param tile
	 * @param player
	 * @return
	 */
	public static boolean isUseableByPlayer(TileEntity tile, EntityPlayer player) {
		return player.getDistanceSq(tile.xCoord + 0.5D, tile.yCoord + 0.5D, tile.zCoord + 0.5D) <= 64.0D && tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) == tile;
	}

	public static boolean isClientSide() {
		return FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}

	/**
	 * Безопасная реализация обработки пакета
	 * @param packet
	 * @param player
	 */
	public static void setChipNBTSafe(PacketCustom packet, EntityPlayerMP player) {
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
