/*
 * This file is part of adventure-platform, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.platform.bungeecord;

import net.kyori.adventure.platform.AudienceInfo;
import net.kyori.adventure.platform.impl.AbstractAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.text.serializer.bungeecord.BungeeCordComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.Connection;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/* package */ class BungeeSenderAudience extends AbstractAudience {

  private final CommandSender sender;
  private final ComponentRenderer<AudienceInfo> renderer;

  /* package */ BungeeSenderAudience(final @NonNull CommandSender sender, final @NonNull ComponentRenderer<AudienceInfo> renderer) {
    this.sender = requireNonNull(sender, "command sender");
    this.renderer = requireNonNull(renderer, "renderer");
  }

  protected BungeeCordComponentSerializer serializer() {
    return BungeeCordComponentSerializer.get();
  }

  protected BaseComponent[] render(final @NonNull Component component) {
    requireNonNull(component, "component");
    return serializer().serialize(renderer.render(component, this));
  }

  @Override
  public boolean hasPermission(@NonNull String permission) {
    return this.sender.hasPermission(requireNonNull(permission, "permission"));
  }

  @Override
  public boolean isConsole() {
    return !(this.sender instanceof Connection);
  }

  @Override
  public void sendMessage(final @NonNull Component message) {
    this.sender.sendMessage(this.render(message));
  }
}