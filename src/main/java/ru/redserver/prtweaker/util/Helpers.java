package ru.redserver.prtweaker.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

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

}
