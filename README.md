# VelocityCommandForward
Forward commands from backend to proxy!

## Features
* Players and console can execute proxy commands (sends a packet to the proxy to execute either as player or console).
* Custom command.

## Installation
Very simple, download both Velocity and Paper plugin versions and upload to their designed servers (backend/proxy).

## Notes
* Configuration only generates for backend server!

## Use case for this?
* My use case was very niche, I have a discord command that can run minecraft commands (if player is online) and
the issue was that the discord bot was running on backend server and so I decided to make a plugin to run proxy
commands, such as `/skin`.

## Command usage
Command name can be changed in the backend plugin configuration.

`/proxyexec skin set Notch`

In the examples I changed the command to `proxy`, I didn't set it as default because I was worried that some other plugin could use this command already.

![image](https://github.com/user-attachments/assets/63b707f1-e745-4bbb-a1d4-402f4eb292de)

![image](https://github.com/user-attachments/assets/3c069e64-256b-46da-8ada-6a51c4a0ea30)

Be aware that if command doesn't exist, it gives no output.

![image](https://github.com/user-attachments/assets/3836e0cf-8426-4f45-bdbe-2cec4f92feae)

## Executing in console
If you execute the command in the console, you won't get any output and instead it's going to show up in the proxy server logs.
There must be at least one online player to be able to send command packet to the proxy (that's how plugin messaging channels work between backend and proxy).

Backend server.

![image](https://github.com/user-attachments/assets/0a55af23-2319-49f0-a6d9-ec6125fdeaac)

Proxy server.

![image](https://github.com/user-attachments/assets/1d04272e-5f70-42da-8c78-61dd529de442)

## Default configuration for backend
Changing custom command requires a server restart
```yaml
# VelocityCommandForward

# Main command for forwarding commands to proxy
custom-command: proxyexec
```

## Requirements
* Velocity plugin was built with `Java 17` and Paper plugin - `Java 21`.
* Might work on lower minecraft versions, not tested, though you need to meet Java version requirements!
