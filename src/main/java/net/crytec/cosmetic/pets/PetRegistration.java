/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.PetRegistration can not be copied and/or distributed without the express
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

package net.crytec.cosmetic.pets;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_15_R1.PathfinderGoalSelector;

public class PetRegistration {

  public static Object getPrivateField(final String fieldname, final Class clazz, final Object objekt) {

    final Field field;
    Object o = null;

    try {
      field = clazz.getDeclaredField(fieldname);
      field.setAccessible(true);
      o = field.get(objekt);
    } catch (final NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return o;
  }

  public static void clearGoals(final PathfinderGoalSelector goalSelector, final PathfinderGoalSelector targetSelector) {
    final Map<?, ?> goalC = (Map<?, ?>) PetRegistration.getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
    goalC.clear();
    final Set<?> targetB = (Set<?>) PetRegistration.getPrivateField("d", PathfinderGoalSelector.class, targetSelector);
    targetB.clear();
    final Set<?> targetC = (Set<?>) PetRegistration.getPrivateField("f", PathfinderGoalSelector.class, targetSelector);
    targetC.clear();
  }

}
