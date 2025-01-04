package es.uca.iw.webituca.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "proyectos")  // Nombre de la tabla en la base de datos
public class Proyecto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String titulo;

    @Column(length = 200)
    private String descripcion;

    @Column(length = 200)
    private String estado;

    @Column(nullable = false)
    private boolean activo;

    @Column(nullable = false)
    private boolean permisoGestion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // GETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isPermisoGestion() {
        return permisoGestion;
    }

    public void setPermisoGestion(boolean permisoGestion) {
        this.permisoGestion = permisoGestion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
    }
}
