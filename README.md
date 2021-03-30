# ListenDotMoe

ListenDotMoe is a Java API used to get song information from [listen.moe](https://listen.moe/)

## Usage

```java
package com.example;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;
import dev.JustRed23.ListenDotMoe.Utils.Logger;

import java.util.concurrent.TimeUnit;

public class Example {

    public static void main(String[] args) throws InterruptedException {
        //Create a new instance
        ListenDotMoe ldm = new ListenDotMoe();
        //Enable / disable debug logging
        ldm.enableDebug(false);
        //Enable / disable the logger
        ldm.disableLogger(false);
        //Add the song event handler
        ldm.addSongEventHandler(song -> Logger.info("Song title: " + song.getTitle()));
        //Start ListenDotMoe
        ldm.start();

        //Sleep for three seconds
        TimeUnit.SECONDS.sleep(3);
        //Stop ListenDotMoe
        ldm.stop();
    }
}
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
