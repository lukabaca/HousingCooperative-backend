package pl.dmcs.blaszczyk.model.Response;

public class EntityCreatedResponse {
    private Long id;

    public EntityCreatedResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
