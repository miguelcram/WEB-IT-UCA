package es.uca.iw.webituca.Model;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false)
    private String password;

    @Column(length = 25, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellidos;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    // @ManyToOne
    // private Centro centro;

    // @OneToMany
    // private List<Solicitud> solicitudes;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Solicitante'")
    @Enumerated(EnumType.STRING)
    //private Rol rol;

    // @Column(columnDefinition = "VARCHAR(255) DEFAULT 'icons/profile.svg'")
    // private String fotoPerfil = "icons/profile.svg";

    // public List<GrantedAuthority> getAuthorities() {
    //     return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.getRol().name()));
    // }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return usuario;
    }

    public void setUsername(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    // public Rol getRol() {
    //     return rol;
    // }

    // public void setRol(Rol rol) {
    //     this.rol = rol;
    // }

    // public Centro getCentro() {
    //     return centro;
    // }

    // // public String getFotoPerfil() { return fotoPerfil; }

    // // public void setFotoPerfil(String fotoPerfil) {
    // //     this.fotoPerfil = fotoPerfil;
    // // }

    // public void setCentro(Centro centro) {
    //     this.centro = centro;
    // }

    // public List<Solicitud> getSolicitudes() {
    //     return solicitudes;
    // }

    // public void setSolicitudes(List<Solicitud> solicitudes) {
    //     this.solicitudes = solicitudes;
    // }
}