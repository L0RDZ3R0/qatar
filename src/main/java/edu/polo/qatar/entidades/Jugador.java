package edu.polo.qatar.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.*;
import javax.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name="jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @ManyToOne
    @JsonBackReference
    private Seleccion seleccion;

    private String foto;

}