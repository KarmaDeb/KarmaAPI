---
description: You can also fetch heads from the web API
---

# 😐 Heads

By default, the responses of this API are in image format. But it also supports json output. To make the response be in json format, you must add `?display=json`. Allthrought that's not recommended as it's better to use the API which has wrappers and the propper implementation of the API iteraction.

{% hint style="danger" %}
Please we aware of a responsible the API usage. It has a rate limit and request delayer. It will start to delay requests by 500 ms (0,5 seconds) for every 25 requests in less than 15 minutes. And will block further requests after 100 requests in less than 15 minutes.
{% endhint %}

## Fetching a head

> https://karmadev.es/api/head/KarmaDev

The head request also supports head image sizing:

* 64
* 128
* 256
* 512
* 1024

For example

| Size | URL                                        |
| ---- | ------------------------------------------ |
| 64   | https://karmadev.es/api/head/KarmaDev/64   |
| 128  | https://karmadev.es/api/head/KarmaDev/128  |
| 256  | https://karmadev.es/api/head/KarmaDev/256  |
| 512  | https://karmadev.es/api/head/KarmaDev/512  |
| 1024 | https://karmadev.es/api/head/KarmaDev/1024 |

<figure><img src="https://karmadev.es/api/head/KarmaDev/128" alt=""><figcaption><p>https://karmadev.es/api/head/KarmaDev/128</p></figcaption></figure>

## API Implementation

As mentioned previously, the recommended way to interact with this part of the API is by using the internal API. Which allows you to not only get the image, but also export it into a file

```java
import ml.karmaconfigs.api.common.minecraft.api.MineAPI;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        String name = args[0];
        int size = MineAPI.DEFAULT;
        if (args.length == 2) {
            switch (args[1].toLowerCase()) {
                case "very_small":
                    size = MineAPI.VERY_SMALL;
                    break;
                case "small":
                    size = MineAPI.SMALL;
                    break;
                case "medium":
                    size = MineAPI.MEDIUM;
                    break;
                case "big":
                    size = MineAPI.BIG;
                    break;
                default:
                    break;
            }
        }
        
        //MineAPI.fetchHead(name).whenComplete((response) -> {
        MineAPI.fetchHead(name, size).whenComplete((response) -> {
            try {
                Path image = response.export("images", name + ".png"); //Export the image into a file "<player>.png"
                Desktop.getDesktop().open(image.toFile()); //Open the image

                System.out.printf("URL: %s%n", response.getUri()); //Show the image URL
                System.out.printf("ID: %d%n", response.getId()); //Show the head ID
                System.out.printf("Size: %d%n", response.getSize()); //Show the head size
                System.out.printf("Textures: %s%n", response.getTexture()); //Show the head textures
                System.out.printf("Head: %s%n", response.getHead()); //Show the head image base64
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
```
