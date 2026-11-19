package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.dto.ReporteVentasDTO;
import co.edu.sena.sellpal.service.ReporteService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/reportes")
public class ReporteController {
 private final ReporteService service;
 public ReporteController(ReporteService service){this.service=service;}
 @GetMapping("/ventas-estado") public List<ReporteVentasDTO> ventasPorEstado(){return service.ventasPorEstado();}
 @GetMapping("/resumen") public Map<String,Object> resumen(){return service.resumen();}
}
