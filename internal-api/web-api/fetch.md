---
description: >-
  The fetch method will just retrieve the user data, without trying to update
  his information, for example a username change.
---

# 📥 Fetch

You can use the fetch to fetch a bunch of data, or to fetch a single user data. Here are some examples with their response.

{% hint style="danger" %}
Please we aware of a responsible the API usage. It has a rate limit and request delayer. It will start to delay requests by 500 ms (0,5 seconds) for every 25 requests in less than 15 minutes. And will block further requests after 100 requests in less than 15 minutes.
{% endhint %}

## Fetch single (known) data

> https://karmadev.es/api/fetch/KarmaDev

```json
{
  "id": 545,
  "name": "KarmaDev",
  "created": "2018-10-07", //When the account was created
  "offline": [
    {
      "data": {
        "id": "00501053-c277-3f7c-aa82-e433eaa617b2",
        "short": "00501053c2773f7caa82e433eaa617b2"
      }
    }
  ],
  "online": [
    {
      "data": {
        "id": "79303af9-01bc-4968-878e-735f916dd7b4",
        "short": "79303af901bc4968878e735f916dd7b4"
      }
    }
  ],
  "properties": [
    {
      "name": "skin",
      "url": "http://textures.minecraft.net/texture/c8d09eba561765c7c7545e1d327fe2380ee9a73a2a8529662c1659a4966e8ed6",
      "data": "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAEB0lEQVR4XuWYPVIbQRCFSV12ziV8CB/BFyAhICIgIiAggowLEPgwduYDUOUTkNghyOGaptyq3k/TM92r2ZUEr+oVO9M/815rtZI4OmpguL8ftiH7TcVcfZvgwVmy38GBhrJkvyxqPWqxbqChLNkvi1qPWqwbaChL9sui1qMW6wYaypL9sqj1qMW6gYayZL+DAw1lyX5ZsBfXW4OCydXV1Zp2//ft7UbuFHo6Intay33GqnEGyOPj4+HP4+N6AHIte8Pd3UbuFHo61gKD0Dp9Ybz4Rn8GNihGX7i+A/6vN/Im0tOxFhhEq96N60brluZboBeNxt2AgjyWngM9SD2Lg4I8ri4vh+fz8+Hvzc3wfHGxEZ+DVqcMX859Pjsbnk5Pqw/AFu3dXizm20FMf/r4Yfj18OOVci0iWNebalAGbw0L7BBYl2GoWAbw+haQu+DlVVhdX3cbgDVVgwxB+XRyEq5L4/Lhy2BpxcqdwXyCBkn2J9mPYL+orjAoKHsQxZHsT7IfwX5RXWFQUPYgiiPZn2Q/gv2iusKgoOxBFEeyP8l+BPtFdbmggCwpZl9Iny7ExLevP1+p1zTpUXJ5sJAfoy3OkU+fLtS8JY16tAOoiSrFSns9SZ8u1EjpDjj9/rnI0gD2jfTpgq+qJY1zAEIeXGPPV116lahx+nRB03MOIEoOimvdK1Hj9OmCpvdhABHS+OQBsHHmkF2SmqiNPl2wceYQIe+a3qSmqDb6dMHGmUOE3scmP1pbH7PMUVJTVBt9umDjzCHClqHSNXNLOXpNTVFt9OmCjTOHCGmkRGsowkUHEMEsTQ8FnDrjbx7vfgCCd/0WWAJ8wC0+cH3qcn8J6FuL5hcbghrXIWQGkcn1QMMk82dBDyNTQcMk8wXe/sGCpq157tUGc7CgOX0mlGLdB8Df+lmyX2/QePcB7As8g3Jd+pSwtVuDTbnWHxqlOO+I3qRxausCNuW6NIDa12L5FSd/7UfsOGOMWj6NU1sXsCnXpQHUROhPWr2Wv7Uh1PJpvHX2JLAp19kBCPj7nnEik986uzsihhXWiJAPMOYTNne2f3BkkDXQYwBa824HoNyLAQgyIt7kAGqg4Syt4QypY2egoSxpLErq2BloKEsai5I6ZkHkHyT86ipUc1pP08raV90WqcOiFW+iZLq0J6B5oZq2AygNQnL1S5YlzZKSQx0Wdkis1b1xRQHWsGdeQPM6gBKjA6gNQePUYUGz1QFQfA/SqEfJpfGaeTuEtYECbA5rdW9csQUoPmLAGimReWRkAEJ7zb1RgR6sa5usay9O8REDStZF6xcZANe2yK4pPmJAyTqvnntTBqA9dG9UoAfr2jZorSmeYmtkXbR+ygC4Nyp4iwPgnod/BdJxChT3wt4AAAAASUVORK5CYII=",
      "value": "ewogICJ0aW1lc3RhbXAiIDogMTY3OTQzODkwOTAxOCwKICAicHJvZmlsZUlkIiA6ICI3OTMwM2FmOTAxYmM0OTY4ODc4ZTczNWY5MTZkZDdiNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJLYXJtYURldiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jOGQwOWViYTU2MTc2NWM3Yzc1NDVlMWQzMjdmZTIzODBlZTlhNzNhMmE4NTI5NjYyYzE2NTlhNDk2NmU4ZWQ2IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=",
      "signature": "C0DSs/4mSgnAeLZ+DopLt2TD35vryXS/Zl0kTqT0zzn4sAlQiPV/ND0ke59C7I+evTPVmuHMBd4pUuBXR6Alei5z72fLWS9u6pQ6wEJLxSKwMbt1whw2aIm2M7TjRYhWqvrf3l83JS/0EEmxXz/SoSLJzyYPhfL/Zh2v6NRNUw8uthB3lm3tD8ml2QfiuFs6I38xgOoe97UpT10qndWit3DyKE2GF2ZCZ1i2V8FMjKG+348JHa7oyms6MbtAdT1fjNCPYr3VioaGNSVncuhdKhvub3sUJJaq14m/ZTD/stxJS8n4XK3V8M0kQFhUnuCprLE+lS/N289/j/sF4wKjKgT9SfCsBIv+mTn9/jqCpaHIena7efNQ9OFp4gTlPjHZasT4x6uvX/aDLxjsyccvy6QPfyFR2rfGy8Sh0l09PKH7Mw33ndw7QG84LIGFbx8y9J9eBVhzghCh9k/CFN6DvK2WaSwJCEDuNGA2IvAm5iHeTel+uQbWYKq40BO4wyjwp5HCTI7RADtMNosNGZcuJcxIQht3BiJa0Axbks+Kd/vksRSD0kysRRLFl/VWQE/kZ8n3cAvQh5BHkcM1fxlH+/CFLm/R6zmu7ylTrlsfN0rgJ7GnhKS9GjH2pa/sk33aJsYOMMtF0hU57fr9xKjFWiL0jefao6/IS/X6Fk+eYU4="
    },
    {
      "name": "cape",
      "url": "http://textures.minecraft.net/texture/c8d09eba561765c7c7545e1d327fe2380ee9a73a2a8529662c1659a4966e8ed6",
      "data": "iVBORw0KGgoAAAANSUhEUgAAAEAAAAAgCAYAAACinX6EAAACiElEQVR4XuWWT2oVQRjE3xGCCDG4MFF0EzSIGFBBEZTss8km4M6NJ/AE7gR3HsO1kHU2LvUIHsATjKlmaqjU149umh5BevHj9avpmf6q+ps/m1enPzZnr3cmsH9wmIXHP9y4NX3Zuz99v3OUBcfmOZtfD55V8eTp2+nw4Yt0/tX/HOEcME1TFzZq0o3XHNuGF7wNBuB6CTfSyhLAp/e7qRCAolgYwDE3WMILLsF18fvm5Dyha/t8N9JK2gEWffF5PwQAjcd1bgkvuASvr0DT8FcLgIuh1dUwxmx/naegwFyHuMEaNHyupfXoreJGWkmL0sjHm7cTP789TvA/DXKumsdDD3gIbq4GnKfdB1DHqgEwZRj9und3MU2o6U64+VwIbq4GmsyZXy0ANU/4WlONIbDl3TxgtwA3V0MuAO0+0jUAFK1m9X3sOubSKMceAHU3VwON4tdvQT6guwegOw4u7x1fC0DBXDeaCwC4uRrY6t6VvCZ00D0AmNYAfj86SriuxeCrz0OARt3N1cCdVvPalRquG2llCYBmsRADwJjmNQAa5acvd1/1+VrBZAma59qsiVBzI61cC4DtzwDYBdAYkJrkbrt5MBccDFYQnkVO1wB0h9n+f14+T7ALAMb/IgCuo3B9rceNtJIC4IURAM0TLoixB0DzPQPgmtoBuVvBjbSSAuCiLQFsewbM5wWDJTQArWO1DsD9VhsA5qpJfw12CmAJget6ENDdSCtB6MVsIBgsoQFoEKt1gAu9mAsNBksggOnd+RIAxmSYDrgyu4SgATCEIQLA+blb4L/pgPm1FQyWkA5YQvDd7/oh5MJoBGE0gjAaQRiNIIxGEEYjCKMRhNEIwmgEYTT+AksiJ9gdoefNAAAAAElFTkSuQmCC"
    }
  ]
}
```

### Fetch single (unknown) data

> https://karmadev.es/api/fetch/SomeUnknownUser

```json
{
  "id": -1, //The user internal ID
  "name": "SomeUnknownUser",
  "created": "unknown",
  "offline": [
    {
      "data": {
        "id": "c5e7d036-2581-3a81-a3fe-a273722c9b73",
        "short": "c5e7d03625813a81a3fea273722c9b73"
      }
    }
  ],
  "online": [
    {
      "data": {
        "id": "unknown",
        "short": "unknown"
      }
    }
  ],
  "properties": [
    {
      "name": "skin"
    },
    {
      "name": "cape"
    }
  ]
}
```

### Fetch multiple data

> https://karmadev.es/api/fetch/@all?page=1

```json
{
  "stored": 1341, //The amount of stored data
  "page": 1, //The current page
  "pages": 135, //The maximum number of pages (could be actually +1)
  "fetched": {
    "770": {
      "id": 16,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "0fa930a9-71f4-3bd9-9cfb-9695a3647abc",
            "short": "0fa930a971f43bd99cfb9695a3647abc"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "0f438102-b98f-47af-aece-2c692de1d1bc",
            "short": "0f438102b98f47afaece2c692de1d1bc"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "1234": {
      "id": 4,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "21f46faa-a97e-3482-ba4c-824963b1b2fc",
            "short": "21f46faaa97e3482ba4c824963b1b2fc"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "6fe1ff97-f7cf-4ce0-ac61-ec74f1a34ba9",
            "short": "6fe1ff97f7cf4ce0ac61ec74f1a34ba9"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "12345": {
      "id": 5,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "704c82d6-e63f-3dce-b5a9-116eeff85494",
            "short": "704c82d6e63f3dceb5a9116eeff85494"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "815a93d1-2337-497a-b6fc-58db5f363bd9",
            "short": "815a93d12337497ab6fc58db5f363bd9"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "00wywrotka": {
      "id": 1,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "85229497-a2c4-3175-8514-51e4f252de19",
            "short": "85229497a2c43175851451e4f252de19"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "aee9a114-ac2d-42a7-9339-e06f7590ed85",
            "short": "aee9a114ac2d42a79339e06f7590ed85"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "0Bollo0": {
      "id": 2,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "bf549017-979e-3efb-a67f-15fc50126721",
            "short": "bf549017979e3efba67f15fc50126721"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "218a3114-0118-413c-b56f-e99f74a609f3",
            "short": "218a31140118413cb56fe99f74a609f3"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "11MATPUS": {
      "id": 3,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "57505a32-0839-30ac-a1c1-36006ca77ceb",
            "short": "57505a32083930aca1c136006ca77ceb"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "cecd5dd8-16b5-4254-b37a-d94f79909d40",
            "short": "cecd5dd816b54254b37ad94f79909d40"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "1Dity": {
      "id": 6,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "e6e1fb99-453e-32dc-b1f4-1af404a08930",
            "short": "e6e1fb99453e32dcb1f41af404a08930"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "c6661ad6-11ea-4f9b-8fe8-b470d190246b",
            "short": "c6661ad611ea4f9b8fe8b470d190246b"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "1_1_1": {
      "id": 7,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "cef16399-4bac-3eed-a775-b1a942ec6571",
            "short": "cef163994bac3eeda775b1a942ec6571"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "ee868477-7e81-4582-bbb1-22dc6f8ebdf0",
            "short": "ee8684777e814582bbb122dc6f8ebdf0"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "2137v": {
      "id": 8,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "4e398cd8-b11c-3408-b0cf-6caa8d63d8e5",
            "short": "4e398cd8b11c3408b0cf6caa8d63d8e5"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "d9b63cfd-cd44-423f-853f-c0b7e4473565",
            "short": "d9b63cfdcd44423f853fc0b7e4473565"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    },
    "26er": {
      "id": 9,
      "created": "2023-03-03",
      "offline": [
        {
          "data": {
            "id": "a76774f8-1bed-3f9c-98c7-e580cf040c68",
            "short": "a76774f81bed3f9c98c7e580cf040c68"
          }
        }
      ],
      "online": [
        {
          "data": {
            "id": "1acd0475-8850-41e9-a381-e342205bc894",
            "short": "1acd0475885041e9a381e342205bc894"
          }
        }
      ],
      "properties": [
        {
          "name": "skin"
        },
        {
          "name": "cape"
        }
      ]
    }
  }
}
```

## API Implementation

This method also has an internal API implementation, which is highly recommended over creating a custom handle.

### Single fetch

```java
import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.utils.uuid.UUIDType;

public class Main {

    public static void main(String[] args) {
        MineAPI.fetch(args[0]).whenComplete((response) -> {
            System.out.printf("URL: %s%n", response.getUri()); //The request URL
            System.out.printf("ID: %d%n", response.getId()); //The user ID
            System.out.printf("Nick: %s%n", response.getNick()); //The user name
            System.out.printf("Offline: %s%n", response.getUUID(UUIDType.OFFLINE)); //The user offline mode UUID
            System.out.printf("Online: %s%n", response.getUUID(UUIDType.ONLINE)); //The user online mode UUID
            System.out.printf("Skin: %s%n", response.getSkin()); //The user skin textures
            System.out.printf("Cape: %s%n", response.getCape()); //The user cape textures
        });
    }
}
```

### Multiple fetch

```java
import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.utils.uuid.UUIDType;

public class Main {

    public static void main(String[] args) {
        long page = Long.parseLong(args[0]);
    
        long start = System.currentTimeMillis();
        MineAPI.fetchAll(page ).whenComplete((response) -> {
            long end = System.currentTimeMillis();
            long diff = (end - start);

            System.out.printf("Fetched %d accounts in %d ms%n", response.getFetched(), diff);
            response.getAll().forEach((account) -> {
                System.out.printf("URL: %s%n", account.getUri());
                System.out.printf("ID: %d%n", account.getId());
                System.out.printf("Nick: %s%n", account.getNick());
                System.out.printf("Offline: %s%n", account.getUUID(UUIDType.OFFLINE));
                System.out.printf("Online: %s%n", account.getUUID(UUIDType.ONLINE));
                System.out.printf("Skin: %s%n", account.getSkin());
                System.out.printf("Cape: %s%n", account.getCape());
                System.out.println("---------------------------------------------");
            });

            Optional<OKARequest> user = response.find("KarmaDev");
            user.ifPresent((account) -> {
                System.out.printf("URL: %s%n", account.getUri());
                System.out.printf("ID: %d%n", account.getId());
                System.out.printf("Nick: %s%n", account.getNick());
                System.out.printf("Offline: %s%n", account.getUUID(UUIDType.OFFLINE));
                System.out.printf("Online: %s%n", account.getUUID(UUIDType.ONLINE));
                System.out.printf("Skin: %s%n", account.getSkin());
                System.out.printf("Cape: %s%n", account.getCape());
                System.out.println("---------------------------------------------");
            });

            Optional<OKARequest> uuidUser = response.find(UUID.nameUUIDFromBytes(("OfflinePlayer:KarmaDev").getBytes()));
            uuidUser.ifPresent((account) -> {
                System.out.printf("URL: %s%n", account.getUri());
                System.out.printf("ID: %d%n", account.getId());
                System.out.printf("Nick: %s%n", account.getNick());
                System.out.printf("Offline: %s%n", account.getUUID(UUIDType.OFFLINE));
                System.out.printf("Online: %s%n", account.getUUID(UUIDType.ONLINE));
                System.out.printf("Skin: %s%n", account.getSkin());
                System.out.printf("Cape: %s%n", account.getCape());
                System.out.println("---------------------------------------------");
            });
        });
    }
}
```
