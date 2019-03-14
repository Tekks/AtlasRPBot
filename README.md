# AtlasRPBot

## Infos

This bot serves as a neutralizer of texts from players in an RP environment. 
It posts anonymously texts which are read in advance by moderators.

## Functions

### BottlePost

```
!flaschenpost <Text>
```

The BottlePost command partially encodes a message. This simulates the moisture level inside a bottlepost. Moderators read this message before it appears in the actual channel.

### Gossip

```
!gerücht <Text>
```

The Gossip command is simultaneous to the bottle post command. Here, however, no characters are encrypted. Moderators read this message before it appears in the actual channel.

### Configs

```
!config
```

Displays all configurable values

```
!setconfig <Key>---<Value>
```

Provides the ability to change bot parameters at runtime. This includes the prefix or the channel ids.

## Installation

Just clone the repositories and change the discord Token in the config file.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is  licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
