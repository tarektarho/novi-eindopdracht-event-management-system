package nl.novi.event_management_system.enums;

/**
 * Enum for the different roles a user can have.
 */
public enum RoleEnum {
    ADMIN, // access to all. Can create delete update events and view feedback and reservations and view all users
    ORGANIZER, // ORGANIZER + endUser // organizer can create delete update events also view them and view feedback and reservations
    PARTICIPANT;// endUser // participant can register for events and view events and submit feedback and make reservations also upload files and download files user can also buy ticket

    /**
     * Get the role name for a given role.
     *
     * @param roleEnum The role to get the name for.
     * @return The role name.
     */
    public static String getRoleName(RoleEnum roleEnum) {
        return "ROLE_" + roleEnum.name();
    }
}