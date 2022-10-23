/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.particle.ParticlePack can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.particle;

import net.crytec.cosmetic.particle.effects.AngelFairy;
import net.crytec.cosmetic.particle.effects.AngelWings;
import net.crytec.cosmetic.particle.effects.ApocalypticCloud;
import net.crytec.cosmetic.particle.effects.Blackhole;
import net.crytec.cosmetic.particle.effects.Blizzard;
import net.crytec.cosmetic.particle.effects.BloodHelix;
import net.crytec.cosmetic.particle.effects.Chakra;
import net.crytec.cosmetic.particle.effects.CrushedCandyCane;
import net.crytec.cosmetic.particle.effects.DarkFairy;
import net.crytec.cosmetic.particle.effects.Deathless;
import net.crytec.cosmetic.particle.effects.Devil;
import net.crytec.cosmetic.particle.effects.EarthFairy;
import net.crytec.cosmetic.particle.effects.EmeraldTwirl;
import net.crytec.cosmetic.particle.effects.Enchant;
import net.crytec.cosmetic.particle.effects.EnderAura;
import net.crytec.cosmetic.particle.effects.EnderEye;
import net.crytec.cosmetic.particle.effects.FireRings;
import net.crytec.cosmetic.particle.effects.FlameFairy;
import net.crytec.cosmetic.particle.effects.GoldenAura;
import net.crytec.cosmetic.particle.effects.IcyFlame;
import net.crytec.cosmetic.particle.effects.Mage;
import net.crytec.cosmetic.particle.effects.MagicSpiral;
import net.crytec.cosmetic.particle.effects.Meteor;
import net.crytec.cosmetic.particle.effects.Poseidon;
import net.crytec.cosmetic.particle.effects.PremiumSignal;
import net.crytec.cosmetic.particle.effects.RainCloud;
import net.crytec.cosmetic.particle.effects.Santa;
import net.crytec.cosmetic.particle.effects.ShadowWalk;
import net.crytec.cosmetic.particle.effects.Shaman;
import net.crytec.cosmetic.particle.effects.Sorcery;
import net.crytec.cosmetic.particle.effects.Titan;
import net.crytec.cosmetic.particle.effects.YingYangNew;

public enum ParticlePack {
	
	ANGEL_WINGS(AngelWings.class),
	ANGEL_FAIRY(AngelFairy.class),
	APOCALYPTIC_CLOUD(ApocalypticCloud.class),
	BLACKHOLE(Blackhole.class),
	BLIZZARD(Blizzard.class),
	BLOOD_HELIX(BloodHelix.class),
	CRUSHED_CANDY_CANE(CrushedCandyCane.class),
	CHAKRA(Chakra.class),
	DARK_FAIRY(DarkFairy.class),
	DEAHTLESS(Deathless.class),
	DEVIL( Devil.class),
	EARTH_FAIRY(EarthFairy.class),
	ENDER_EYE(EnderEye.class),
	EMERALDTWIRL(EmeraldTwirl.class),
	ENCHANT(Enchant.class),
	ENDER_AURA(EnderAura.class),
	FIRE_RINGS(FireRings.class),
	FLAME_FAIRY(FlameFairy.class),
	GOLDEN_AURA(GoldenAura.class),
	ICYFLAME(IcyFlame.class),
	MAGE(Mage.class),
	MAGIC_SPIRAL(MagicSpiral.class),
	METEOR(Meteor.class),
	POSEIDON(Poseidon.class),
	PREMIUM(PremiumSignal.class),
	RAINCLOUD(RainCloud.class),
	SANTA(Santa.class),
	SHADOW_WALK(ShadowWalk.class),
	SHAMAN(Shaman.class),
	SORCERY(Sorcery.class),
	TITAN(Titan.class),
	YING_YANG(YingYangNew.class);

	private Class<? extends IParticleEffect> clazz;
	
	private ParticlePack(Class<? extends IParticleEffect> clazz) {
		this.clazz = clazz;
	}
	
	public Class<? extends IParticleEffect> getParticleEffectClass() {
		return this.clazz;
	}
}