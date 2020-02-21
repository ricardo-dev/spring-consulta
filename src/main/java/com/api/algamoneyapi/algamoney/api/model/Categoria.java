package com.api.algamoneyapi.algamoney.api.model;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;


@Entity
@Table(name="categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=3, max=20)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(id, categoria.id) &&
                Objects.equals(name, categoria.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
