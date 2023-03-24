### [Check out the documentation](https://docs.karmadev.es/)



# KarmaAPI

KarmaAPI source code...

[Please take a look at the license before using the source code, legal actions could be performed if you break any condition](https://karmadev.es/license/)<br>
To resume, you are allowed to:

- Use the API as you want
- Read the code as you want

You are not allowed to:

- Copy the code and paste as yours
- Copy the code and modify it
- Extract the code and use it in your code without a valid license agreement
  in [our discord](https://discord.com/invite/jRFfsdxnJR)

You are allowed to if you have a license agreement with us to:

- Modify the code and request it for a change
- Extract the code and use it as yours with the correspondient credits to its author
- Modify the code to use in a non-comercial purpose project only ( if you request it for a change, you will be able to
  use it as you want )

## Maven

- Shade the API

```xml
<build>
    <finalName>MyAwesomePlugin</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>X.X.X</version>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>ml.karmaconfigs.api</pattern>
                        <shadedPattern>change.this</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <createDependencyReducedPom>false</createDependencyReducedPom>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

- Add needed dependencies

Latest stable (recommended, fewer features, but all of them
working): ![Maven Central](https://maven-badges.herokuapp.com/maven-central/ml.karmaconfigs/KarmaAPI/badge.svg?version&style=flat-square)<br>

```xml
<dependencies>
    <!-- For bukkit plugin development -->
    <dependency>
        <groupId>ml.karmaconfigs</groupId>
        <artifactId>KarmaAPI-Bukkit</artifactId>
        <version>{version}-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>

    <!-- For bungeecord plugin development -->
    <dependency>
        <groupId>ml.karmaconfigs</groupId>
        <artifactId>KarmaAPI-Bungee</artifactId>
        <version>{version}-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>

    <!-- For velocity plugin development -->
    <dependency>
        <groupId>ml.karmaconfigs</groupId>
        <artifactId>KarmaAPI-Velocity</artifactId>
        <version>{version}-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>

    <!-- For general development -->
    <dependency>
        <groupId>ml.karmaconfigs</groupId>
        <artifactId>KarmaAPI-Common</artifactId>
        <version>{version}-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>

    <!-- For multiple purposes development -->
    <dependency>
        <groupId>ml.karmaconfigs</groupId>
        <artifactId>KarmaAPI-Bundle</artifactId>
        <version>{version}-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

