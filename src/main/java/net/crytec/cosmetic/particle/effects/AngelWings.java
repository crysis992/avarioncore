/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.effects.AngelWings can not be copied and/or distributed without the express
 *  permission of crysis992
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of AvarionCraft.de and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AvarionCraft.de
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AvarionCraft.de.
 *
 */

package net.crytec.cosmetic.particle.effects;

import net.crytec.cosmetic.particle.IParticleEffect;
import net.crytec.libs.commons.utils.UtilMath;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AngelWings extends IParticleEffect {

	
	short x = 1;
	short o = 0;
	
	private final short[][] shape = {
						{o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
						{o, x, x, x, x, o, o, o, o, o, o, o, x, x, x, x, o, o},
						{o, o, x, x, x, x, x, o, o, o, x, x, x, x, x, o, o, o},
						{o, o, o, x, x, x, x, x, x, x, x, x, x, x, o, o, o, o},
						{o, o, o, o, x, x, x, x, x, x, x, x, x, o, o, o, o, o},
						{o, o, o, o, x, x, x, x, o, x, x, x, x, o, o, o, o, o},
						{o, o, o, o, o, x, x, x, o, x, x, x, o, o, o, o, o, o},
						{o, o, o, o, o, x, x, o, o, o, x, x, o, o, o, o, o, o},
						{o, o, o, o, x, x, o, o, o, o, o, x, x, o, o, o, o, o}
				};
	
	
	public AngelWings(final Player player) {
		super(player);
	}

	@Override
	public void onUpdate(final boolean isMoving) {
		
		final Location location = getPlayer().getLocation().clone();
		
		if (isMoving) {
			Particle.REDSTONE.builder().location(location).extra(0).count(4).offset(0.2, 0.2, 0.2).data(new DustOptions(Color.WHITE, 0.85F)).receivers(60).spawn();
			return;
		}
		
		final double space = 0.2;
		final double defX = location.getX() - (space * shape[0].length / 2) + space;
		double x = defX;
		double y = location.clone().getY() + 2;
		double angle = -((location.getYaw() + 180) / 60);
		angle += (location.getYaw() < -180 ? 3.25 : 2.985);

		for (final short[] aShape : shape) {
			for (final short color : aShape) {
				if (color > 0) {

					final Location target = location.clone();
					target.setX(x);
					target.setY(y);

					Vector v = target.toVector().subtract(location.toVector());
					final Vector v2 = getBackVector(location);
					
					v = UtilMath.rotateAroundAxisY(v, angle);
					v2.setY(0).multiply(-0.2);

					location.add(v);
					location.add(v2);
					Particle.REDSTONE.builder().location(location).extra(0).data(new DustOptions(getColorOf(color), 1.35F)).receivers(60).spawn();
					
					location.subtract(v2);
					location.subtract(v);
				}
				x += space;
			}
			y -= space;
			x = defX;
		}
	}

	public Color getColorOf(final short id) {
		
		switch (id) {
			case 1: return Color.WHITE;
			case 2: return Color.RED;
			case 3:	return Color.BLUE;
		default: return Color.WHITE;
		}
	}
	
	public static Vector getBackVector(final Location loc) {
		final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90))));
		final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90))));
		return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
	}

}