/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.cosmetic.pets.PetManager can not be copied and/or distributed without the express
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

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.crytec.AvarionCore;
import net.crytec.cosmetic.CosmeticManager;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PetManager implements Listener {

  private final HashMap<Player, Pet> pets = new HashMap<>();
  private final HashMap<UUID, HashMap<PetType, String>> defaultnames = Maps.newHashMap();

  private final EnumMap<PetType, PetConfig> petConfigs;

  private final CosmeticManager manager;


  private static final NamespacedKey petNamespace = new NamespacedKey(AvarionCore.getPlugin(), "pets");
  private static final NamespacedKey petTypeNamespace = new NamespacedKey(AvarionCore.getPlugin(), "petType");
  private static final NamespacedKey petNameNameSpace = new NamespacedKey(AvarionCore.getPlugin(), "petName");

  public PetManager(final CosmeticManager manager) {
    petConfigs = new EnumMap<>(PetType.class);

    this.manager = manager;
    Pet.manager = this;
    initialize();

    Bukkit.getPluginManager().registerEvents(this, AvarionCore.getPlugin());
  }


  private void initialize() {
    boolean shouldSave = false;

    for (final PetType pet : PetType.values()) {

      if (!manager.getConfig().isSet("particles." + pet.toString())) {
        manager.getConfig().set("pets." + pet.toString() + ".name", pet.toString());
        manager.getConfig().set("pets." + pet.toString() + ".description", Arrays.asList("&7Pet Beschreibung", "&7Kann in der config verändert werden."));
        manager.getConfig().set("pets." + pet.toString() + ".permission", "pet." + pet.toString().toLowerCase());
        shouldSave = true;
      }
    }
    if (shouldSave) {
      manager.saveConfig();
    }

    for (final PetType pet : PetType.values()) {

      final PetConfig pconfig = new PetConfig(pet);

      final String displayname = ChatColor.translateAlternateColorCodes('&', manager.getConfig().getString("pets." + pet.toString() + ".name"));
      final List<String> description = manager.getConfig().getStringList("pets." + pet.toString() + ".description")
          .stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());

      pconfig.setDisplayname(displayname);

      pconfig.setPermission(manager.getConfig().getString("pets." + pet.toString() + ".permission"));

      pconfig.setIcon(new ItemBuilder(pet.getSpawnEggIcon()).name(displayname).lore(description).build());
      petConfigs.put(pet, pconfig);
    }

    Bukkit.getOnlinePlayers().forEach(this::loadPet);
  }

  public void shutdown() {
    Bukkit.getOnlinePlayers().forEach(cur -> {
      savePet(cur);
      despawnPet(cur);
    });
  }

  public Pet getPet(final Player owner) {
    return pets.getOrDefault(owner, null);
  }

  public boolean hasPet(final Player owner) {
    return pets.containsKey(owner);
  }

  public void deletePet(final Player owner) {
    if (getPet(owner) == null) {
      return;
    }

    owner.getPersistentDataContainer().remove(petNamespace);
    getPet(owner).kill();
    pets.remove(owner);
    owner.sendMessage(F.main("Pets", "Dein Pet wurde gelöscht."));
  }

  public void despawnPet(final Player player) {
    if (!hasPet(player)) {
      return;
    }
    getPet(player).kill();
  }

  public void savePet(final Player player) {
    if (!hasPet(player)) {
      return;
    }

    final Pet pet = getPet(player);

    final PersistentDataContainer container = player.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();

    container.set(petTypeNamespace, PersistentDataType.STRING, pet.getType().toString());
    container.set(petNameNameSpace, PersistentDataType.STRING, pet.hasCustomName() ? pet.getName() : "!none!");

    container.set(petNamespace, PersistentDataType.TAG_CONTAINER, container);
  }

  public PetConfig getPetConfig(final PetType type) {
    return petConfigs.get(type);
  }

  public void loadPet(final Player player) {

    if (!player.getPersistentDataContainer().has(petNamespace, PersistentDataType.TAG_CONTAINER)) {
      return;
    }

    final PersistentDataContainer container = player.getPersistentDataContainer().get(petNamespace, PersistentDataType.TAG_CONTAINER);

    final String type = container.get(petTypeNamespace, PersistentDataType.STRING);
    final String name = container.get(petNameNameSpace, PersistentDataType.STRING);

    if (!EnumUtils.isValidEnum(PetType.class, type)) {
      Bukkit.getLogger().severe("Failed to load PetType " + type + " for player: " + player.getName());
      return;
    }

    createNewPet(player, PetType.valueOf(type));

    if (!name.equals("!none!")) {
      getPet(player).setName(name);
    }
  }

  public void createNewPet(final Player owner, final PetType type) {
    if (hasPet(owner)) {
      despawnPet(owner);
    }
    pets.put(owner, new Pet(owner, type, getPetConfig(type).getDisplayname()));
  }

  public void hidePet(final Player owner) {
    if (!hasPet(owner)) {
      return;
    }
    if (getPet(owner).isHidden()) {
      return;
    }
    getPet(owner).setHidden(true);
    owner.sendMessage(F.main("Pets", "Dein Pet wurde verborgen."));
  }

  public void showPet(final Player owner) {
    if (!hasPet(owner)) {
      return;
    }
    if (!getPet(owner).isHidden()) {
      return;
    }
    getPet(owner).setHidden(false);
    owner.sendMessage(F.main("Pets", "Dein verborgendes Pet ist wieder erschienen!"));
  }

  /*
   * Save the Pet to Database on Disconnect.
   */
  @EventHandler
  public void onDisconnect(final PlayerQuitEvent e) {
    savePet(e.getPlayer());
    despawnPet(e.getPlayer());
    pets.remove(e.getPlayer());
  }

  /*
   * Load the given Pets on Join
   */
  @EventHandler
  public void delayedJoin(final PlayerJoinEvent e) {
    if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) {
      return;
    }
    loadPet(e.getPlayer());
  }


  @EventHandler
  public void onWorldChange(final PlayerChangedWorldEvent e) {
    if (!hasPet(e.getPlayer())) {
      return;
    }

    final Player owner = e.getPlayer();

    if (getPet(owner).isHidden()) {
      return;
    }

    if (getPet(owner) == null) {
      return;
    }

    getPet(owner).respawn();

  }


  @EventHandler
  public void onOwnerTeleport(final PlayerTeleportEvent e) {
    if (!hasPet(e.getPlayer())) {
      return;
    }

    final Player owner = e.getPlayer();

    if (getPet(owner).isHidden()) {
      return;
    }
    if (getPet(owner) == null) {
      return;
    }
    getPet(owner).respawn();
  }

  @EventHandler
  public void onGameModeChange(final PlayerGameModeChangeEvent e) {
    if (e.getNewGameMode() == GameMode.SPECTATOR && hasPet(e.getPlayer())) {
      final Player owner = e.getPlayer();
      if (!getPet(owner).isHidden()) {
        getPet(owner).setHidden(true);
        owner.sendMessage(F.main("Pet", "Du bist in den Zuschauermodus gewechselt, dein Pet wurde versteckt."));
      }
    }
  }

  @EventHandler
  public void petInteract(final PlayerInteractEntityEvent e) {

    if (isPet(e.getRightClicked())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void denyBreeding(final EntityBreedEvent e) {
    if (isPet(e.getEntity())) {
      e.setCancelled(true);
    }
  }

  public boolean isPet(final Entity entity) {
    boolean ispet = false;
    for (final Pet pet : pets.values()) {
      if (pet.getEntity() == entity) {
        ispet = true;
        break;
      }
    }
    return ispet;
  }
}
