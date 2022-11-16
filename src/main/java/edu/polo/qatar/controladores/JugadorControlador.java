package edu.polo.qatar.controladores;

import edu.polo.qatar.entidades.*;
import edu.polo.qatar.servicios.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.*;

@RestController
@RequestMapping("jugadores")
public class JugadorControlador {

    @Autowired
    JugadorServicio jugadorServicio;

    @Autowired
    SeleccionServicio seleccionServicio;

    @GetMapping
    private ModelAndView index()
    {
        ModelAndView maw = new ModelAndView();
        maw.setViewName("fragments/base");
        maw.addObject("titulo", "Listado de jugadores");
        maw.addObject("vista", "jugadores/index");
        maw.addObject("jugadores", jugadorServicio.getAll());
        return maw;
    }

    @GetMapping("/{id}")
    private ModelAndView one(@PathVariable("id") Long id)
    {
        ModelAndView maw = new ModelAndView();
        maw.setViewName("fragments/base");
        maw.addObject("titulo", "Detalle del jugador #" + id);
        maw.addObject("vista", "jugadores/ver");
        maw.addObject("jugador", jugadorServicio.getById(id));
        return maw;
    }

    @GetMapping("/crear")
    public ModelAndView crear()
    {
        ModelAndView maw = new ModelAndView();
        maw.setViewName("fragments/base");
        maw.addObject("titulo", "Crear jugador");
        maw.addObject("vista", "jugadores/crear");
        maw.addObject("selecciones", seleccionServicio.getAll());
        return maw;
    }

    @PostMapping
    private RedirectView save(@RequestParam("archivo") MultipartFile archivo, Jugador jugador)
    {
        jugadorServicio.save(jugador);

        String tipo = archivo.getContentType();
        String extension = "." + tipo.substring(tipo.indexOf('/') + 1, tipo.length());
        String foto = jugador.getId() + extension;
        String path = Paths.get("src/main/resources/static/images/jugadores", foto).toAbsolutePath().toString();

        try {
            archivo.transferTo( new File(path) );
        } catch (Exception e) {
            return new RedirectView("/jugadores");
        }

        jugador.setFoto(foto);
        jugadorServicio.save(jugador);
        return new RedirectView("/jugadores/editar/" + jugador.getId());
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id)
    {
        ModelAndView maw = new ModelAndView();
        maw.setViewName("fragments/base");
        maw.addObject("titulo", "Editar jugador");
        maw.addObject("vista", "jugadores/editar");
        maw.addObject("selecciones", seleccionServicio.getAll());
        maw.addObject("jugador", jugadorServicio.getById(id));
        return maw;
    }

    @PutMapping("/{id}")
    private Object update(@PathVariable("id") Long id,
    @RequestParam(value = "archivo", required = false) MultipartFile archivo,
    @ModelAttribute Jugador jugador)
    {
        Jugador registro = jugadorServicio.getById(id);
        registro.setNombre( jugador.getNombre() );
        registro.setFechaNacimiento( jugador.getFechaNacimiento() );
        registro.setSeleccion( jugador.getSeleccion() );

        if ( ! archivo.isEmpty() ) {
            String tipo = archivo.getContentType();
            String extension = "." + tipo.substring(tipo.indexOf('/') + 1, tipo.length());
            String foto = jugador.getId() + extension;
            String path = Paths.get("src/main/resources/static/images/jugadores", foto).toAbsolutePath().toString();

            try {
                archivo.transferTo( new File(path) );
            } catch (Exception e) {
                return e.toString();
            }

            registro.setFoto(foto);
        }

        jugadorServicio.save(registro);
        return new RedirectView("/jugadores");
    }

    @DeleteMapping("/{id}")
    private RedirectView delete(@PathVariable("id") Long id)
    {
        jugadorServicio.delete(id);
        return new RedirectView("/jugadores");
    }
    
}