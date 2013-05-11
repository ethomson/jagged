package org.libgit2.jagged.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A Platform represents a given {@link OperatingSystem} and processor
 * {@link Architecture}, useful for identifying the machine you're running on.
 */
public class Platform
{
    private final OperatingSystem operatingSystem;
    private final Architecture architecture;

    /**
     * Create a platform from the given {@link OperatingSystem} and
     * {@link Architecture}.
     * 
     * @param operatingSystem
     *        the operating system for this platform (not {@code null})
     * @param architecture
     *        the architecture for this platform (not {@code null})
     */
    public Platform(OperatingSystem operatingSystem, Architecture architecture)
    {
        Ensure.argumentNotNull(operatingSystem, "operatingSystem");
        Ensure.argumentNotNull(architecture, "architecture");

        this.operatingSystem = operatingSystem;
        this.architecture = architecture;
    }

    /**
     * Gets the {@link OperatingSystem} associated with this platform.
     * 
     * @return the operating system for this platform (never {@code null})
     */
    public OperatingSystem getOperatingSystem()
    {
        return operatingSystem;
    }

    /**
     * Gets the processor {@link Architecture} associated with this platform.
     * 
     * @return the processor architecture for this platform (never {@code null})
     */
    public Architecture getArchitecture()
    {
        return architecture;
    }

    @Override
    public int hashCode()
    {
        return (operatingSystem.ordinal() << 8) | architecture.ordinal();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Platform))
        {
            return false;
        }

        Platform other = (Platform) o;

        return (other.operatingSystem == operatingSystem && other.architecture == architecture);
    }

    private static Platform currentPlatform;

    static
    {
        currentPlatform =
            new Platform(
                OperatingSystem.getOperatingSystem(System.getProperty("os.name")),
                Architecture.getArchitecture(System.getProperty("os.arch")));
    }

    /**
     * Returns a {@link Platform} that represents the currently running
     * operating system and processor architecture.
     * 
     * @return the representing the current platform (never {@code null})
     */
    public static Platform getCurrentPlatform()
    {
        return currentPlatform;
    }

    public static enum OperatingSystem
    {
        /**
         * An unknown operating system.
         */
        UNKNOWN("Unknown", "unknown"),

        /**
         * The Windows operating system (32 or 64-bit, despite the name).
         */
        WINDOWS("Windows", "win32"),

        /**
         * Linux.
         */
        LINUX("Linux", "linux"),

        /**
         * FreeBSD.
         */
        FREEBSD("FreeBSD", "freebsd"),

        /**
         * Mac OS X or Darwin.
         */
        MAC_OS_X("Mac OS X", "macosx"),

        /**
         * Solaris a/k/a SunOS.
         */
        SOLARIS("Sun OS", "solaris"),

        /**
         * HP-UX.
         */
        HPUX("HP-UX", "hpux"),

        /**
         * AIX.
         */
        AIX("AIX", "aix"),

        /**
         * z/OS.
         */
        Z_OX("z/OS", "zos");

        private final String name;
        private final String osgiName;

        private static final Map<String, OperatingSystem> operatingSystems = new HashMap<String, OperatingSystem>();

        static
        {
            for (OperatingSystem operatingSystem : EnumSet.allOf(OperatingSystem.class))
            {
                operatingSystems.put(operatingSystem.getName(), operatingSystem);
            }
        }

        private OperatingSystem(final String name, final String osgiName)
        {
            this.name = name;
            this.osgiName = osgiName;
        }

        /**
         * Gets the proper name (user friendly) of the Operating System, for
         * example "Windows" or "Linux".
         * 
         * @return the name of the operating system (never {@code null})
         */
        public String getName()
        {
            return name;
        }

        /**
         * Gets the OSGI name of the Operating System ("osname").
         * 
         * @return the OSGI operating system name (never {@code null})
         */
        public String getOsgiName()
        {
            return osgiName;
        }

        /**
         * Given an operating system name, return the associated
         * {@code OperatingSystem}.
         * 
         * @param name
         *        the name of the operating system (not {@code null} or empty)
         * @return the associated operating system or
         *         {@link OperatingSystem#UNKNOWN} if unknown
         */
        public static OperatingSystem getOperatingSystem(final String name)
        {
            Ensure.argumentNotNullOrEmpty(name, "name");

            for (Entry<String, OperatingSystem> entry : operatingSystems.entrySet())
            {
                if (name.startsWith(entry.getKey()))
                {
                    return entry.getValue();
                }
            }

            return UNKNOWN;
        }
    }

    public static enum Architecture
    {
        /**
         * An unknown architecture.
         */
        UNKNOWN("unknown"),

        /**
         * Intel's 32-bit x86 architecture.
         */
        X86("x86", "i386"),

        /**
         * AMD's 64-bit architecture based on Intel's x86 instruction set.
         */
        X86_64("x86_64", "amd64"),

        /**
         * Sun's SPARC architecture in 32-bit (v8 and prior) or 64-bit (v9 and
         * later) architectures.
         */
        SPARC("sparc", "sparcv9"),

        /**
         * PowerPC in 32-bit and 64-bit architectures.
         */
        POWERPC_32("ppc", "ppc64"),

        /**
         * HP's PA-RISC architecture. PA_RISC 2.0 is 64-bit but can load 32-bit
         * binaries from 1.0 and 1.1 so we need not define them separately.
         */
        PA_RISC_20("PA_RISC2.0"),

        /**
         * Intel's Itanium (IA64) architecture in 32-bit ("N" for narrow) mode.
         */
        ITANIUM_32("ia32", "IA64N"),

        /**
         * Intel's Itanium (IA64) architecture running 64-bit ("W" for wide)
         * mode.
         */
        ITANIUM_64("ia64_32", "IA64W"),

        /**
         * IBM's zSeries mainframe architecture. Supports 31-bit (yes, not
         * 32-bit) and 64-bit applications. Java can be either 31- or 64-bit.
         */
        Z_ARCH("390", "s390"),

        /**
         * ARM 32-bit chipsets.
         */
        ARM_32("arm", "armv6l");

        private final String name;
        private final String[] aliases;

        private static final Map<String, Architecture> architectures = new HashMap<String, Architecture>();

        static
        {
            for (Architecture architecture : EnumSet.allOf(Architecture.class))
            {
                architectures.put(architecture.getName().toLowerCase(), architecture);

                for (String alias : architecture.getAliases())
                {
                    architectures.put(alias.toLowerCase(), architecture);
                }
            }
        }

        private Architecture(final String name, final String... aliases)
        {
            this.name = name;
            this.aliases = aliases;
        }

        /**
         * Gets the canonical name of this architecture.
         * 
         * @return the architecture name (never {@code null})
         */
        public String getName()
        {
            return name;
        }

        /**
         * Gets any aliases for this architecture, for example "x86_64" is
         * sometimes also called "amd64".
         * 
         * @return aliases for this architecture (never {@code null})
         */
        public String[] getAliases()
        {
            return aliases;
        }

        /**
         * Given an architecture name, return the associated
         * {@code Architecture}.
         * 
         * @param name
         *        the name of the architecture (not {@code null} or empty)
         * @return the associated architecture or {@link Architecture#UNKNOWN}
         *         if unknown
         */
        public static Architecture getArchitecture(final String name)
        {
            Architecture architecture = architectures.get(name.toLowerCase());

            if (architecture == null)
            {
                return UNKNOWN;
            }

            return architecture;
        }
    }
}
