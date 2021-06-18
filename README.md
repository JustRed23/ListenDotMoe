# ListenDotMoe

ListenDotMoe is a Java API used to get song information from [listen.moe](https://listen.moe/)

## Usage

```java
package com.example;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;

import java.util.concurrent.TimeUnit;

public class Example {

    public static void main(String[] args) throws InterruptedException {
        //Create a new instance
        ListenDotMoe ldm = new ListenDotMoe();
        //Add the song event handler
        ldm.addSongEventHandler(song -> System.out.println("Song title: " + song.getTitle()));
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
