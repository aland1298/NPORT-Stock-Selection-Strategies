package util;

import java.util.Arrays;

public class EnumTypeUtils {
    /**
     * Checks if the enum class contains an enum constant with the given typeId and name, case-insensitive.
     *
     * @param <T>       the enum type parameter
     * @param enumClass the class of the enum
     * @param typeId    the typeId of the enum constant to check for
     * @param typeName  the name of the enum constant to check for
     * @return true if the enum class contains an enum constant with the given typeId and name, case-insensitive; false otherwise
     */
    public static <T extends Enum<T> & EnumWithIdAndName> boolean isValidEnum(Class<T> enumClass, int typeId, String typeName) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumVal -> enumVal.getTypeId() == typeId)
                .findFirst()
                .map(enumVal -> enumVal.getName().equalsIgnoreCase(typeName))
                .orElse(false);
    }

    /**
     * Returns the typeId of the enum constant with the given name, case-insensitive.
     *
     * @param <T>       the enum type parameter
     * @param enumClass the class of the enum
     * @param name      the name of the enum constant
     * @return the typeId of the enum constant with the given name
     * @throws IllegalArgumentException if the name does not match any of the enum constants in the enum class
     */
    public static <T extends Enum<T> & EnumWithIdAndName> int getTypeIdByName(Class<T> enumClass, String name) throws IllegalArgumentException {
        for (T enumVal : enumClass.getEnumConstants()) {
            if (enumVal.getName().equalsIgnoreCase(name)) {
                return enumVal.getTypeId();
            }
        }
        throw new IllegalArgumentException("Invalid enum name: " + name);
    }

    /**
     * Returns the Enum of the enum constant with the given name, case-insensitive.
     *
     * @param <T>       the enum type parameter
     * @param enumClass the class of the enum
     * @param name      the name of the enum constant
     * @return the enum of the enum constant with the given name
     * @throws IllegalArgumentException if the name does not match any of the enum constants in the enum class
     */
    public static <T extends Enum<T> & EnumWithIdAndName> T getEnumByName(Class<T> enumClass, String name) throws IllegalArgumentException {
        for (T enumVal : enumClass.getEnumConstants()) {
            if (enumVal.getName().equalsIgnoreCase(name)) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("Invalid enum name: " + name);
    }

    public interface EnumWithIdAndName {
        int getTypeId();
        String getName();
    }

    public enum FormType implements EnumWithIdAndName {
        NPORT_P(1, "NPORT-P");

        private final int typeId;
        private final String name;

        FormType(int typeId, String name) {
            this.typeId = typeId;
            this.name = name;
        }

        public int getTypeId() {
            return typeId;
        }

        public String getName() {
            return name;
        }
    }
}
