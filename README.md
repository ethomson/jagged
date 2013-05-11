# jagged

jagged provides Java language bindings to [libgit2][0], a native Git
implementation.

[0]: https://github.com/libgit2/libgit2

## What is this madness?

This is very experimental, very mediocre JNI bindings for libgit2.
Almost nothing is bound, short of ensuring that this crazy idea might
work.  It turns out that it might.

You probably actually want to be using [jgit][1].

[1]: http://www.jgit.org

## How do I work with this?

Apologies to Maven lovers, this setup is abysmal at the moment.

1. Build libgit2 for your platform:

        cd src/main/native/libgit2
    	mkdir build
    	cd build
    	cmake .. -DSONAME=OFF
    	cmake --build . --config RelWithDebInfo
    	make

2. Copy the resultant binaries to `native/[osname]/[arch]` where `[osname]`
   and `[arch]` represent the OSGI operating system and processor identifiers.
   eg, `native/linux/x86`.  (The exception being on Mac OS, where we build
   fat binaries.)

3. Build JNI libraries for your platform:

        cd src/main/native/libjagged
    	make

4. Copy the resultant binaries to `native/[osname]/[arch]`, the same place
   as in step 2.

5. Now you can build the Java (eg `mvn install`).

## License

Available under the MIT license (refer to the [LICENSE][2] file).

[2]: https://github.com/ethomson/jagged/blob/master/LICENSE
