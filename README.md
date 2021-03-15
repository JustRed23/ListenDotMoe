# ListenDotMoe

ListenDotMoe is a Java API used to get song information from [listen.moe](https://listen.moe/)

## Usage

```java
import dev.JustRed23.ListenDotMoe.Music.Song;
import dev.JustRed23.ListenDotMoe.Music.SongUpdateEvent;

public class SongClass implements SongUpdateEvent {

    @Override
    public void onSongUpdate(Song song) {
        //Handle song info here
    }
}

/******MAIN CLASS******/

import dev.JustRed23.ListenDotMoe.ListenDotMoe;

public class MainClass {

    public static void main(String[] args) {
        ListenDotMoe ldm = new ListenDotMoe();
        SongClass songClass = new SongClass();
        ldm.addSongEventHandler(songClass);
        ldm.start();
        
        //...
        ldm.stop();
    }
}
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
