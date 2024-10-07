/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.momirealms.customnameplates.common.sender;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.momirealms.customnameplates.common.plugin.NameplatesPlugin;
import net.momirealms.customnameplates.common.util.Tristate;

import java.util.Objects;
import java.util.UUID;

/**
 * Factory class to make a thread-safe sender instance
 *
 * @param <P> the plugin type
 * @param <T> the command sender type
 */
public abstract class SenderFactory<P extends NameplatesPlugin, T> implements AutoCloseable {
    private final P plugin;

    public SenderFactory(P plugin) {
        this.plugin = plugin;
    }

    protected P getPlugin() {
        return this.plugin;
    }

    protected abstract UUID getUniqueId(T sender);

    protected abstract String getName(T sender);

    public abstract Audience getAudience(T sender);

    protected abstract void sendMessage(T sender, Component message);

    protected abstract Tristate getPermissionValue(T sender, String node);

    protected abstract boolean hasPermission(T sender, String node);

    protected abstract void performCommand(T sender, String command);

    protected abstract boolean isConsole(T sender);

    protected boolean consoleHasAllPermissions() {
        return true;
    }

    public final Sender wrap(T sender) {
        Objects.requireNonNull(sender, "sender");
        return new AbstractSender<>(this.plugin, this, sender);
    }

    @Override
    public void close() {

    }
}