/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.addons.armorstandeditor.ArmorStandData can not be copied and/or distributed without the express
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

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import lombok.Getter;
import lombok.Setter;
import net.crytec.addons.armorstandeditor.data.ArmorStandAxis;
import net.crytec.addons.armorstandeditor.data.ArmorStandEditMode;
import net.crytec.addons.armorstandeditor.data.ArmorStandOptions;

public class ArmorStandData {

	public ArmorStandData(Player player, ArmorStand entity) {
		this.entity = entity;
		this.player = player;
	}
	
	@Getter
	private final Player player;
	@Getter
	private final ArmorStand entity;
	@Setter @Getter
	private ArmorStandAxis axis = ArmorStandAxis.X;
	@Setter @Getter
	private ArmorStandEditMode mode = ArmorStandEditMode.ENTITY;
	
	private final static int fine = 120;
	private final static int coarse = 12;
	private final static double coarseMov = 1;
	private final static double fineMov = .03125;
	private final static double coarseAdj = ArmorStandUtil.FULLCIRCLE / coarse;
	private final static double fineAdj = ArmorStandUtil.FULLCIRCLE / fine;
	
	private double getMoveChange() {
		return player.isSneaking() ? coarseMov : fineMov;
	}
	
	private double getEulerAngleChange() {
		return player.isSneaking() ? coarseAdj : fineAdj;
	}
	
	private double getDegreeAngleChange() {
		return getEulerAngleChange() / Math.PI * 180;
	}
	
	public void toggleOption(ArmorStandOptions option) {
		switch(option) {
			case ARMS :
				this.entity.setArms(!this.entity.hasArms());
				break;
			case BASEPLATE :
				this.entity.setBasePlate(!this.entity.hasBasePlate());
				break;
			case GRAVITY :
				this.entity.setGravity(!this.entity.hasGravity());
				break;
			case SIZE :
				this.entity.setSmall(!this.entity.isSmall());
				break;
			case VISIBILLITY :
				this.entity.setVisible(!this.entity.isVisible());
				break;
			default :
				break;
		}
	}
	
	public void editPosition(boolean add) {
		if (add) {
			this.addPosition();
		} else {
			this.subPosition();
		}
	}
	
	private void subPosition() {
		switch (mode) {
			case BODY :
				this.entity.setBodyPose(subEulerAngle(this.entity.getBodyPose()));
				break;
			case ENTITY :
				this.reverseMove(this.entity);
				break;
			case ROTATE :
				this.reverseRotate(this.entity);
				break;
			case HEAD :
				this.entity.setHeadPose(subEulerAngle(this.entity.getHeadPose()));
				break;
			case LEFT_ARM :
				this.entity.setLeftArmPose(subEulerAngle(this.entity.getLeftArmPose()));
				break;
			case LEFT_LEG :
				this.entity.setLeftLegPose(subEulerAngle(this.entity.getLeftLegPose()));
				break;
			case NOTHING :
				break;
			case RIGHT_ARM :
				this.entity.setRightArmPose(subEulerAngle(this.entity.getRightArmPose()));
				break;
			case RIGHT_LEG :
				this.entity.setRightLegPose(subEulerAngle(this.entity.getRightLegPose()));
				break;
			default :
				break;
			
		}
	}
	
	private void addPosition() {
		switch (mode) {
			case BODY :
				this.entity.setBodyPose(addEulerAngle(this.entity.getBodyPose()));
				break;
			case ENTITY :
				this.move(this.entity);
				break;
			case ROTATE :
				this.rotate(this.entity);
				break;
			case HEAD :
				this.entity.setHeadPose(addEulerAngle(this.entity.getHeadPose()));
				break;
			case LEFT_ARM :
				this.entity.setLeftArmPose(addEulerAngle(this.entity.getLeftArmPose()));
				break;
			case LEFT_LEG :
				this.entity.setLeftLegPose(addEulerAngle(this.entity.getLeftLegPose()));
				break;
			case NOTHING :
				break;
			case RIGHT_ARM :
				this.entity.setRightArmPose(addEulerAngle(this.entity.getRightArmPose()));
				break;
			case RIGHT_LEG :
				this.entity.setRightLegPose(addEulerAngle(this.entity.getRightLegPose()));
				break;
			default :
				break;
			
		}
	}
	
	
	private EulerAngle addEulerAngle(EulerAngle angle) {
		switch (axis) {
			case X :
				angle = angle.setX(ArmorStandUtil.addAngle(angle.getX(), this.getEulerAngleChange()));
				break;
			case Y :
				angle = angle.setY(ArmorStandUtil.addAngle(angle.getY(), this.getEulerAngleChange()));
				break;
			case Z :
				angle = angle.setZ(ArmorStandUtil.addAngle(angle.getZ(), this.getEulerAngleChange()));
				break;
			default :
				break;
		}
		return angle;
	}

	private EulerAngle subEulerAngle(EulerAngle angle) {
		switch (axis) {
			case X :
				angle = angle.setX(ArmorStandUtil.subAngle(angle.getX(), this.getEulerAngleChange()));
				break;
			case Y :
				angle = angle.setY(ArmorStandUtil.subAngle(angle.getY(), this.getEulerAngleChange()));
				break;
			case Z :
				angle = angle.setZ(ArmorStandUtil.subAngle(angle.getZ(), this.getEulerAngleChange()));
				break;
			default :
				break;
		}
		return angle;
	}
	
	private void move(ArmorStand armorStand) {
		Location loc = armorStand.getLocation();
		switch (axis) {
			case X :
				loc.add(getMoveChange(), 0, 0);
				break;
			case Y :
				loc.add(0, getMoveChange(), 0);
				break;
			case Z :
				loc.add(0, 0, getMoveChange());
				break;
		}
		armorStand.teleport(loc);
	}

	private void reverseMove(ArmorStand armorStand) {
		Location loc = armorStand.getLocation();
		switch (axis) {
			case X :
				loc.subtract(getMoveChange(), 0, 0);
				break;
			case Y :
				loc.subtract(0, getMoveChange(), 0);
				break;
			case Z :
				loc.subtract(0, 0, getMoveChange());
				break;
		}
		armorStand.teleport(loc);
	}

	private void rotate(ArmorStand armorStand) {
		Location loc = armorStand.getLocation();
		float yaw = loc.getYaw();
		loc.setYaw((yaw + 180 + (float) this.getDegreeAngleChange()) % 360 - 180);
		armorStand.teleport(loc);
	}

	private void reverseRotate(ArmorStand armorStand) {
		Location loc = armorStand.getLocation();
		float yaw = loc.getYaw();
		loc.setYaw((yaw + 180 - (float) this.getDegreeAngleChange()) % 360 - 180);
		armorStand.teleport(loc);
	}
	
	public void resetPosition(ArmorStand armorStand) {
		armorStand.setHeadPose(new EulerAngle(0,0,0));
		armorStand.setBodyPose(new EulerAngle(0,0,0));
		armorStand.setLeftArmPose(new EulerAngle(0,0,0));
		armorStand.setRightArmPose(new EulerAngle(0,0,0));
		armorStand.setLeftLegPose(new EulerAngle(0,0,0));
		armorStand.setRightLegPose(new EulerAngle(0,0,0));
	}
}