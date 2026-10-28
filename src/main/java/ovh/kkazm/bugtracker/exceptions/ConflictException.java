package ovh.kkazm.bugtracker.exceptions;

public class ConflictException extends RuntimeException {
    private final String resource;
    private final Object property;

    public ConflictException(String resource, Object property) {
        super("%s with this %s already exists.".formatted(resource, property));
        this.resource = resource;
        this.property = property;
    }

    public Object getProperty() {
        return property;
    }

    public String getResource() {
        return resource;
    }

}
