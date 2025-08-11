# VelocityCommandForward

[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/plugin/velocitycommandforward) [![hangar](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/hangar_vector.svg)](https://hangar.papermc.io/ItsTauTvyDas/VelocityCommandForward)

![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/velocitycommandforward?style=for-the-badge)

**Forward commands from backend to proxy!**

## ✨ Features
* Custom command.
* Custom message.
* Customize commands to be sent silently (without sending messages or console logs)
* Players and console can execute proxy commands (sends a packet to the proxy to execute either as player or console).

## 📥 How to install? / Installation / Setup
Very simple, download both Velocity and Paper plugin and upload to their designed servers (proxy/backend).

1. Turn off your proxy (Velocity) and backend (Paper) server.
2. Download Velocity and Paper version plugin.
3. Drop both plugin files (for your server version) into the `plugins` folder.
4. Start your server.

## 💡 Notes
* Configuration only generates for backend server!

## ❓ Use case for this?
* My use case was very niche, I have a discord command that can run minecraft commands (if player is online) and
the issue was that the discord bot was running on backend server and so I decided to make a plugin to run proxy
commands, such as `/skin`.
* Another common use case is when you install the TAB plugin on Velocity and need to send commands from the backend server to the proxy server, such as `/btab scoreboard` to toggle TAB's scoreboard. While executing it manually works fine, if you use custom items from other plugins that are bound to this command, the command may fail to execute. In such cases, simply relying on this plugin's commands can resolve the issue of TAB commands not working.

## 🕹️ Command usage
Command name can be changed in the backend plugin configuration:

```yaml
custom-command: proxyexec
```

- `/proxyexec reload`

Used to reload plugin config. Changing `custom-command` requires a server restart.

- `/proxyexec <the command you want to send>`

In the examples I changed the command to `proxy`, I didn't set it as default because I was worried that some other plugin could use this command already.

![image](https://github.com/user-attachments/assets/63b707f1-e745-4bbb-a1d4-402f4eb292de)

## Executing in console
If you execute the command in the console, you won't get any output and instead it's going to show up in the proxy server logs.
There must be at least one online player to be able to send command packet to the proxy (that's how plugin messaging channels work between backend and proxy).

Backend server.

![image](https://github.com/user-attachments/assets/0a55af23-2319-49f0-a6d9-ec6125fdeaac)

Proxy server.

![image](https://github.com/user-attachments/assets/1d04272e-5f70-42da-8c78-61dd529de442)

## 📄 Default configuration
Changing `custom-command` requires a server restart.
```yaml
# VelocityCommandForward

# Command for forwarding commands to proxy.
# You need to restart the server for the changes to apply.
custom-command: proxyexec

# Here you can specify root commands that will not send log and message.
filtered-commands:
  - example_command

# {command} = The command sent by the sender
# {sender} = The player who sent the command
# If set to '', no message will be sent.
messages:
  reload: '&aPlugin messages have been reloaded!'
  command-sent-as-player: '&2Command sent to proxy &7=> &a/{command}'
  command-sent-as-console: '&2Command sent as console to proxy &7=> &a/{command}'
  no-online-player: '&cThere must be at least 1 online player to be able to execute proxy console commands!'
  console-log: '[{sender}] Sending command packet to proxy => /{command}'
  velocity-log: '[{sender}] Received proxy command packet => /{command}'
```

## ✅ Requirements
* Velocity plugin was built with `Java 17` and Paper plugin is `Java 21`.
* Compatible with versions 1.20.6 and above. Since the plugin uses Brigadier (added in 1.20.6) to build commands, servers running lower versions will encounter errors.
