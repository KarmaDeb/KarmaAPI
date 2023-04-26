---
description: >-
  As LockLogin was built with BungeeCord in mind, having multiple BungeeCord
  instances is not a problem for LockLogin
---

# 🎛 Multiple bungee

{% hint style="danger" %}
PLEASE NOTE. IF YOU ALREADY DOWNLOADED THE PLUGIN PREVIOUSLY, AND YOU ARE JUST EXPANDING YOUR NETWORK, WE URGE YOU TO SIMPLY COPY THE EXISTING RUNNING LOCKLOGIN INSTANCE AND PASTE IN THE NEW SERVER, AS LONG AS THE CONTENTS INSIDE OF <mark style="color:purple;">plugins/LockLogin/cache</mark> WHICH INCLUDES THE LICENSE
{% endhint %}

To install LockLogin correctly without any errors, please follow these steps.

{% hint style="info" %}
IN ORDER FOR LOCKLOGIN TO WORK PROPERLY, PLEASE FOLLOW THESE STEPS IN EACH OF YOUR NETWORK SERVERS (except step 1 and 2).\
\
PLEASE NOTE; <mark style="color:green;">LOCKLOGIN SUGGESTS SQL</mark> TO WORK IN MULTI BUNGEE ENVIRONMENTS
{% endhint %}

1. Download the [KarmaAPI platform](https://www.spigotmc.org/resources/karmaapi-platform.98542/)
2. Download [LockLogin](https://www.spigotmc.org/resources/rd-locklogin-the-best-authentication-plugin-%E2%9C%85-fast%E2%9A%A1-reliable%E2%AD%90-secure%E2%AD%95.75156/)
3. Install the KarmaAPI platform
4. Install LockLogin
5. Start your servers and proxy

{% hint style="info" %}
Those are optional steps, it's ok if you don't follow them, but we urge you to follow them if possible.\
\
YOU WILL NEED A SQL SERVER FOR THOSE STEPS
{% endhint %}

1. Stop all your proxies (you can leave spigot servers running, just shutdown bungeecord servers)
2. Download [LockLoginSQL](https://karmadev.es/locklogin/modules/) module
3. Install LockLoginSQL module in all the proxies (only bungeecord servers required). See [How to install modules](../modules/installing.md)
4. Start your proxies, and after they've been fully started, shutdown them again
5. Navigate to <mark style="color:purple;">plugins/LockLogin/plugin/modules/LockLoginSQL/config.yml</mark>
6. Configure the SQL connection
7. Start your proxies

After these steps, LockLogin should be completely working. LockLogin is compatible from 1.7.X to the latest minecraft version

## Example configuration of LockLoginSQL module

````yaml
```yaml
#######################
#                     #
#    []      []       #
#    []      []       #
#    []      []       #
#    []      []       #
#    [][][]  [][][]   #
#                     #
#######################

#LockLogin storage driver to use
#
# MongoDB: An alternative for MySQL
#
######################################
#
# JSON: The same as KarmaFile but with the known format json.
#
######################################
#
# MySQL: Uses the MySQL connection to store players account into
#        a sql server
#
######################################
#
# SQLite: Uses a sql connection to store players account in a local
#         sqlite file
#
######################################
#
# File: The default, store players account separately in files
#       using separated KarmaFile
#
######################################
Driver: MySQL

#The time in minutes the user accounts cache will be stored in the server
#while the client is not online
CacheTimes: 5

#Azuriom configuration
# Private: Allow only azuriom registered users
#
# AdminOnly: Works only with azuriom private. Requires the
#            user to be admin rank in azuriom
#
# MinimalPower: Works only with azuriom private. Requires the
#               user rank to have at least the specified amount
#               of power to join the server
#
# SyncPrefix: WORK IN PROGRESS; Tries to make Vault or LuckPerms ( if found )
#             change the current user role prefix to the Azuriom rank name and
#             color
Azuriom:
  Private: false
  AdminOnly: false
  MinimalPower: 0
  SyncPrefix: false

#Require users to be registered at your WordPress forum
PrivateWordPress: false

#Require user group to be admin group to join the server
AzuriomOnlyAdmin: false

#Connection details for SQLite or MySQL driver. File driver
#won't use any of this options
#
# Host: It's the MySQL host name, for example:
#       192.168.1.5 or sql.domain.com. In case of
#       SQLite, this will be used as the file name,
#       example: <host>.sqlite.
#       In case of MongoDB, the MongoDB URI must be provided
#
#
# Port: Only used by MySQL, it's the port where your SQL server
#       is open to. By default, is 3306
#
# User: Only used by MySQL, is the account name that should have
#       access to the database
#
# Password: Only used by MySQL, is the password of the account that
#           should have access to the database
#
# Database: Is the database of the SQL server or, in case of SQLite,
#           the name that the database will internally receive
#
# Table: Is the table name ( inside of database ) in where the accounts
#        and 2fa data will be stored. This applies for MySQL, SQLITE and MongoDB
#
# Azuriom: Extra azuriom tables. For example, bans to retrieve user bans and ban reasons
Connection:
  Host: "sqlserveraddress.net"
  Port: 3306
  User: "databaseuser"
  Password: "databasepassword"
  Database: "serveraccounts"
  Table: "ll_accounts"

#Connection limitations and extra configurations for MySQL driver
#and/or SQLite
#
# MinConnections: The amount of connections to retain in the
#                 sql server to avoid connectivity errors
#
# MaxConnections: The amount of maximum connections to retain
#                 in the sql server to avoid database overflow
#
# ConnectionLifeTime: The maximum time ( in seconds ) each connection will
#                     be alive while idle
#
# ListenSSL: This will make the connection run encrypted, which means
#            passwords and other info will be also encrypted when communicating
#            with the database.
#
# RespectCertificates: If the server has a valid certificate, enable this, otherwise if
#                      your sql server doesn't have a valid certificate set up, turn this
#                      to false
Limitations:
  MinConnections: 3
  MaxConnections: 10
  ConnectionLifeTime: 300
  ListenSSL: true
  RespectCertificates: true

#List of dependencies to ignore
#when verifying dependency drivers
#
# HikariCP
# SLF4J
# CommonsIO
# JsonSimple
#
# Leave empty to disable:
#
# DriverExceptions: []
DriverExceptions:
  - "HikariCP"
```
````
