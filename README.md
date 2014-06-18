# jagged

jagged provides Java language bindings to [libgit2][0], a native Git
implementation.

See the javadoc at [http://ethomson.github.io/jagged][1].

[0]: https://github.com/libgit2/libgit2
[1]: http://ethomson.github.io/jagged

## What is this madness?

This is very experimental, very mediocre JNI bindings for libgit2.
Almost nothing is bound, short of ensuring that this crazy idea might
work.  It turns out that it might.

You probably actually want to be using [jgit][2].

[2]: http://www.eclipse.org/jgit/

## How do I work with this?

Apologies to Maven lovers, this setup is abysmal at the moment.


1. Get the included libgit2 submodule:  

        git submodule init
        git submodule update


2. Build the natives (libgit2 and libjagged) for your platform, 

 i) To build libgit2  

        cd src/main/native/libgit2
        mkdir build && cd build
        cmake ..
        make
        sudo make install #WARNING: This will install libgit2 on your system

 ii) To build libjagged  

            cd src/main/native/libjagged
            mkdir build && cd build
            cmake ..  #If this fails because of missing JNI, follow this SO thread http://stackoverflow.com/a/21383730/377816
            make

3. Now you can build the Java (eg `mvn install`).

## Using Eclipse

Quick start guide for those unfamiliar with Maven and Eclipse:

1. Determine the location of your Eclipse Workspace (this cannot be inside
   or beneath the jagged folder.)  Then configure the workspace directory
   for Maven and set up the Eclipse project files:

        mvn -Declipse.workspace=..\jagged-eclipse eclipse:configure-workspace
        mvn eclipse:eclipse

2. Open Eclipse, selecting the Eclipse workspace configured in step 1.
3. Select File > Import > General > Existing Projects into Workspace.  In the
   "Select Root Directory" prompt, browse to the location of your jagged
   source.

## License

Available under the MIT license (refer to the [LICENSE][3] file).

[3]: https://github.com/ethomson/jagged/blob/master/LICENSE
