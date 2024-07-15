package org.zydd.bebtpn.dto;

public class ResponHeaderMessage {
    public static ResponHeader getRequestSuccess() {
        return new ResponHeader("200", true, "Request successfully processed.");
    }

    public static ResponHeader getDataCreated() {
        return new ResponHeader("201", true, "Session successfully created.");
    }

    public static ResponHeader getBadRequestError() {
        return new ResponHeader("400", false, "Please check the data you entered and try again.");
    }

    public static ResponHeader getUnauthorized() {
        return new ResponHeader("401", false, "Username not found or password incorrect.");
    }

    public static ResponHeader getForbiddenAccess() {
        return new ResponHeader("403", false, "You are not allowed to access this resource.");
    }

    public static ResponHeader getInputValidationError() {
        return new ResponHeader("422", false, "The data you entered is invalid, please try again.");
    }

    public static ResponHeader getDataNotFound() {
        return new ResponHeader("404", false, "Data / page not found.");
    }

    public static ResponHeader getServerUnknownError() {
        return new ResponHeader("500", false, "There is a problem with the server, contact the administrator for more information.");
    }
}
