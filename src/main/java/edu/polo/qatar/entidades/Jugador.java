package edu.polo.qatar.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 250, message= "Nombre demasiado largo")
    private String nombre;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Campo obligatorio")
    @Past(message = "La fecha de nacimiento no puede ser futura")
    private Date fechaNacimiento;

    @ManyToOne
    @JsonBackReference
    @NotNull(message = "Debe elegir un valor")
    private Seleccion seleccion;

    private String foto;

}