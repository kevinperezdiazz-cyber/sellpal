package co.edu.sena.sellpal.service;

import co.edu.sena.sellpal.dto.ReporteVentasDTO;
import co.edu.sena.sellpal.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {
 private final PedidoRepository pedidos;
 public ReporteService(PedidoRepository pedidos){this.pedidos=pedidos;}
 public List<ReporteVentasDTO> ventasPorEstado(){
  return pedidos.findAll().stream().collect(Collectors.groupingBy(p->p.getEstado()==null?"SIN ESTADO":p.getEstado().getNombre_estado(),Collectors.toList())).entrySet().stream().map(e->new ReporteVentasDTO(e.getKey(),(long)e.getValue().size(),e.getValue().stream().map(p->p.getTotal()==null?BigDecimal.ZERO:p.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add))).sorted(Comparator.comparing(ReporteVentasDTO::estado)).toList();
 }
 public Map<String,Object> resumen(){var rows=ventasPorEstado(); return Map.of("pedidos",rows.stream().mapToLong(ReporteVentasDTO::cantidadPedidos).sum(),"ventas",rows.stream().map(ReporteVentasDTO::total).reduce(BigDecimal.ZERO,BigDecimal::add),"porEstado",rows);}
}
