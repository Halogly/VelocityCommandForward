# VelocityCommandForward
Forward commands from backend to proxy!

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

## Default configuration for backend
Changing custom command requires a server restart
```yaml
# VelocityCommandForward

# Main command for forwarding commands to proxy
custom-command: proxyexec
```

## Requirements
* Java 21.
* This was built with Velocity `3.4.0-SNAPSHOT` and `Paper 1.21.5-R0.1-SNAPSHOT`.
  * If you are running lower versions, check Velocity/PaperMC documentations if things I used in the code are supported for those specific versions.
