/*
 * This file is part of text-extras, licensed under the MIT License.
 *
 * Copyright (c) 2018 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.platform.bukkit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class TextAdapter0 {
  private static final List<Adapter> ADAPTERS = pickAdapters();

  private static List<Adapter> pickAdapters() {
    final ImmutableList.Builder<Adapter> adapters = ImmutableList.builder();
    if(isSpigotAdapterSupported()) {
      adapters.add(new SpigotAdapter());
    }
    adapters.add(new CraftBukkitAdapter());
    adapters.add(new LegacyAdapter());
    return adapters.build();
  }

  private static boolean isSpigotAdapterSupported() {
    try {
      Player.class.getMethod("spigot");
      return true;
    } catch(final NoSuchMethodException e) {
      return false;
    }
  }

  static void sendComponent(final Iterable<? extends CommandSender> viewers, final Component component, final boolean actionBar) {
    final List<CommandSender> list = new LinkedList<>();
    Iterables.addAll(list, viewers);
    for(final Iterator<Adapter> it = ADAPTERS.iterator(); it.hasNext() && !list.isEmpty(); ) {
      final Adapter adapter = it.next();
      if(actionBar) {
        adapter.sendActionBar(list, component);
      } else {
        adapter.sendMessage(list, component);
      }
    }
  }
}