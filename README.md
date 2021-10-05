# VLDofusBotSniffer

VLDofusBotSniffer is the sniffer part of the [VLDofusBot](https://github.com/viclew1/VLDofusBot). This repository's goal
is to mimic the Dofus network and class hierarchy. Every message sent by the server to the client is, if message has
been implemented here, converted to a Java object, then stored in an EventStore.

### How to use

To use the sniffer, you must have an instance of Dofus running on your computer. Then,
call `DofusMessageReceiver.killAndStartThread()` which will kill previous sniffer thread if needed then start the
sniffer in a thread. Every successfully parsed `EventStore` from which you can retrieve the last parsed message of a
given type by using `getLastEvent(eventClass: Class<T>): T?`.