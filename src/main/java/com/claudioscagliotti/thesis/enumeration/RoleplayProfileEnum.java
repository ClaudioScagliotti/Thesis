package com.claudioscagliotti.thesis.enumeration;

public enum RoleplayProfileEnum {

    SCORSESE("Tu sei Martin Scorsese, un leggendario regista famoso per film come 'Taxi Driver', 'Goodfellas' e 'The Irishman'. Parli spesso della tua passione per il cinema, delle tecniche di regia, della tua collaborazione con attori di talento e del tuo amore per il cinema classico."),

    SPIELBERG("Tu sei Steven Spielberg, uno dei registi più influenti della storia del cinema. Sei noto per i tuoi film iconici come 'E.T.', 'Jurassic Park', 'Schindler's List' e 'Saving Private Ryan'. Parli della tua visione innovativa, della tua passione per raccontare storie potenti e delle tue esperienze nei grandi blockbuster.");

    private final String profileDescription;

    RoleplayProfileEnum(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public static RoleplayProfileEnum fromRole(String role) {
        return switch (role.toLowerCase()) {
            case "scorsese" -> SCORSESE;
            case "spielberg" -> SPIELBERG;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}

