---
description: Common issues that people usually report
---

# 👷 Common issues

> My plugin does not have a license

This usually happens when you download the plugin from BuiltByBit or CurseForge.\
\
Due to BuiltByBit and CurseForge restrictions, LockLogin is not able to generate the license once download, so instead, you must manually generate the license via command. This is a very easy to do task, and you only need to run <mark style="color:purple;">/locklogin install</mark> in the server console and restart the server.

> I've installed the plugin, but when I connect to a server, it won't allow me to move or do anything, plus it kicks me out because "Conenct through bungee proxy!"

As mentioned previously, LockLogin uses a license system to comunicate betwen plugin instances, if you expanded your network and downloaded a new plugin instance, the license won't be the same, so you have two options.\
\
**Manually copying the license**\
To manually copy the license, simply stop your server, and navigate to any other server (recommended bungee server) and go to <mark style="color:purple;">plugins/LockLogin/cache</mark> folder. After that, copy the file <mark style="color:purple;">license.dat</mark> and paste it in the same directory of the server in where LockLogin "is not working"\
\
**Automatize the license synchronization**\
To automatically synchronize the license, without copying any file, you must run the <mark style="color:purple;">/locklogin sync</mark>  command from BungeeCord console. This will prompt the current license synchronization key as a command, simply copy that command, and then run these commands in the server console where Locklogin "is not working"

1. /locklogin uninstall
2. /locklogin sync <mark style="color:purple;">\<synchronization key></mark>
