package team.bupt.h7.exceptions

enum class ErrorCode(val errorCode: String, val errorMessage: String) {
    InsufficientPermissions(
        "INSUFFICIENT_PERMISSIONS",
        "You do not have sufficient permissions to perform this action."
    ),
    InvalidPassword("INVALID_PASSWORD", "The provided password is incorrect."),
    ReauthenticationRequired(
        "REAUTH_REQUIRED",
        "Reauthentication with your password is required for this operation."
    ),
    UserExists("USER_EXISTS", "The user you are trying to create already exists."),
    UserNotFound("USER_NOT_FOUND", "The requested user could not be found."),
    UsernameOrPasswordInvalid(
        "USERNAME_OR_PASSWORD_INVALID",
        "The provided username or password is invalid."
    ),
    InvalidUrlParameters("INVALID_URL_PARAM", "The provided URL parameters are invalid."),
    PlaceSeekerNotFound("PLACE_SEEKER_NOT_FOUND", "The requested place seeker could not be found."),
    WelcomeOfferNotFound(
        "WELCOME_OFFER_NOT_FOUND",
        "The requested welcome offer could not be found."
    ),
    UserNotOwner("USER_NOT_OWNER", "The user is not the owner of the resource."),
    WelcomeOfferNotActive("OFFER_NOT_ACTIVE", "You can only accept or decline active offers."),
    PlaceSeekerNotActive(
        "PLACE_SEEKER_NOT_ACTIVE",
        "The corresponding place seeker is not active."
    ),
}
