package io.bootify.agenda_gym.events;


public class BeforeDeleteUsuario {

    private Long id;

    public BeforeDeleteUsuario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
