/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.armorstandeditor.ArmorStandUtil can not be copied and/or distributed without the express
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

package net.crytec.addons.armorstandeditor;

public class ArmorStandUtil {
	
	public static final double FULLCIRCLE = Math.PI*2;

	public static double addAngle(double current, double angleChange) {
		current += angleChange;
		current = fixAngle(current, angleChange);
		return current;
	}

	public static double subAngle(double current, double angleChange){
		current -= angleChange;
		current = fixAngle(current, angleChange);
		return current;
	}

	//clamps angle to 0 if it exceeds 2PI rad (360 degrees), is closer to 0 than angleChange value, or is closer to 2PI rad than 2PI rad - angleChange value.
	private static double fixAngle(double angle, double angleChange){
		if(angle > FULLCIRCLE){
			return 0;
		}
		if(angle > 0 && angle < angleChange){
			if(angle < angleChange/2){
				return 0;
			}
		}
		if(angle > FULLCIRCLE-angle){
			if(angle > FULLCIRCLE - (angleChange/2)){
				return 0;
			}
		}
		return angle;
	}
}